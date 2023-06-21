package br.com.sbk.sbking.clientapp;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import org.springframework.stereotype.Component;

import br.com.sbk.sbking.gui.frames.SBKingClientJFrame;
import br.com.sbk.sbking.gui.main.ConnectToServer;
import br.com.sbk.sbking.gui.main.MainNetworkGame;
import br.com.sbk.sbking.networking.client.SBKingClient;

@Component
public class ClientComponent {

    MainNetworkGame game;

    public ClientComponent() {
        LOGGER.trace("Initializing ClientComponent");
        SBKingClient sbkingClient = ConnectToServer.connectToServer();
        SBKingClientJFrame sbkingClientJFrame = new SBKingClientJFrame();

        game = new MainNetworkGame(sbkingClient, sbkingClientJFrame);
    }

    public void run() {
        game.run();
    }

}
