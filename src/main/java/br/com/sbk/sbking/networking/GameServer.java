package br.com.sbk.sbking.networking;

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
import br.com.sbk.sbking.core.exceptions.SelectedPositiveOrNegativeInAnotherPlayersTurnException;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;

public class GameServer {

	final static Logger logger = Logger.getLogger(GameServer.class);
	private List<PlayerSocket> playerSockets = new ArrayList<PlayerSocket>();
	private ExecutorService pool;
	private NetworkGame networkGame;
	private Direction currentChooser = Direction.EAST;
	private PositiveOrNegativeNotification event = new PositiveOrNegativeNotification();

	public GameServer() {
		pool = Executors.newFixedThreadPool(4);
	}

	public void run() throws Exception {
		try (ServerSocket listener = new ServerSocket(60000)) {
			logger.info("Game Server is Running...");
			for (Direction direction : Direction.values()) {
				this.connectPlayer(listener.accept(), direction);
			}

			logger.info("Sleeping for 500ms waiting for last client to setup itself");
			Thread.sleep(500);

			this.sendMessageAll("ALLCONNECTED");

			while (true) {
				this.sendChooserAll(currentChooser);

				synchronized (event) {
					// wait until object notifies - which relinquishes the lock on the object too
					try {
						logger.info(
								"I am waiting for some thread to notify that it wants to choose positive or negative");
						event.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				logger.info("I received that is going to be " + event.getPositiveOrNegative().toString());
				this.sendPositiveOrNegativeAll(event.getPositiveOrNegative());

				logger.debug("Sleeping for 100 seconds");
				Thread.sleep(100000);
			}

//			logger.info("All players connected, passing control to game");
//			NetworkGame game;
//			Dealer dealer = new Dealer(Direction.NORTH);
//			game = new NetworkGame(pool, dealer.deal(new NegativeTricksRuleset()));
//			game.run();
//			logger.info("Game has ended. Exiting main thread.");
		}
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

	public void removePlayerSocket(PlayerSocket playerSocket) {
		this.playerSockets.remove(playerSocket);
	}

	public void notifyPlayCard(Card playedCard, Direction direction) {
		this.networkGame.notifyPlayCard(playedCard, direction);
	}

	public void notifyChoosePositiveOrNegative(PositiveOrNegative positiveOrNegative, Direction direction) {
		synchronized (event) {
			if (this.currentChooser == direction) {
				this.event.notifyAllWithPositiveOrNegative(positiveOrNegative);
			} else {
				throw new SelectedPositiveOrNegativeInAnotherPlayersTurnException();
			}
		}
	}

	public void sendDealAll(Deal deal) {
		logger.info("Sending everyone the current deal");
		for (PlayerSocket playerSocket : playerSockets) {
			playerSocket.sendDeal(deal);
		}
		logger.info("Finished sending deals.");
	}

	private void sendMessageAll(String message) {
		logger.info("Sending everyone the following message: --" + message + "--");
		for (PlayerSocket playerSocket : playerSockets) {
			playerSocket.sendMessage(message);
		}
		logger.info("Finished sending messages.");
	}

	private void sendChooserAll(Direction chooser) {
		logger.info("Sending everyone the chooser: --" + chooser + "--");
		for (PlayerSocket playerSocket : playerSockets) {
			playerSocket.sendChooser(chooser);
		}
		logger.info("Finished sending messages.");
	}
	
	private void sendPositiveOrNegativeAll(PositiveOrNegative positiveOrNegative) {
		String message = positiveOrNegative.toString().toUpperCase();
		logger.info("Sending everyone : --" + message + "--");
		for (PlayerSocket playerSocket : playerSockets) {
			playerSocket.sendPositiveOrNegative(message);
		}
		logger.info("Finished sending messages.");
	}

}
