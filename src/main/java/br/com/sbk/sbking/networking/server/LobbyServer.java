package br.com.sbk.sbking.networking.server;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.sbk.sbking.core.constants.ErrorCodes;
import br.com.sbk.sbking.networking.core.properties.FileProperties;
import br.com.sbk.sbking.networking.core.properties.NetworkingProperties;
import br.com.sbk.sbking.networking.core.properties.SystemProperties;
import br.com.sbk.sbking.networking.server.gameServer.GameServer;
import br.com.sbk.sbking.networking.server.gameServer.MinibridgeGameServer;

public class LobbyServer {

    private static final String NETWORKING_CONFIGURATION_FILENAME = "networkConfiguration.cfg";

    private Table table;

    private static final int MAXIMUM_NUMBER_OF_CONCURRENT_GAME_SERVERS = 2;
    private ExecutorService pool;

    public LobbyServer() {
        this.pool = Executors.newFixedThreadPool(MAXIMUM_NUMBER_OF_CONCURRENT_GAME_SERVERS);
    }

    public void run() {
        int port = this.getPortFromNetworkingProperties();
        LOGGER.info("LobbyServer is Running...");

        GameServer gameServer = new MinibridgeGameServer();
        LOGGER.info("Created new MinibridgeGameServer");
        this.table = new Table(gameServer);

        SBKingServer sbkingServer = SBKingServerFactory.createWithKryonetConnection(this.table, port, gameServer);
        LOGGER.info("Listening for connections on port: " + port);

        LOGGER.info("Sleeping while nobody is connected");
        while (sbkingServer.nobodyIsConnected()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOGGER.error(e);
            }
        }
        LOGGER.info("Finished sleeping because someone is connected");
        LOGGER.info("Executing game server");

        pool.execute(gameServer);
    }

    private int getPortFromNetworkingProperties() {
        FileProperties fileProperties = new FileProperties(NETWORKING_CONFIGURATION_FILENAME);
        int port = 0;
        try {
            NetworkingProperties networkingProperties = new NetworkingProperties(fileProperties,
                    new SystemProperties());
            port = networkingProperties.getPort();
        } catch (Exception e) {
            LOGGER.fatal("Could not get port from properties.");
            LOGGER.debug(e);
            System.exit(ErrorCodes.COULD_NOT_GET_PORT_FROM_PROPERTIES_ERROR);
        }

        return port;
    }

}
