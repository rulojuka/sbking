package br.com.sbk.sbking.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;

public class SBKingClient implements Runnable {

	private Socket socket;

	private Serializator serializator;

	private NetworkGameMode networkGameMode;
	private Direction direction;
	private boolean allPlayersConnected;

	private Direction positiveOrNegativeChooser;
	private Direction GameModeOrStrainChooser;
	private PositiveOrNegative positiveOrNegative;

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
		logger.info("Entering on the infinite loop to process commands.");
		while (true) {
			processCommand();
		}
	}

	private void initializeDirection(Direction direction) {
		this.direction = direction;
	}

	private void processCommand() {
		logger.info("Waiting for a command");
		Object readObject = this.serializator.tryToDeserialize();
		String controlMessage = (String) readObject;
		logger.info("Read control: --" + controlMessage + "--");

		final String MESSAGE = "MESSAGE";
		final String DEAL = "DEAL";
		final String DIRECTION = "DIRECTION";
		final String WAIT = "WAIT";
		final String CONTINUE = "CONTINUE";
		final String ALLCONNECTED = "ALLCONNECTED";
		final String CHOOSERPOSITIVENEGATIVE = "CHOOSERPOSITIVENEGATIVE";
		final String CHOOSERGAMEMODEORSTRAIN = "CHOOSERGAMEMODEORSTRAIN";
		final String POSITIVEORNEGATIVE = "POSITIVEORNEGATIVE";

		if (MESSAGE.equals(controlMessage)) {
			String string = this.serializator.tryToDeserializeString();
			logger.info("I received a message: --" + string + "--");
			if (ALLCONNECTED.equals(string)) {
				setAllPlayersConnected();
			}
		} else if (DEAL.equals(controlMessage)) {
			Deal deal = this.serializator.tryToDeserializeDeal();
			logger.info("I received a deal that contains this trick: " + deal.getCurrentTrick()
					+ " and will paint it on screen");
			paintBoardWithDeal(deal);
		} else if (DIRECTION.equals(controlMessage)) {
			Direction direction = this.serializator.tryToDeserializeDirection();
			logger.info("I received my direction: " + direction);
			this.initializeDirection(direction);
		} else if (WAIT.equals(controlMessage)) {
			logger.info("Waiting for a CONTINUE message");
			do {
				this.serializator.tryToDeserialize();
				readObject = this.serializator.tryToDeserialize();
				controlMessage = (String) readObject;
			} while (!CONTINUE.equals(controlMessage));
			logger.info("Received a CONTINUE message");

		} else if (CHOOSERPOSITIVENEGATIVE.equals(controlMessage)) {
			Direction direction = this.serializator.tryToDeserializeDirection();
			logger.info("Received PositiveOrNegative choosers direction: " + direction);

			setPositiveOrNegativeChooser(direction);
		} else if (POSITIVEORNEGATIVE.equals(controlMessage)) {
			String positiveOrNegative = this.serializator.tryToDeserializeString();
			if ("POSITIVE".equals(positiveOrNegative)) {
				this.selectedPositive();
			} else if ("NEGATIVE".equals(positiveOrNegative)) {
				this.selectedNegative();
			} else {
				logger.info("Could not understand POSITIVE or NEGATIVE.");
			}
		} else if (CHOOSERGAMEMODEORSTRAIN.equals(controlMessage)) {
			Direction direction = this.serializator.tryToDeserializeDirection();
			logger.info("Received GameModeOrStrain choosers direction: " + direction);
			setGameModeOrStrainChooser(direction);
		} else {
			logger.info("Could not understand control.");
		}

		// Run these when received an order to start game from server.
		// NetworkCardPlayer networkCardPlayer = new
		// NetworkCardPlayer(this.serializator);
		// this.networkGameMode = new NetworkGameMode(networkCardPlayer,
		// this.direction);

		// FIXME
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendPositive() {
		logger.debug("Sending positive to server");
		String positive = "POSITIVE";
		this.serializator.tryToSerialize(positive);
	}

	public void sendNegative() {
		logger.debug("Sending negative to server");
		String negative = "NEGATIVE";
		this.serializator.tryToSerialize(negative);
	}

	private void setAllPlayersConnected() {
		allPlayersConnected = true;
	}

	private void paintBoardWithDeal(Deal deal) {
		networkGameMode.paintBoardElements(deal);
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

	private void setPositiveOrNegativeChooser(Direction direction) {
		this.positiveOrNegativeChooser = direction;
	}

	public boolean isPositiveOrNegativeChooserSet() {
		return this.positiveOrNegativeChooser != null;
	}

	public Direction getPositiveOrNegativeChooser() {
		return this.positiveOrNegativeChooser;
	}

	private void setGameModeOrStrainChooser(Direction direction) {
		this.GameModeOrStrainChooser = direction;
	}

	public boolean isGameModeOrStrainChooserSet() {
		return this.GameModeOrStrainChooser != null;
	}

	public Direction getGameModeOrStrainChooser() {
		return this.GameModeOrStrainChooser;
	}

	public void selectedPositive() {
		this.positiveOrNegative = new PositiveOrNegative();
		this.positiveOrNegative.setPositive();
	}

	public void selectedNegative() {
		this.positiveOrNegative = new PositiveOrNegative();
		this.positiveOrNegative.setNegative();
	}

	public boolean isPositiveOrNegativeSelected() {
		if (this.positiveOrNegative == null)
			return false;
		return this.positiveOrNegative.isSelected();
	}

	public boolean isPositive() {
		return this.positiveOrNegative.isPositive();
	}

	public boolean isNegative() {
		return this.positiveOrNegative.isNegative();
	}

	public void sendGameModeOrStrain(String gameModeOrStrain) {
		logger.debug("Sending Game Mode or Strain to server");
		this.serializator.tryToSerialize(gameModeOrStrain);
	}

}
