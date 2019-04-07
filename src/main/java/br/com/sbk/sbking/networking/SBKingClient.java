package br.com.sbk.sbking.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;

public class SBKingClient implements Runnable{

	private Socket socket;

	private Serializator serializator;

	private NetworkGameMode networkGameMode;
	private Direction direction;
	private boolean allPlayersConnected;

	final static Logger logger = Logger.getLogger(SBKingClient.class);

	public SBKingClient() {
		try {
			socket = new Socket("localhost", 60000);
		} catch (UnknownHostException e) {
			logger.debug(e);
		} catch (IOException e) {
			logger.debug(e);
		}
		setupSerializator();
	}

	private void setupSerializator() {
		this.serializator = null;
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			this.serializator = new Serializator(objectInputStream, objectOutputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Finished serializator setup.");
	}

	@Override
	public void run() {
		initializeDirection();
		NetworkCardPlayer networkCardPlayer = new NetworkCardPlayer(this.serializator);
		//this.networkGameMode = new NetworkGameMode(networkCardPlayer, this.direction);
		logger.info("Entering on the infinite loop to process commands.");
		while (true) {
			processCommand();
		}
	}

	private void initializeDirection() {
		processCommand();
	}

	private void processCommand() {
		logger.info("Waiting for a command");
		Object readObject = this.serializator.tryToDeserialize();
		String controlMessage = (String) readObject;
		logger.info("Read control: --" + controlMessage + "--");

		String MESSAGE = "MESSAGE";
		String DEAL = "DEAL";
		String DIRECTION = "DIRECTION";
		String WAIT = "WAIT";
		String CONTINUE = "CONTINUE";
		String ALLCONNECTED = "ALLCONNECTED";

		if (MESSAGE.equals(controlMessage)) {
			String string = this.serializator.tryToDeserializeString();
			logger.info("I received a message: --" + string + "--");
			if (ALLCONNECTED.equals(string)) {
				allPlayersConnected = true;
			}
		} else if (DEAL.equals(controlMessage)) {
			Deal deal = this.serializator.tryToDeserializeDeal();
			logger.info("I received a deal that contains this trick: " + deal.getCurrentTrick()
					+ " and will paint it on screen");
			networkGameMode.paintBoardElements(deal);
		} else if (DIRECTION.equals(controlMessage)) {
			Direction direction = this.serializator.tryToDeserializeDirection();
			logger.info("I received my direction: " + direction);
			this.direction = direction;
		} else if (WAIT.equals(controlMessage)) {
			logger.info("Waiting for a CONTINUE message");
			do {
				this.serializator.tryToDeserialize();
				readObject = this.serializator.tryToDeserialize();
				controlMessage = (String) readObject;
			} while (!CONTINUE.equals(controlMessage));
			logger.info("Received a CONTINUE message");

		} else {
			logger.info("Could not understand control.");
		}

		// FIXME
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isEveryoneConnected() {
		return allPlayersConnected;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public boolean isDirectionSet() {
		return this.direction != null;
	}

}
