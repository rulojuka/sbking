package br.com.sbk.sbking.networking.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import br.com.sbk.sbking.core.Board;
import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Deal;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.core.Game;
import br.com.sbk.sbking.core.exceptions.SelectedPositiveOrNegativeInAnotherPlayersTurnException;
import br.com.sbk.sbking.core.rulesets.abstractClasses.Ruleset;
import br.com.sbk.sbking.gui.models.GameScoreboard;
import br.com.sbk.sbking.gui.models.PositiveOrNegative;
import br.com.sbk.sbking.networking.core.properties.FileProperties;
import br.com.sbk.sbking.networking.core.properties.NetworkingProperties;
import br.com.sbk.sbking.networking.core.properties.SystemProperties;
import br.com.sbk.sbking.networking.core.serialization.Serializator;
import br.com.sbk.sbking.networking.server.notifications.GameModeOrStrainNotification;
import br.com.sbk.sbking.networking.server.notifications.PositiveOrNegativeNotification;

public class GameServer {

	private static final String NETWORKING_CONFIGURATION_FILENAME = "networkConfiguration.cfg";
	private static final int COULD_NOT_GET_PORT_FROM_PROPERTIES_ERROR = 1;

	final static Logger logger = Logger.getLogger(GameServer.class);
	private List<PlayerSocket> playerSockets = new ArrayList<PlayerSocket>();
	private ExecutorService pool;
	private NetworkGame networkGame;
	private PositiveOrNegativeNotification positiveOrNegativeNotification = new PositiveOrNegativeNotification();
	private PositiveOrNegative currentPositiveOrNegative;
	private GameModeOrStrainNotification gameModeOrStrainNotification = new GameModeOrStrainNotification();
	private Ruleset currentGameModeOrStrain;

	private Game game;
	private boolean isRulesetPermitted;

	public GameServer() {
		pool = Executors.newFixedThreadPool(4);
	}

