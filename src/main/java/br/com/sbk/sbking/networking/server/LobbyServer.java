package br.com.sbk.sbking.networking.server;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.app.PlayerController;
import br.com.sbk.sbking.core.constants.ErrorCodes;
import br.com.sbk.sbking.networking.core.properties.FileProperties;
import br.com.sbk.sbking.networking.core.properties.NetworkingProperties;
import br.com.sbk.sbking.networking.core.properties.SystemProperties;

public class LobbyServer {

    private SBKingServer sbKingServer;
    private PlayerController playerController;

    public LobbyServer(PlayerController playerController) {
        this.playerController = playerController;
    }

    public SBKingServer getSbKingServer() {
        return sbKingServer;
    }

    public void run() {
        int port = this.getPortFromNetworkingProperties();
        LOGGER.info("LobbyServer is Running...");

        this.sbKingServer = SBKingServerFactory.createWithKryonetConnection(port, playerController);
        LOGGER.info("SBKingServer created.");
        LOGGER.info("Listening for connections on port: {}", port);
    }

    private int getPortFromNetworkingProperties() {
        int port = 0;
        FileProperties fileProperties = new FileProperties();
        LOGGER.info("Created fileProperties.");
        LOGGER.info("Host: {}\nPort: {}", fileProperties.getHost(), fileProperties.getPort());
        try {
            NetworkingProperties networkingProperties = new NetworkingProperties(fileProperties,
                    new SystemProperties());
            LOGGER.info("Created networkingProperties.");
            String finalHost = networkingProperties.getHost();
            port = networkingProperties.getPort();
            LOGGER.info("Host: {}\nPort: {}", finalHost, port);
        } catch (Exception e) {
            LOGGER.fatal("Could not get port from properties.");
            LOGGER.fatal(e);
            System.exit(ErrorCodes.COULD_NOT_GET_PORT_FROM_PROPERTIES_ERROR);
        }

        return port;
    }

}
