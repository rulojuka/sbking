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
	private NetworkCardPlayer networkCardPlayer;

	private Direction direction;
	private boolean allPlayersConnected;

	private Direction positiveOrNegativeChooser;
	private Direction GameModeOrStrainChooser;
	private PositiveOrNegative positiveOrNegative;
	private Deal currentDeal;

	private boolean dealFinished;

	private boolean gameFinished = false;

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
		this.networkCardPlayer = new NetworkCardPlayer(this.serializator);
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
		final String INITIALIZEDEAL = "INITIALIZEDEAL";
		final String FINISHDEAL = "FINISHDEAL";
		final String DIRECTION = "DIRECTION";
		final String WAIT = "WAIT";
		final String CONTINUE = "CONTINUE";
		final String ALLCONNECTED = "ALLCONNECTED";
		final String CHOOSERPOSITIVENEGATIVE = "CHOOSERPOSITIVENEGATIVE";
		final String CHOOSERGAMEMODEORSTRAIN = "CHOOSERGAMEMODEORSTRAIN";
		final String POSITIVEORNEGATIVE = "POSITIVEORNEGATIVE";
		final String GAMEMODEORSTRAIN = "GAMEMODEORSTRAIN";
		final String FINISHGAME = "FINISHGAME";

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
			this.setCurrentDeal(deal);
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
		} else if (GAMEMODEORSTRAIN.equals(controlMessage)) {
			String gameModeOrStrain = this.serializator.tryToDeserializeString();
			logger.info("Received GameModeOrStrain: " + gameModeOrStrain);
		} else if (INITIALIZEDEAL.equals(controlMessage)) {
			this.initializeDeal();
		} else if (FINISHDEAL.equals(controlMessage)) {
			this.finishDeal();
		} else if (FINISHGAME.equals(controlMessage)) {
			this.finishGame();
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

	private void finishDeal() {
		this.initializeEverythingToNextDeal();
		this.dealFinished = true;
	}

	private void initializeDeal() {
		this.dealFinished = false;
	}

	private void initializeEverythingToNextDeal() {
		this.unsetPositiveOrNegativeChooser();
		this.unsetPositiveOrNegative();
		this.unsetGameModeOrStrainChooser();
		this.unsetCurrentDeal();
	}

	public NetworkCardPlayer getNetworkCardPlayer() {
		return this.networkCardPlayer;
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

	private void unsetPositiveOrNegativeChooser() {
		this.positiveOrNegativeChooser = null;
	}

	public Direction getPositiveOrNegativeChooser() {
		return this.positiveOrNegativeChooser;
	}

	private void setGameModeOrStrainChooser(Direction direction) {
		this.GameModeOrStrainChooser = direction;
	}

	private void unsetGameModeOrStrainChooser() {
		this.GameModeOrStrainChooser = null;
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

	private void unsetPositiveOrNegative() {
		this.positiveOrNegative = null;
	}

	public void sendGameModeOrStrain(String gameModeOrStrain) {
		logger.debug("Sending Game Mode or Strain to server");
		this.serializator.tryToSerialize(gameModeOrStrain);
	}

	public boolean newDealAvailable() {
		return currentDeal != null;
	}

	private void setCurrentDeal(Deal deal) {
		this.currentDeal = deal;
	}

	private void unsetCurrentDeal() {
		this.currentDeal = null;
	}

	public Deal getDeal() {
		Deal deal = this.currentDeal;
		this.currentDeal = null;
		return deal;
	}

	public boolean isDealFinished() {
		return this.dealFinished;
	}

	public boolean isGameFinished() {
		return this.gameFinished;
	}

	private void finishGame() {
		this.gameFinished = true;
	}

}
