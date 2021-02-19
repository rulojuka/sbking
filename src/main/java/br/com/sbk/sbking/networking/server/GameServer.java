package br.com.sbk.sbking.networking.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Game;
import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.exceptions.SelectedPositiveOrNegativeInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.server.notifications.CardPlayNotification;
import br.com.sbk.sbking.networking.server.notifications.GameModeOrStrainNotification;
import br.com.sbk.sbking.networking.server.notifications.PositiveOrNegativeNotification;

public class GameServer implements Runnable {

	final static Logger logger = Logger.getLogger(GameServer.class);

	private static final int NUMBER_OF_PLAYERS_AND_KIBITZERS_IN_A_GAME = 20;

	private PositiveOrNegativeNotification positiveOrNegativeNotification = new PositiveOrNegativeNotification();
	private PositiveOrNegative currentPositiveOrNegative;
	private GameModeOrStrainNotification gameModeOrStrainNotification = new GameModeOrStrainNotification();
	private Ruleset currentGameModeOrStrain;
	private boolean dealHasChanged;
	private CardPlayNotification cardPlayNotification = new CardPlayNotification();

	private Game game;
	private boolean isRulesetPermitted;

	private Collection<ClientGameSocket> playerSockets = new ArrayList<ClientGameSocket>();
	private Collection<ClientGameSocket> spectatorSockets = new ArrayList<ClientGameSocket>();
	private MessageSender messageSender;

	private ExecutorService pool;
	private Direction nextDirection = Direction.values()[0];

	public GameServer() {
		this.pool = Executors.newFixedThreadPool(NUMBER_OF_PLAYERS_AND_KIBITZERS_IN_A_GAME);
	}

	public void addPlayer(PlayerNetworkInformation playerNetworkInformation) {
		PlayerGameSocket currentPlayerGameSocket = new PlayerGameSocket(playerNetworkInformation, getNextDirection(), this);
		this.playerSockets.add(currentPlayerGameSocket);
		pool.execute(currentPlayerGameSocket);
	}

	public void addSpectator(PlayerNetworkInformation playerNetworkInformation) {
		SpectatorGameSocket spectatorGameSocket = new SpectatorGameSocket(playerNetworkInformation, this);
		this.spectatorSockets.add(spectatorGameSocket);
		this.messageSender.addClientGameSocket(spectatorGameSocket);
		logger.info("Info do spectator:" + spectatorGameSocket);
		pool.execute(spectatorGameSocket);
	}

	private Direction getNextDirection() {
		Direction nextDirection = this.nextDirection;
		this.nextDirection = this.nextDirection.next();
		return nextDirection;
	}

