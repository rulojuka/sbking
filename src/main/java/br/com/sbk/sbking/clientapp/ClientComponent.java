package br.com.sbk.sbking.clientapp;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import org.springframework.stereotype.Component;

import br.com.sbk.sbking.gui.frames.SBKingClientJFrame;
import br.com.sbk.sbking.gui.main.ConnectToServer;
import br.com.sbk.sbking.gui.main.MainNetworkGame;
import br.com.sbk.sbking.networking.client.SBKingClient;

@Component
public class ClientComponent {

    private MainNetworkGame game;
    private SBKingClient sbkingClient;

    public ClientComponent() {
        LOGGER.trace("Initializing ClientComponent");
        this.sbkingClient = ConnectToServer.connectToServer();
        game = new MainNetworkGame(sbkingClient, new SBKingClientJFrame());
    }

    public void run() {
        game.run();
    }

    public SBKingClient getSBKingClient() {
        return this.sbkingClient;
    }

}
