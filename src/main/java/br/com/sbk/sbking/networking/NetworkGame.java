package br.com.sbk.sbking.networking;

import org.apache.log4j.Logger;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;

public class NetworkGame {

	private Deal deal;
	final static Logger logger = Logger.getLogger(NetworkGame.class);
	private CardPlayNotification event = new CardPlayNotification();
	private GameServer gameServer;
	private boolean dealHasChanged;

	public NetworkGame(GameServer gameServer, Deal deal) {
		this.gameServer = gameServer;
		this.deal = deal;
	}

	public void run() {
		this.dealHasChanged = true;
		while (!this.deal.isFinished()) {
			logger.info("Sleeping for 500ms waiting for all clients to prepare themselves.");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(this.dealHasChanged) {
				logger.info("Sending new 'round' of deals");
				this.gameServer.sendDealAll(this.deal);
				this.dealHasChanged = false;
			}
			synchronized (event) {
				// wait until object notifies - which relinquishes the lock on the object too
				try {
					logger.info("I am waiting for some thread to notify that it wants to play a card.");
					event.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Direction directionToBePlayed = event.getDirection();
			Card cardToBePlayed = event.getCard();
			logger.info("Received notification that " + directionToBePlayed + " wants to play the " + cardToBePlayed);
			try {
				this.playCard(cardToBePlayed, directionToBePlayed);
			}catch (Exception e) {
				throw e;
			}
		}
	}

	private void playCard(Card card, Direction direction) {
		logger.info("It is currently the " + this.deal.getCurrentPlayer() + " turn");
		try {
			if (this.deal.getCurrentPlayer() == direction) {
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
		this.deal.playCard(card);
		logger.info("Finished playing the " + card);
		logger.info("Leaving synchronized play card");
	}

	public void notifyPlayCard(Card card, Direction direction) {
		synchronized (event) {
			logger.info("Started notifying main thread that I(" + direction + ") want to play the " + card);
			// release all waiters
			event.notifyAllWithCardAndDirection(card, direction);
			logger.info("Finished notifying main thread that I(" + direction + ") want to play the " + card);
		}
	}

}
