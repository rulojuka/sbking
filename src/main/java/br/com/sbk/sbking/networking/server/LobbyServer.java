package br.com.sbk.sbking.networking.server;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import java.util.ArrayList;
import java.util.List;

import br.com.sbk.sbking.core.constants.ErrorCodes;
import br.com.sbk.sbking.networking.core.properties.FileProperties;
import br.com.sbk.sbking.networking.core.properties.NetworkingProperties;
import br.com.sbk.sbking.networking.core.properties.SystemProperties;
import br.com.sbk.sbking.networking.server.gameServer.CagandoNoBequinhoGameServer;
import br.com.sbk.sbking.networking.server.gameServer.GameServer;
import br.com.sbk.sbking.networking.server.gameServer.KingGameServer;
import br.com.sbk.sbking.networking.server.gameServer.MinibridgeGameServer;
import br.com.sbk.sbking.networking.server.gameServer.PositiveKingGameServer;

public class LobbyServer {

    public void run() {
        int port = this.getPortFromNetworkingProperties();
        LOGGER.info("LobbyServer is Running...");

        SBKingServer sbkingServer = SBKingServerFactory.createWithKryonetConnection(port);
        LOGGER.info("SBKingServer created.");
        LOGGER.info("Listening for connections on port: " + port);

        List<Class<? extends GameServer>> gameServerClasses = new ArrayList<Class<? extends GameServer>>();
        gameServerClasses.add(CagandoNoBequinhoGameServer.class);
        gameServerClasses.add(KingGameServer.class);
        gameServerClasses.add(PositiveKingGameServer.class);
        gameServerClasses.add(MinibridgeGameServer.class);

        for (Class<? extends GameServer> gameServerClass : gameServerClasses) {
            sbkingServer.createTable(gameServerClass);
        }

    }

    private int getPortFromNetworkingProperties() {
        FileProperties fileProperties = new FileProperties();
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