	@Override
	public void run() {
		this.messageSender = new MessageSender(playerSockets);

		logger.info("Sleeping for 1000ms waiting for last client to setup itself");
		sleepFor(1000);

		this.messageSender.sendMessageAll("ALLCONNECTED");

		this.game = new Game();
		
		while (!game.isFinished()) {
			this.game.dealNewBoard();

			do {

				this.messageSender.sendInitializeDealAll();
				logger.info("Sleeping for 500ms waiting for clients to initialize its deals.");
				sleepFor(500);

				this.messageSender.sendBoardAll(this.game.getCurrentBoard());

				this.messageSender.sendChooserPositiveNegativeAll(this.getCurrentPositiveOrNegativeChooser());

				synchronized (positiveOrNegativeNotification) {
					// wait until object notifies - which relinquishes the lock on the object too
					try {
						logger.info(
								"I am waiting for some thread to notify that it wants to choose positive or negative");
						positiveOrNegativeNotification.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				logger.info("I received that is going to be "
						+ positiveOrNegativeNotification.getPositiveOrNegative().toString());
				this.currentPositiveOrNegative = positiveOrNegativeNotification.getPositiveOrNegative();
				this.messageSender.sendPositiveOrNegativeAll(this.currentPositiveOrNegative);

				this.messageSender.sendChooserGameModeOrStrainAll(this.getCurrentGameModeOrStrainChooser());

				synchronized (gameModeOrStrainNotification) {
					// wait until object notifies - which relinquishes the lock on the object too
					try {
						logger.info(
								"I am waiting for some thread to notify that it wants to choose game Mode Or Strain");
						gameModeOrStrainNotification.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				logger.info("I received that is going to be "
						+ gameModeOrStrainNotification.getGameModeOrStrain().getShortDescription());
				this.currentGameModeOrStrain = gameModeOrStrainNotification.getGameModeOrStrain();

				isRulesetPermitted = this.game.isGameModePermitted(this.currentGameModeOrStrain,
						this.getCurrentGameModeOrStrainChooser());

				if (!isRulesetPermitted) {
					logger.info("This ruleset is not permitted. Restarting choose procedure");
					this.messageSender.sendInvalidRulesetAll();
				} else {
					this.messageSender.sendValidRulesetAll();
				}

			} while (!isRulesetPermitted);

			this.messageSender
					.sendGameModeOrStrainShortDescriptionAll(this.currentGameModeOrStrain.getShortDescription());

			logger.info("Sleeping for 500ms waiting for everything come out right.");
			sleepFor(500);

			logger.info("Everything selected! Game commencing!");
			this.game.addRuleset(currentGameModeOrStrain);
			
			Deal currentDeal = this.game.getCurrentDeal();
			for (ClientGameSocket socket : playerSockets) {
				PlayerGameSocket playerGameSocket = (PlayerGameSocket) socket;
				logger.info("Socket:" + playerGameSocket);
				logger.info("Direction:" + playerGameSocket.getDirection());
				logger.info("Player:" + playerGameSocket.getPlayer());
				logger.info("CurrentDeal:" + currentDeal);
				currentDeal.setPlayerOf(playerGameSocket.getDirection(), playerGameSocket.getPlayer());
			}

			this.dealHasChanged = true;
			while (!this.game.getCurrentDeal().isFinished()) {
				logger.info("Sleeping for 500ms waiting for all clients to prepare themselves.");
				sleepFor(500);
				if (this.dealHasChanged) {
					logger.info("Sending new 'round' of deals");
					this.messageSender.sendDealAll(this.game.getCurrentDeal());
					this.dealHasChanged = false;
				}
				synchronized (cardPlayNotification) {
					// wait until object notifies - which relinquishes the lock on the object too
					try {
						logger.info("I am waiting for some thread to notify that it wants to play a card.");
						cardPlayNotification.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Direction directionToBePlayed = cardPlayNotification.getDirection();
				Card cardToBePlayed = cardPlayNotification.getCard();
				logger.info(
						"Received notification that " + directionToBePlayed + " wants to play the " + cardToBePlayed);
				try {
					this.playCard(cardToBePlayed, directionToBePlayed);
				} catch (Exception e) {
					throw e;
				}
			}

			this.game.finishDeal();
			this.messageSender.sendGameScoreboardAll(this.game.getGameScoreboard());

			this.messageSender.sendFinishDealAll();
			logger.info("Deal finished!");

		}

		this.messageSender.sendFinishGameAll();

		logger.info("Game has ended.");

	}

	public void removeClientGameSocket(ClientGameSocket playerSocket) {
		this.playerSockets.remove(playerSocket);
		this.spectatorSockets.remove(playerSocket);
	}

	public void notifyChoosePositiveOrNegative(PositiveOrNegative positiveOrNegative, Direction direction) {
		synchronized (positiveOrNegativeNotification) {
			if (this.getCurrentPositiveOrNegativeChooser() == direction) {
				this.positiveOrNegativeNotification.notifyAllWithPositiveOrNegative(positiveOrNegative);
			} else {
				throw new SelectedPositiveOrNegativeInAnotherPlayersTurnException();
			}
		}
	}

	public void notifyChooseGameModeOrStrain(Ruleset gameModeOrStrain, Direction direction) {
		synchronized (gameModeOrStrainNotification) {
			if (this.getCurrentGameModeOrStrainChooser() == direction) {
				this.gameModeOrStrainNotification.notifyAllWithGameModeOrStrain(gameModeOrStrain);
			} else {
				throw new SelectedPositiveOrNegativeInAnotherPlayersTurnException();
			}
		}
	}

	private Direction getCurrentPositiveOrNegativeChooser() {
		return this.game.getDealer().getPositiveOrNegativeChooserWhenDealer();
	}

	private Direction getCurrentGameModeOrStrainChooser() {
		return this.game.getDealer().getGameModeOrStrainChooserWhenDealer();
	}

	private void playCard(Card card, Direction direction) {
		logger.info("It is currently the " + this.game.getCurrentDeal().getCurrentPlayer() + " turn");
		try {
			if (this.game.getCurrentDeal().getCurrentPlayer() == direction) {
				syncPlayCard(card);
				this.dealHasChanged = true;
			} else {
				throw new PlayedCardInAnotherPlayersTurnException();
			}
		} catch (Exception e) {
			logger.debug(e);
		}
	}

	private synchronized void syncPlayCard(Card card) {
		logger.info("Entering synchronized play card");
		this.game.getCurrentDeal().playCard(card);
		logger.info("Leaving synchronized play card");
	}

	public void notifyPlayCard(Card card, Direction direction) {
		synchronized (cardPlayNotification) {
			logger.info("Started notifying main thread that I(" + direction + ") want to play the " + card);
			// release all waiters
			cardPlayNotification.notifyAllWithCardAndDirection(card, direction);
			logger.info("Finished notifying main thread that I(" + direction + ") want to play the " + card);
		}
	}

	private void sleepFor(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
			logger.debug(e);
		}
	}
	
}
