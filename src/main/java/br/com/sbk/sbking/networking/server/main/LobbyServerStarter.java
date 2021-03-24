package br.com.sbk.sbking.networking.server.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.sbk.sbking.networking.server.LobbyServer;

public class LobbyServerStarter {
    private static final Logger LOGGER = LogManager.getLogger(LobbyServerStarter.class);

    public static void main(String[] args) {
        LOGGER.info("Entering application.");
        LobbyServer lobbyServer = new LobbyServer();
        lobbyServer.run();
        LOGGER.info("Exiting application.");
    }
}
