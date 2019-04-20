package br.com.sbk.sbking.networking.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Game;
import br.com.sbk.sbking.core.exceptions.SelectedPositiveOrNegativeInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.core.properties.FileProperties;
import br.com.sbk.sbking.networking.core.properties.NetworkingProperties;
import br.com.sbk.sbking.networking.core.properties.SystemProperties;
import br.com.sbk.sbking.networking.core.serialization.Serializator;
import br.com.sbk.sbking.networking.server.notifications.GameModeOrStrainNotification;
import br.com.sbk.sbking.networking.server.notifications.PositiveOrNegativeNotification;

public class GameServer {

	private static final String NETWORKING_CONFIGURATION_FILENAME = "networkConfiguration.cfg";
	private static final int COULD_NOT_GET_PORT_FROM_PROPERTIES_ERROR = 1;

	final static Logger logger = Logger.getLogger(GameServer.class);

	private NetworkGame networkGame;
	private PositiveOrNegativeNotification positiveOrNegativeNotification = new PositiveOrNegativeNotification();
	private PositiveOrNegative currentPositiveOrNegative;
	private GameModeOrStrainNotification gameModeOrStrainNotification = new GameModeOrStrainNotification();
	private Ruleset currentGameModeOrStrain;

	private Game game;
	private boolean isRulesetPermitted;

	private static final int MAXIMUM_NUMBER_OF_PLAYERS_IN_THE_LOBBY = 20;
	private ExecutorService pool;
	private List<PlayerSocket> playerSockets = new ArrayList<PlayerSocket>();
	private MessageSender messageSender;

	public GameServer() {
		pool = Executors.newFixedThreadPool(MAXIMUM_NUMBER_OF_PLAYERS_IN_THE_LOBBY);
	}

	private void connectPlayer(Socket socket, Direction direction) {

		Serializator serializator = null;
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			serializator = new Serializator(objectInputStream, objectOutputStream);
		} catch (Exception e) {
			logger.debug(e);
		}

		PlayerSocket current = new PlayerSocket(serializator, socket, direction, this);
		playerSockets.add(current);
		pool.execute(current);
	}

	public void run() throws Exception {
		int port = this.getPortFromNetworkingProperties();

		try (ServerSocket listener = new ServerSocket(port)) {
			logger.info("Game Server is Running...");
			logger.info("My InetAddress is: " + listener.getInetAddress());
			logger.info("Listening for connections on port: " + port);
			for (Direction direction : Direction.values()) {
				this.connectPlayer(listener.accept(), direction);
			}

			logger.info("Sleeping for 1000ms waiting for last client to setup itself");
			Thread.sleep(1000);
			this.messageSender = new MessageSender(playerSockets);

			this.messageSender.sendMessageAll("ALLCONNECTED");

			this.game = new Game();

			while (!game.isFinished()) {
				this.game.initializeBoard();

				do {

					this.messageSender.sendInitializeDealAll();
					logger.info("Sleeping for 500ms waiting for clients to initialize its deals.");
					Thread.sleep(500);

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
				Thread.sleep(500);

				logger.info("Everything selected! Game commencing!");
				this.game.addRuleset(currentGameModeOrStrain);

				this.networkGame = new NetworkGame(this, this.game.getCurrentDeal());
				networkGame.run();

				this.game.finishDeal();
				this.messageSender.sendGameScoreboardAll(this.game.getGameScoreboard());

				this.messageSender.sendFinishDealAll();
				logger.info("Deal finished!");

			}

			this.messageSender.sendFinishGameAll();

			logger.info("Game has ended. Exiting main thread.");
		}
	}

	public void removePlayerSocket(PlayerSocket playerSocket) {
		this.playerSockets.remove(playerSocket);
	}

	public void notifyPlayCard(Card playedCard, Direction direction) {
		this.networkGame.notifyPlayCard(playedCard, direction);
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

	private int getPortFromNetworkingProperties() {
		FileProperties fileProperties = new FileProperties(NETWORKING_CONFIGURATION_FILENAME);
		int port = 0;
		try {
			NetworkingProperties networkingProperties = new NetworkingProperties(fileProperties,
					new SystemProperties());
			port = networkingProperties.getPort();
		} catch (Exception e) {
			logger.fatal("Could not get port from properties.");
			logger.debug(e);
			System.exit(COULD_NOT_GET_PORT_FROM_PROPERTIES_ERROR);
		}

		return port;
	}

	// FIXME Refactor this method out of here ASAP
	// Whoever is calling him, should be calling the messageSender directly in this
	// case
	public void sendDealAll(Deal deal) {
		this.messageSender.sendDealAll(deal);
	}

}
