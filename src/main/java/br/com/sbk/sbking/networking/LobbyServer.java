package br.com.sbk.sbking.networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Dealer;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Hand;
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
			System.out.println("All players connected, passing control to game");
			game.play();

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

	public void play() {
		if (!deal.isFinished()) {
			for (PlayerSocket playerSocket : playerSockets) {
				playerSocket.sendDeal(deal);
			}
			waitForPlay(deal.getCurrentPlayer());
		}
	}

	public void connectPlayer(Socket socket, Direction direction) {
		PlayerSocket current = new PlayerSocket(socket, direction);
		playerSockets.add(current);
		pool.execute(current);
	}

	private void sendAll(String message) {
		for (PlayerSocket playerSocket : playerSockets) {
			playerSocket.sendMessage(message);
		}
	}

	private void waitForPlay(Direction currentPlayer) {
		for (PlayerSocket playerSocket : playerSockets) {
			playerSocket.sendMessage("Your hand:");
			playerSocket.sendHand(this.deal.getHandOf(playerSocket.direction));
		}
		sendAll("Waiting for " + currentPlayer + " to play a card.");

	}

	public synchronized void playCard(Card card, Direction direction) {
		System.out.println(direction + " is trying to play the " + card);
		if (deal.getCurrentPlayer() == direction) {
			this.deal.playCard(card);
			this.played(card, direction);
			this.play();
		} else {
			throw new PlayedCardInAnotherPlayersTurnException();
		}

	}

	private void played(Card card, Direction direction) {
		sendAll(direction + " played the " + card.completeName());
	}

	class PlayerSocket implements Runnable {
		private Socket socket;
		private Scanner input;
		private PrintWriter output;
		private Direction direction;

		PlayerSocket(Socket socket, Direction direction) {
			this.socket = socket;
			this.direction = direction;
		}

		@Override
		public void run() {
			System.out.println("Connected: " + socket);
			try {
				setup();
				processCommands();
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

		private void setup() throws IOException {
			input = new Scanner(socket.getInputStream());
			output = new PrintWriter(socket.getOutputStream(), true);
			this.sendMessage("You are " + direction);

		}

		private void processCommands() {
			while (input.hasNextLine()) {
				String nextLine = input.nextLine();
				System.out.println("Recebi " + nextLine + ". Tentando transformar em uma carta");
				try {
					Card card = readCardFromString(nextLine);
					playCard(card, this.direction);
				} catch (Exception e) {
					this.sendMessage(e.getMessage());
					e.printStackTrace();
				}
			}
		}

		private Card readCardFromString(String receivedLine) {
			return new Card(receivedLine);
		}

		private void sendMessage(String message) {
			System.out.println("Sending --" + message + "-- to " + this.socket);
			this.output.println(message);
			this.output.flush();
		}

		public void sendDeal(Deal deal) {
			this.sendMessage(deal.toString());
		}

		public void sendHand(Hand hand) {
			this.sendMessage(hand.toString());
		}

	}

}
