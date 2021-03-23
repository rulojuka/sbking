package br.com.sbk.sbking.networking.server.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.sbk.sbking.networking.server.LobbyServer;

public class LobbyServerStarter {
    private static final Logger logger = LogManager.getLogger(LobbyServerStarter.class);

    public static void main(String[] args) {
        logger.info("Entering application.");
        LobbyServer lobbyServer = new LobbyServer();
        lobbyServer.run();
        logger.info("Exiting application.");
    }
}
