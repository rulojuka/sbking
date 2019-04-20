package br.com.sbk.sbking.networking.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.listeners.PlayCardActionListener;
import br.com.sbk.sbking.gui.models.GameScoreboard;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.core.properties.FileProperties;
import br.com.sbk.sbking.networking.core.properties.NetworkingProperties;
import br.com.sbk.sbking.networking.core.properties.SystemProperties;
import br.com.sbk.sbking.networking.core.serialization.Serializator;

public class SBKingClient implements Runnable {

	private static final String NETWORKING_CONFIGURATION_FILENAME = "networkConfiguration.cfg";
	private static final int COULD_NOT_GET_NETWORK_INFORMATION_FROM_PROPERTIES_ERROR = 1;
	private static final int COULD_NOT_CREATE_SOCKET_ERROR = 2;
	private static final int COULD_NOT_CREATE_SERIALIZATOR_ERROR = 3;
	private final Serializator serializator;

	private Direction direction;
	private boolean allPlayersConnected;

	private Direction positiveOrNegativeChooser;
	private Direction GameModeOrStrainChooser;
	private PositiveOrNegative positiveOrNegative;
	private Deal currentDeal;

	private boolean dealFinished;
	private Boolean rulesetValid = null;

	private boolean gameFinished = false;

	private GameScoreboard currentGameScoreboard = new GameScoreboard();

	private Board currentBoard;

	private PlayCardActionListener playCardActionListener;

	final static Logger logger = Logger.getLogger(SBKingClient.class);

	public SBKingClient() {
		Socket socket = initializeSocketOrExit();
		logger.info("Socket initialized.");
		this.serializator = initializeSerializatorOrExit(socket);
		logger.info("Serializator initialized.");
		NetworkCardPlayer networkCardPlayer = new NetworkCardPlayer(this.serializator);
		this.playCardActionListener = new PlayCardActionListener(networkCardPlayer);
	}

	private Socket initializeSocketOrExit() {
		String host = null;
		int port = 0;
		try {
			FileProperties configFile = new FileProperties(NETWORKING_CONFIGURATION_FILENAME);
			NetworkingProperties networkingProperties = new NetworkingProperties(configFile, new SystemProperties());
			host = networkingProperties.getHost();
			port = networkingProperties.getPort();
		} catch (Exception e) {
			logger.fatal("Could not get network information from properties.");
			logger.debug(e);
			System.exit(COULD_NOT_GET_NETWORK_INFORMATION_FROM_PROPERTIES_ERROR);
		}

		try {
			logger.info("Trying to create socket to: " + host + ":" + port);
			return new Socket(host, port);
		} catch (Exception e) {
			logger.fatal("Could not create socket.");
			logger.debug(e);
			System.exit(COULD_NOT_CREATE_SOCKET_ERROR);
		}

		// Unreachable code
		return null;
	}

	private Serializator initializeSerializatorOrExit(Socket socket) {
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			return new Serializator(objectInputStream, objectOutputStream);
		} catch (Exception e) {
			logger.fatal("Could not create serializator.");
			logger.debug(e);
			System.exit(COULD_NOT_CREATE_SERIALIZATOR_ERROR);
		}

		// Unreachable code
		return null;
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
		Object readObject = this.serializator.tryToDeserialize(Object.class);
		String controlMessage = (String) readObject;
		logger.info("Read control: --" + controlMessage + "--");

		final String MESSAGE = "MESSAGE";
		final String DEAL = "DEAL";
		final String BOARD = "BOARD";
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
		final String GAMESCOREBOARD = "GAMESCOREBOARD";
		final String INVALIDRULESET = "INVALIDRULESET";
		final String VALIDRULESET = "VALIDRULESET";