	public void run() throws Exception {
		int port = this.getPortFromNetworkingProperties();

		try (ServerSocket listener = new ServerSocket(port)) {
			logger.info("Game Server is Running...");
			logger.info("My InetAddress is: " + listener.getInetAddress());
			logger.info("Listening for connections on port: " + port);
			for (Direction direction : Direction.values()) {
				this.connectPlayer(listener.accept(), direction);
			}

			logger.info("Sleeping for 1000ms waiting for last client to setup itself");
			Thread.sleep(1000);

			this.sendMessageAll("ALLCONNECTED");

			this.game = new Game();

			while (!game.isFinished()) {
				this.game.initializeBoard();

				do {

					this.sendInitializeDealAll();
					logger.info("Sleeping for 500ms waiting for clients to initialize its deals.");
					Thread.sleep(500);

					this.sendBoardAll(this.game.getCurrentBoard());

					this.sendChooserPositiveNegativeAll(this.getCurrentPositiveOrNegativeChooser());

					synchronized (positiveOrNegativeNotification) {
						// wait until object notifies - which relinquishes the lock on the object too
						try {
							logger.info(
									"I am waiting for some thread to notify that it wants to choose positive or negative");
							positiveOrNegativeNotification.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					logger.info("I received that is going to be "
							+ positiveOrNegativeNotification.getPositiveOrNegative().toString());
					this.currentPositiveOrNegative = positiveOrNegativeNotification.getPositiveOrNegative();
					this.sendPositiveOrNegativeAll(this.currentPositiveOrNegative);

					this.sendChooserGameModeOrStrainAll(this.getCurrentGameModeOrStrainChooser());

					synchronized (gameModeOrStrainNotification) {
						// wait until object notifies - which relinquishes the lock on the object too
						try {
							logger.info(
									"I am waiting for some thread to notify that it wants to choose game Mode Or Strain");
							gameModeOrStrainNotification.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					logger.info("I received that is going to be "
							+ gameModeOrStrainNotification.getGameModeOrStrain().getShortDescription());
					this.currentGameModeOrStrain = gameModeOrStrainNotification.getGameModeOrStrain();

					isRulesetPermitted = this.game.isGameModePermitted(this.currentGameModeOrStrain,
							this.getCurrentGameModeOrStrainChooser());

					if (!isRulesetPermitted) {
						logger.info("This ruleset is not permitted. Restarting choose procedure");
						this.sendInvalidRulesetAll();
					} else {
						this.sendValidRulesetAll();
					}

				} while (!isRulesetPermitted);

				this.sendGameModeOrStrainShortDescriptionAll(this.currentGameModeOrStrain.getShortDescription());

				logger.info("Sleeping for 500ms waiting for everything come out right.");
				Thread.sleep(500);

				logger.info("Everything selected! Game commencing!");
				this.game.addRuleset(currentGameModeOrStrain);

				this.networkGame = new NetworkGame(this, this.game.getCurrentDeal());
				networkGame.run();

				this.game.finishDeal();
				this.sendGameScoreboardAll(this.game.getGameScoreboard());

				this.sendFinishDealAll();
				logger.info("Deal finished!");

			}

			this.sendFinishGameAll();

			logger.info("Game has ended. Exiting main thread.");
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
		synchronized (positiveOrNegativeNotification) {
			if (this.getCurrentPositiveOrNegativeChooser() == direction) {
				this.positiveOrNegativeNotification.notifyAllWithPositiveOrNegative(positiveOrNegative);
			} else {
				throw new SelectedPositiveOrNegativeInAnotherPlayersTurnException();
			}
		}
	}

	public void notifyChooseGameModeOrStrain(Ruleset gameModeOrStrain, Direction direction) {
		synchronized (gameModeOrStrainNotification) {
			if (this.getCurrentGameModeOrStrainChooser() == direction) {
				this.gameModeOrStrainNotification.notifyAllWithGameModeOrStrain(gameModeOrStrain);
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

	public void sendBoardAll(Board board) {
		logger.info("Sending everyone the current board");
		for (PlayerSocket playerSocket : playerSockets) {
			playerSocket.sendBoard(board);
		}
		logger.info("Finished sending boards.");
	}

	private void sendMessageAll(String message) {
		logger.info("Sending everyone the following message: --" + message + "--");
		for (PlayerSocket playerSocket : playerSockets) {
			playerSocket.sendMessage(message);
		}
		logger.info("Finished sending messages.");
	}

	private void sendChooserPositiveNegativeAll(Direction chooser) {
		logger.info("Sending everyone the chooser of Positive or Negative: --" + chooser + "--");
		for (PlayerSocket playerSocket : playerSockets) {
			playerSocket.sendChooserPositiveNegative(chooser);
		}
		logger.info("Finished sending messages.");
	}

	private void sendChooserGameModeOrStrainAll(Direction chooser) {
		logger.info("Sending everyone the chooser of GameMode or Strain: --" + chooser + "--");
		for (PlayerSocket playerSocket : playerSockets) {
			playerSocket.sendChooserGameModeOrStrain(chooser);
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

	private void sendGameModeOrStrainShortDescriptionAll(String currentGameModeOrStrain) {
		String message = currentGameModeOrStrain;
		logger.info("Sending everyone : --" + message + "--");
		for (PlayerSocket playerSocket : playerSockets) {
			playerSocket.sendGameModeOrStrain(message);
		}
		logger.info("Finished sending messages.");
	}

	private void sendInitializeDealAll() {
		logger.info("Sending everyone Initialize Deal control");
		for (PlayerSocket playerSocket : playerSockets) {
			playerSocket.sendInitializeDeal();
		}
		logger.info("Finished sending controls.");
	}

	private void sendFinishDealAll() {
		logger.info("Sending everyone Finish deal control");
		for (PlayerSocket playerSocket : playerSockets) {
			playerSocket.sendFinishDeal();
		}
		logger.info("Finished sending controls.");
	}

	private void sendGameScoreboardAll(GameScoreboard gameScoreboard) {
		logger.info("Sending everyone the Game Scoreboard");
		for (PlayerSocket playerSocket : playerSockets) {
			playerSocket.sendGameScoreboard(gameScoreboard);
		}
		logger.info("Finished sending Game Scoreboards.");
	}

	private void sendFinishGameAll() {
		logger.info("Sending everyone Finish Game control");
		for (PlayerSocket playerSocket : playerSockets) {
			playerSocket.sendFinishGame();
		}
		logger.info("Finished sending controls.");
	}

	private void sendInvalidRulesetAll() {
		logger.info("Sending everyone Invalid ruleset control");
		for (PlayerSocket playerSocket : playerSockets) {
			playerSocket.sendInvalidRuleset();
		}
		logger.info("Finished sending controls.");
	}

	private void sendValidRulesetAll() {
		logger.info("Sending everyone Valid ruleset control");
		for (PlayerSocket playerSocket : playerSockets) {
			playerSocket.sendValidRuleset();
		}
		logger.info("Finished sending controls.");
	}

	private Direction getCurrentPositiveOrNegativeChooser() {
		return this.game.getDealer().getPositiveOrNegativeChooserWhenDealer();
	}

	private Direction getCurrentGameModeOrStrainChooser() {
		return this.game.getDealer().getGameModeOrStrainChooserWhenDealer();
	}

	private int getPortFromNetworkingProperties() {
		FileProperties fileProperties = new FileProperties(NETWORKING_CONFIGURATION_FILENAME);
		int port = 0;
		try {
			NetworkingProperties networkingProperties = new NetworkingProperties(fileProperties,
					new SystemProperties());
			port = networkingProperties.getPort();
		} catch (Exception e) {
			logger.fatal("Could not get port from properties.");
			logger.debug(e);
			System.exit(COULD_NOT_GET_PORT_FROM_PROPERTIES_ERROR);
		}

		return port;
	}

}
