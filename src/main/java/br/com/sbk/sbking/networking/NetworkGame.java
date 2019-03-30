package br.com.sbk.sbking.networking;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;

public class NetworkGame {

	private Deal deal;
	private List<PlayerSocket> playerSockets = new ArrayList<PlayerSocket>();
	private ExecutorService pool;
	final static Logger logger = Logger.getLogger(NetworkGame.class);
	private CardPlayNotification event = new CardPlayNotification();

	public NetworkGame(ExecutorService pool, Deal deal) {
		this.deal = deal;
		this.pool = pool;
	}

	public void connectPlayer(Socket socket, Direction direction) {

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

	public void run() {
		while (!this.deal.isFinished()) {
			logger.info("Starting new 'round' of deals");
			logger.info("Sleeping for 500ms waiting for all clients to prepare themselves.");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			sendDealAll(this.deal);
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
			this.playCard(cardToBePlayed, directionToBePlayed);
		}
	}

	private void sendDealAll(Deal deal) {
		logger.info("Sending everyone the current deal");
		for (PlayerSocket playerSocket : playerSockets) {
			playerSocket.sendDeal(this.deal);
		}
		logger.info("Finished sending deals.");
	}

	private void playCard(Card card, Direction direction) {
		logger.info("It is currently the " + this.deal.getCurrentPlayer() + " turn");
		try {
			if (this.deal.getCurrentPlayer() == direction) {
				syncPlayCard(card);
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
		logger.info("Leaving synchronized play card");
	}

	public synchronized void removePlayerSocket(PlayerSocket playerSocket) {
		this.playerSockets.remove(playerSocket);
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
