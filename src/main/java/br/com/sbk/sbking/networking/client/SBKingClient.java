package br.com.sbk.sbking.networking.client;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.net.Socket;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.gui.listeners.ClientActionListener;
import br.com.sbk.sbking.gui.models.KingGameScoreboard;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.core.properties.FileProperties;
import br.com.sbk.sbking.networking.core.properties.NetworkingProperties;
import br.com.sbk.sbking.networking.core.properties.SystemProperties;
import br.com.sbk.sbking.networking.core.serialization.Serializator;
import br.com.sbk.sbking.networking.core.serialization.SerializatorFactory;
import br.com.sbk.sbking.networking.messages.MessageConstants;

public class SBKingClient implements Runnable {

    private static final String NETWORKING_CONFIGURATION_FILENAME = "networkConfiguration.cfg";
    private static final int COULD_NOT_GET_NETWORK_INFORMATION_FROM_PROPERTIES_ERROR = 1;
    private static final int COULD_NOT_CREATE_SOCKET_ERROR = 2;
    private static final int COULD_NOT_CREATE_SERIALIZATOR_ERROR = 3;
    private Serializator serializator;

    private Direction direction;

    private Direction positiveOrNegativeChooser;
    private Direction gameModeOrStrainChooser;
    private PositiveOrNegative positiveOrNegative;

    private Board currentBoard;
    private boolean boardHasChanged = true;

    private Deal currentDeal;
    private boolean dealHasChanged = true;

    private boolean dealFinished;
    private Boolean rulesetValid = null;

    private boolean gameFinished = false;

    private KingGameScoreboard currentGameScoreboard = new KingGameScoreboard();

    private ClientActionListener playCardActionListener;

    private boolean spectator;

    private String nickname;

    private String hostname;

    public SBKingClient(String nickname, String hostname) {
        this.hostname = hostname;
        this.nickname = nickname;
        startNetworkClient();
    }

    private void startNetworkClient() {
        Socket socket = initializeSocketOrExit(this.hostname);
        LOGGER.info("Socket initialized.");
        this.serializator = initializeSerializatorOrExit(socket);
        LOGGER.info("Serializator initialized.");
        ClientToServerMessageSender networkCardPlayer = new ClientToServerMessageSender(this.serializator);
        this.playCardActionListener = new ClientActionListener(networkCardPlayer);
        this.sendNickname(this.nickname);
    }

    private Socket initializeSocketOrExit(String hostname) {
        String host = null;
        int port = 0;
        try {
            FileProperties configFile = new FileProperties(NETWORKING_CONFIGURATION_FILENAME);
            NetworkingProperties networkingProperties = new NetworkingProperties(configFile, new SystemProperties());

            LOGGER.info("Given hostname is: " + hostname);
            if (hostname == null || hostname.isEmpty()) {
                host = networkingProperties.getHost();
            } else {
                host = hostname;
            }
            port = networkingProperties.getPort();
        } catch (Exception e) {
            LOGGER.fatal("Could not get network information from properties.");
            LOGGER.debug(e);
            System.exit(COULD_NOT_GET_NETWORK_INFORMATION_FROM_PROPERTIES_ERROR);
        }

        try {
            LOGGER.info("Trying to create socket to: " + host + ":" + port);
            return new Socket(host, port);
        } catch (Exception e) {
            LOGGER.fatal("Could not create socket.");
            LOGGER.debug(e);
            System.exit(COULD_NOT_CREATE_SOCKET_ERROR);
            return null;
        }
    }

    private Serializator initializeSerializatorOrExit(Socket socket) {
        SerializatorFactory serializatorFactory = new SerializatorFactory();
        try {
            return serializatorFactory.getSerializator(socket);
        } catch (Exception e) {
            LOGGER.fatal("Could not create serializator.");
            LOGGER.fatal(e);
            System.exit(COULD_NOT_CREATE_SERIALIZATOR_ERROR);
            return null;
        }
    }

    @Override
    public void run() {
        LOGGER.info("Entering on the infinite loop to process commands.");
        while (true) {
            processCommand();
        }
    }

    private void initializeDirection(Direction direction) {
        this.direction = direction;
    }

