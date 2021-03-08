package br.com.sbk.sbking.networking.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.networking.core.properties.FileProperties;
import br.com.sbk.sbking.networking.core.properties.NetworkingProperties;
import br.com.sbk.sbking.networking.core.properties.SystemProperties;
import br.com.sbk.sbking.networking.core.serialization.Serializator;
import br.com.sbk.sbking.networking.core.serialization.SerializatorFactory;

public class LobbyServer {

	final static Logger logger = LogManager.getLogger(LobbyServer.class);

	private static final String NETWORKING_CONFIGURATION_FILENAME = "networkConfiguration.cfg";
	private static final int COULD_NOT_GET_PORT_FROM_PROPERTIES_ERROR = 1;

	private Table table;

	private boolean ownerConnected = false;

	private static final int MAXIMUM_NUMBER_OF_CONCURRENT_GAME_SERVERS = 2;
	private ExecutorService pool;

	public LobbyServer() {
		this.pool = Executors.newFixedThreadPool(MAXIMUM_NUMBER_OF_CONCURRENT_GAME_SERVERS);
	}

	public void run() {
		int port = this.getPortFromNetworkingProperties();

		try (ServerSocket listener = new ServerSocket(port)) {
			logger.info("LobbyServer is Running...");
			logger.info("My InetAddress is: " + listener.getInetAddress());
			logger.info("Listening for connections on port: " + port);

			while (!ownerConnected) {
				Socket connectingSocket = listener.accept();
				logger.info("The first client is trying to connect!");
				try {
					PlayerNetworkInformation connectedPlayerNetworkInformation = this.connectPlayer(connectingSocket);
					logger.info("Created new gameServer");
					GameServer gameServer = new PositiveKingGameServer();
					
					this.table = new Table(connectedPlayerNetworkInformation, gameServer);
					gameServer.setTable(table);
					this.ownerConnected = true;
					logger.info("Created a Table. Owner is " + connectedPlayerNetworkInformation.getSocket().getInetAddress() + "and GameServer is CagandoNoBequinhoGameServer.");
					pool.execute(gameServer);
					logger.info("Executing gameServer in the pool.");
				} catch (RuntimeException e) {
					logger.error(e.getMessage());
				}
			}
			
			while (true) {
				Socket connectingPlayerSocket = listener.accept();
				logger.info("Someone is trying to connect!");
				try {
					PlayerNetworkInformation connectedPlayer = this.connectPlayer(connectingPlayerSocket);
					this.table.addSpectator(connectedPlayer);
					logger.info("Added a spectator.");
				} catch (RuntimeException e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			logger.fatal("Fatal error listening to new connections. Exiting lobby server.");
			logger.fatal(e);
		}
		logger.info("Lobby has ended. Exiting main thread.");
	}
	
	private PlayerNetworkInformation connectPlayer(Socket connectingPlayerSocket) {
		Serializator connectingPlayerSerializator = initializeSerializator(connectingPlayerSocket);
		if (connectingPlayerSocket == null || connectingPlayerSerializator == null) {
			throw new RuntimeException("Could not communicate with client. Will not add it and listen for next one.");
		}
		Player currentPlayer = new Player("Spectator");
		return new PlayerNetworkInformation(connectingPlayerSocket, connectingPlayerSerializator, currentPlayer);
	}

	private Serializator initializeSerializator(Socket socket) {
		SerializatorFactory serializatorFactory = new SerializatorFactory();
		try {
			return serializatorFactory.getSerializator(socket);
		} catch (Exception e) {
			logger.debug(e);
		}
		return null;
	}

	private int getPortFromNetworkingProperties() {
		FileProperties fileProperties = new FileProperties(NETWORKING_CONFIGURATION_FILENAME);
		int port = 0;
		try {
			NetworkingProperties networkingProperties = new NetworkingProperties(fileProperties, new SystemProperties());
			port = networkingProperties.getPort();
		} catch (Exception e) {
			logger.fatal("Could not get port from properties.");
			logger.debug(e);
			System.exit(COULD_NOT_GET_PORT_FROM_PROPERTIES_ERROR);
		}

		return port;
	}

}