		if (MESSAGE.equals(controlMessage)) {
			String string = this.serializator.tryToDeserialize(String.class);
			logger.info("I received a message: --" + string + "--");
			if (ALLCONNECTED.equals(string)) {
				setAllPlayersConnected();
			}
		} else if (BOARD.equals(controlMessage)) {
			Board board = this.serializator.tryToDeserialize(Board.class);
			logger.info("I received a board.");
			this.setCurrentBoard(board);
		} else if (DEAL.equals(controlMessage)) {
			Deal deal = this.serializator.tryToDeserialize(Deal.class);
			logger.info("I received a deal that contains this trick: " + deal.getCurrentTrick()
					+ " and will paint it on screen");
			this.setCurrentDeal(deal);
		} else if (DIRECTION.equals(controlMessage)) {
			Direction direction = this.serializator.tryToDeserialize(Direction.class);
			logger.info("I received my direction: " + direction);
			this.initializeDirection(direction);
		} else if (WAIT.equals(controlMessage)) {
			logger.info("Waiting for a CONTINUE message");
			do {
				controlMessage = this.serializator.tryToDeserialize(String.class);
			} while (!CONTINUE.equals(controlMessage));
			logger.info("Received a CONTINUE message");

		} else if (CHOOSERPOSITIVENEGATIVE.equals(controlMessage)) {
			Direction direction = this.serializator.tryToDeserialize(Direction.class);
			logger.info("Received PositiveOrNegative choosers direction: " + direction);

			setPositiveOrNegativeChooser(direction);
		} else if (POSITIVEORNEGATIVE.equals(controlMessage)) {
			String positiveOrNegative = this.serializator.tryToDeserialize(String.class);
			if ("POSITIVE".equals(positiveOrNegative)) {
				this.selectedPositive();
			} else if ("NEGATIVE".equals(positiveOrNegative)) {
				this.selectedNegative();
			} else {
				logger.info("Could not understand POSITIVE or NEGATIVE.");
			}
		} else if (CHOOSERGAMEMODEORSTRAIN.equals(controlMessage)) {
			Direction direction = this.serializator.tryToDeserialize(Direction.class);
			logger.info("Received GameModeOrStrain choosers direction: " + direction);
			setGameModeOrStrainChooser(direction);
		} else if (GAMEMODEORSTRAIN.equals(controlMessage)) {
			String gameModeOrStrain = this.serializator.tryToDeserialize(String.class);
			logger.info("Received GameModeOrStrain: " + gameModeOrStrain);
		} else if (INITIALIZEDEAL.equals(controlMessage)) {
			this.initializeDeal();
		} else if (FINISHDEAL.equals(controlMessage)) {
			this.finishDeal();
		} else if (FINISHGAME.equals(controlMessage)) {
			this.finishGame();
		} else if (INVALIDRULESET.equals(controlMessage)) {
			this.setRulesetValid(false);
		} else if (VALIDRULESET.equals(controlMessage)) {
			this.setRulesetValid(true);
		} else if (GAMESCOREBOARD.equals(controlMessage)) {
			this.currentGameScoreboard = this.serializator.tryToDeserialize(GameScoreboard.class);
			logger.info("Received GameScoreboard." + this.currentGameScoreboard.toString());
		} else {
			logger.error("Could not understand control.");
		}

		// FIXME
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setCurrentBoard(Board board) {
		this.currentBoard = board;

	}

	private void finishDeal() {
		this.initializeEverythingToNextDeal();
		this.dealFinished = true;
	}

	private void initializeDeal() {
		this.dealFinished = false;
		this.initializeEverythingToNextDeal();
	}

	private void initializeEverythingToNextDeal() {
		this.unsetPositiveOrNegativeChooser();
		this.unsetPositiveOrNegative();
		this.unsetGameModeOrStrainChooser();
		this.unsetCurrentDeal();
		this.unsetRulesetValid();
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

	private void setRulesetValid(boolean valid) {
		this.rulesetValid = valid;
	}

	private void unsetRulesetValid() {
		this.rulesetValid = null;
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

	public GameScoreboard getCurrentGameScoreboard() {
		return this.currentGameScoreboard;
	}

	public boolean isRulesetValidSet() {
		return this.rulesetValid != null;
	}

	public boolean isRulesetValid() {
		return this.rulesetValid;
	}

	public Board getCurrentBoard() {
		return this.currentBoard;
	}

	public PlayCardActionListener getPlayCardActionListener() {
		return this.playCardActionListener;
	}

}
