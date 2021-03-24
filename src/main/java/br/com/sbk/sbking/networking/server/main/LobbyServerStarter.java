package br.com.sbk.sbking.networking.server.main;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.networking.server.LobbyServer;

public class LobbyServerStarter {

    public static void main(String[] args) {
        LOGGER.info("Entering application.");
        LobbyServer lobbyServer = new LobbyServer();
        lobbyServer.run();
        LOGGER.info("Exiting application.");
    }
}