    private void processCommand() {
        LOGGER.info("Waiting for a command");
        Object readObject = this.serializator.tryToDeserialize(Object.class);
        String controlMessage;
        try {
            controlMessage = (String) readObject;
        } catch (Exception e) {
            LOGGER.error("*******************************");
            LOGGER.error("Can't cast readObject to String");
            LOGGER.error("*******************************");

            LOGGER.error("");

            // restart network client
            startNetworkClient();

            return;
        }

        LOGGER.info("Read control: --" + controlMessage + "--");

        if (MessageConstants.MESSAGE.equals(controlMessage)) {
            String string = this.serializator.tryToDeserialize(String.class);
            LOGGER.info("I received a message: --" + string + "--");
        } else if (MessageConstants.BOARD.equals(controlMessage)) {
            Board board = this.serializator.tryToDeserialize(Board.class);
            LOGGER.info("I received a board.");
            this.setCurrentBoard(board);
        } else if (MessageConstants.DEAL.equals(controlMessage)) {
            Deal deal = this.serializator.tryToDeserialize(Deal.class);
            LOGGER.info("I received a deal that contains this trick: " + deal.getCurrentTrick()
                    + " and will paint it on screen");
            this.setCurrentDeal(deal);
        } else if (MessageConstants.DIRECTION.equals(controlMessage)) {
            Direction direction = this.serializator.tryToDeserialize(Direction.class);
            LOGGER.info("I received my direction: " + direction);
            this.initializeDirection(direction);
        } else if (MessageConstants.WAIT.equals(controlMessage)) {
            LOGGER.info("Waiting for a CONTINUE message");
            do {
                controlMessage = this.serializator.tryToDeserialize(String.class);
            } while (!MessageConstants.CONTINUE.equals(controlMessage));
            LOGGER.info("Received a CONTINUE message");

        } else if (MessageConstants.CHOOSERPOSITIVENEGATIVE.equals(controlMessage)) {
            Direction direction = this.serializator.tryToDeserialize(Direction.class);
            LOGGER.info("Received PositiveOrNegative choosers direction: " + direction);

            setPositiveOrNegativeChooser(direction);
        } else if (MessageConstants.POSITIVEORNEGATIVE.equals(controlMessage)) {
            String positiveOrNegative = this.serializator.tryToDeserialize(String.class);
            if ("POSITIVE".equals(positiveOrNegative)) {
                this.selectedPositive();
            } else if ("NEGATIVE".equals(positiveOrNegative)) {
                this.selectedNegative();
            } else {
                LOGGER.info("Could not understand POSITIVE or NEGATIVE.");
            }
        } else if (MessageConstants.CHOOSERGAMEMODEORSTRAIN.equals(controlMessage)) {
            Direction direction = this.serializator.tryToDeserialize(Direction.class);
            LOGGER.info("Received GameModeOrStrain choosers direction: " + direction);
            setGameModeOrStrainChooser(direction);
        } else if (MessageConstants.GAMEMODEORSTRAIN.equals(controlMessage)) {
            String gameModeOrStrain = this.serializator.tryToDeserialize(String.class);
            LOGGER.info("Received GameModeOrStrain: " + gameModeOrStrain);
        } else if (MessageConstants.INITIALIZEDEAL.equals(controlMessage)) {
            this.initializeDeal();
        } else if (MessageConstants.FINISHDEAL.equals(controlMessage)) {
            this.finishDeal();
        } else if (MessageConstants.FINISHGAME.equals(controlMessage)) {
            this.finishGame();
        } else if (MessageConstants.INVALIDRULESET.equals(controlMessage)) {
            this.setRulesetValid(false);
        } else if (MessageConstants.VALIDRULESET.equals(controlMessage)) {
            this.setRulesetValid(true);
        } else if (MessageConstants.GAMESCOREBOARD.equals(controlMessage)) {
            this.currentGameScoreboard = this.serializator.tryToDeserialize(KingGameScoreboard.class);
            LOGGER.info("Received GameScoreboard." + this.currentGameScoreboard.toString());
        } else if (MessageConstants.ISSPECTATOR.equals(controlMessage)) {
            this.spectator = true;
            LOGGER.info("Received ISSPECTATOR.");
        } else if (MessageConstants.ISNOTSPECTATOR.equals(controlMessage)) {
            this.spectator = false;
            LOGGER.info("Received ISNOTSPECTATOR.");
        } else {
            LOGGER.error("Could not understand control.");
        }

        // FIXME remove this if possible
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setCurrentBoard(Board board) {
        this.currentBoard = board;
        this.boardHasChanged = true;
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
        LOGGER.info("Sending positive to server");
        String positive = "POSITIVE";
        this.serializator.tryToSerialize(positive);
    }

    public void sendNegative() {
        LOGGER.info("Sending negative to server");
        String negative = "NEGATIVE";
        this.serializator.tryToSerialize(negative);
    }

    public Direction getDirection() {
        return this.direction;
    }

    public boolean isDirectionOrSpectatorSet() {
        return this.direction != null || this.spectator;
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
        this.gameModeOrStrainChooser = direction;
    }

    private void unsetGameModeOrStrainChooser() {
        this.gameModeOrStrainChooser = null;
    }

    public boolean isGameModeOrStrainChooserSet() {
        return this.gameModeOrStrainChooser != null;
    }

    public Direction getGameModeOrStrainChooser() {
        return this.gameModeOrStrainChooser;
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
        if (this.positiveOrNegative == null) {
            return false;
        }
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
        LOGGER.info("Sending Game Mode or Strain to server");
        this.serializator.tryToSerialize(gameModeOrStrain);
    }

    public boolean newDealAvailable() {
        return currentDeal != null;
    }

    private void setCurrentDeal(Deal deal) {
        this.currentDeal = deal;
        this.dealHasChanged = true;
    }

    private void unsetCurrentDeal() {
        this.currentDeal = null;
    }

    public Deal getDeal() {
        this.dealHasChanged = false;
        return this.currentDeal;
    }

    public boolean getDealHasChanged() {
        return this.dealHasChanged;
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

    public KingGameScoreboard getCurrentGameScoreboard() {
        return this.currentGameScoreboard;
    }

    public boolean isRulesetValidSet() {
        return this.rulesetValid != null;
    }

    public boolean isRulesetValid() {
        if (this.rulesetValid == null) {
            return false;
        }
        return this.rulesetValid;
    }

    public Board getCurrentBoard() {
        this.boardHasChanged = false;
        return this.currentBoard;
    }

    public boolean getBoardHasChanged() {
        return this.boardHasChanged;
    }

    public ClientActionListener getPlayCardActionListener() {
        return this.playCardActionListener;
    }

    public boolean isSpectator() {
        return this.spectator;
    }

    public String getNickname() {
        return nickname;
    }

    public void sendNickname(String nickname) {
        LOGGER.info("Sending nickname to server");
        this.serializator.tryToSerialize("NICKNAME" + nickname);
    }

}
