package br.com.sbk.sbking.networking;

import java.io.IOException;
import java.net.Socket;

import org.apache.log4j.Logger;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;

public class PlayerSocket implements Runnable {
	private Direction direction;
	private Socket socket;
	private Serializator serializator;
	private GameServer gameServer;
	final static Logger logger = Logger.getLogger(PlayerSocket.class);

	public PlayerSocket(Serializator serializator, Socket socket, Direction direction, GameServer gameServer) {
		this.serializator = serializator;
		this.socket = socket;
		this.direction = direction;
		this.gameServer = gameServer;
	}

	@Override
	public void run() {
		logger.info("Connected: " + socket);
		try {
			setup();
			while(true) {
				processCommand();
			}
		} catch (Exception e) {
			logger.debug("Error:" + socket, e);
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				logger.debug(e);
			}
			logger.info("Closed: " + socket + ". Removing (myself) from playerSocketList");
			gameServer.removePlayerSocket(this);
		}
	}

	private void setup() throws IOException, InterruptedException {
		logger.info("Sleeping for 500ms waiting for client to setup itself");
		Thread.sleep(500);
		this.sendDirection(direction);
	}

	private void processCommand() {
		Object readObject = this.serializator.tryToDeserialize();
		if (readObject instanceof String) {
			String string = (String) readObject;
			logger.info(this.direction + "sent this message: --" + string + "--");
		}
		if (readObject instanceof Card) {
			Card playedCard = (Card) readObject;
			logger.info(this.direction + " is trying to play the " + playedCard);
			gameServer.notifyPlayCard(playedCard, this.direction);
		}
	}

	public void sendMessage(String string) {
		String control = "MESSAGE";
		logger.info("Sending " + control + " control to " + this.direction);
		this.serializator.tryToSerialize(control);
		logger.info("Sending message --" + string + "-- to " + this.direction);
		this.serializator.tryToSerialize(string);
	}

	public void sendDeal(Deal deal) {
		String control = "DEAL";
		logger.info("Sending " + control + " control to " + this.direction);
		this.serializator.tryToSerialize(control);
		logger.info("Sending current deal to " + this.direction);
		this.serializator.tryToSerialize(deal);
	}

	public void sendDirection(Direction direction) {
		String control = "DIRECTION";
		logger.info("Sending " + control + " control to " + this.direction);
		this.serializator.tryToSerialize(control);
		logger.info("Sending its direction to " + this.direction);
		this.serializator.tryToSerialize(direction);
	}
	
	public void sendChooser(Direction direction) {
		String control = "CHOOSER";
		logger.info("Sending " + control + " control to " + this.direction);
		this.serializator.tryToSerialize(control);
		logger.info("Sending chooser direction to " + this.direction);
		this.serializator.tryToSerialize(direction);
	}

}