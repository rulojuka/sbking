package br.com.sbk.sbking.app;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.sbk.sbking.networking.server.LobbyServer;
import br.com.sbk.sbking.networking.server.SBKingServer;

@Component
public class ServerComponent {

    private SBKingServer sbKingServer;

    public SBKingServer getSbKingServer() {
        return sbKingServer;
    }

    public ServerComponent(@Autowired PlayerController spectatorController) {
        LOGGER.info("Entering Main Thread.");
        LobbyServer lobbyServer = new LobbyServer(spectatorController);
        lobbyServer.run();
        this.sbKingServer = lobbyServer.getSbKingServer();
        LOGGER.info("Exiting Main Thread.");
    }

}
