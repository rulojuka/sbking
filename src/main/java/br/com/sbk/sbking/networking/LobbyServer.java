package br.com.sbk.sbking.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Dealer;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.exceptions.PlayedCardInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.rulesets.concrete.NegativeTricksRuleset;

public class LobbyServer {
	public static void main(String[] args) throws Exception {
		Game game;
		Dealer dealer = new Dealer(Direction.NORTH);
		ExecutorService pool = Executors.newFixedThreadPool(4);
		game = new Game(pool, dealer.deal(new NegativeTricksRuleset()));

		try (ServerSocket listener = new ServerSocket(60000)) {
			System.out.println("Lobby Server is Running...");
			for (Direction direction : Direction.values()) {
				game.connectPlayer(listener.accept(), direction);
			}

			// FIXME wait for last client to setup itself
			Thread.sleep(1000);

			System.out.println("All players connected, passing control to game");
			game.sendCurrentDealAll();

		}
	}
}

class Game {

	private Deal deal;
	private List<PlayerSocket> playerSockets = new ArrayList<PlayerSocket>();
	private ExecutorService pool;

	public Game(ExecutorService pool, Deal deal) {
		this.deal = deal;
		this.pool = pool;
	}

	public void sendCurrentDealAll() {
		if (!this.deal.isFinished()) {
			System.out.println("Sending everyone the current deal");
			sendDealAll(this.deal);
		}
		System.out.println("Finished sending deals.");
	}

	public void connectPlayer(Socket socket, Direction direction) {
		PlayerSocket current = new PlayerSocket(socket, direction);
		playerSockets.add(current);
		pool.execute(current);
	}

	private void sendDealAll(Deal deal) {
		for (PlayerSocket playerSocket : playerSockets) {
			playerSocket.sendDeal(this.deal);
		}
	}

	public synchronized void playCard(Card card, Direction direction) {
		try {
			if (this.deal.getCurrentPlayer() == direction) {
				this.deal.playCard(card);
				System.out.println(direction + " sucessfully played the " + card);
				System.out.println("Current trick of this deal is:");
				System.out.println(this.deal.getCurrentTrick());
				this.sendCurrentDealAll();
			} else {
				throw new PlayedCardInAnotherPlayersTurnException();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	class PlayerSocket implements Runnable {
		private Direction direction;
		private Socket socket;

		private Serializator serializator;

		PlayerSocket(Socket socket, Direction direction) {
			this.socket = socket;
			this.direction = direction;
		}

		@Override
		public void run() {
			System.out.println("Connected: " + socket);
			try {
				setup();
				processCommand();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error:" + socket);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
				}
				System.out.println("Closed: " + socket);
			}
		}

		private void setup() throws IOException, InterruptedException {
			this.serializator = null;
			try {
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
				this.serializator = new Serializator(objectInputStream, objectOutputStream);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// FIXME wait for client to setup itself
			Thread.sleep(500);

			this.sendDirection(direction);

		}

		private void processCommand() {
			Object readObject = this.serializator.tryToDeserialize();
			if (readObject instanceof String) {
				String string = (String) readObject;
				System.out.println(this.direction + "sent this message: --" + string + "--");
			}
			if (readObject instanceof Card) {
				Card playedCard = (Card) readObject;
				System.out.println(this.direction + " is trying to play the " + playedCard);
				playCard(playedCard, this.direction);
			}
		}
		
		public void sendMessage(String string) {
			System.out.println("Sending message to " + this.direction);
			String control = "MESSAGE";
			this.serializator.tryToSerialize(control);
			this.serializator.tryToSerialize(string);
		}

		public void sendDeal(Deal deal) {
			System.out.println("Sending current deal to " + this.direction);
			String control = "DEAL";
			this.serializator.tryToSerialize(control);
			this.serializator.tryToSerialize(deal);
		}

		public void sendDirection(Direction direction) {
			System.out.println("Sending its direction to " + this.direction);
			String control = "DIRECTION";
			this.serializator.tryToSerialize(control);
			this.serializator.tryToSerialize(direction);
		}

		public void sendWait(Direction direction) {
			System.out.println("Sending WAIT to " + this.direction);
			String control = "WAIT";
			this.serializator.tryToSerialize(control);
		}

		public void sendContinue(Direction direction) {
			System.out.println("Sending CONTINUE to " + this.direction);
			String control = "CONTINUE";
			this.serializator.tryToSerialize(control);
		}

	}

}
