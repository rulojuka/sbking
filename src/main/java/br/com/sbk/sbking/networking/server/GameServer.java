package br.com.sbk.sbking.networking.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.TrickGame;
import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import br.com.sbk.sbking.networking.server.notifications.CardPlayNotification;

public abstract class GameServer implements Runnable {
  protected CardPlayNotification cardPlayNotification = new CardPlayNotification();
	protected boolean dealHasChanged;
	protected Direction nextDirection = Direction.values()[0];

	protected Collection<PlayerGameSocket> playerSockets = new ArrayList<PlayerGameSocket>();
	protected Collection<SpectatorGameSocket> spectatorSockets = new ArrayList<SpectatorGameSocket>();
  protected MessageSender messageSender;
  
  protected TrickGame game;

  protected ExecutorService pool;

  final static Logger logger = LogManager.getLogger(KingGameServer.class);
  
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

	protected Direction getNextDirection() {
		Direction nextDirection = this.nextDirection;
		this.nextDirection = this.nextDirection.next();
		return nextDirection;
  }
  
  public void removeClientGameSocket(ClientGameSocket playerSocket) {
		this.playerSockets.remove(playerSocket);
		this.spectatorSockets.remove(playerSocket);
  }
  
  protected void playCard(Card card, Direction direction) {
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

	protected synchronized void syncPlayCard(Card card) {
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

	protected void sleepFor(int miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
			logger.debug(e);
		}
	}

	protected Collection<ClientGameSocket> getAllSockets(){
		Collection<ClientGameSocket> allSockets = new ArrayList<ClientGameSocket>();
		allSockets.addAll(playerSockets);
		allSockets.addAll(spectatorSockets);
		return allSockets;
	}

}
