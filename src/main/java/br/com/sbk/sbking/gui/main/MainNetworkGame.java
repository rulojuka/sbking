package br.com.sbk.sbking.gui.main;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.gui.frames.SBKingClientJFrame;
import br.com.sbk.sbking.gui.screens.MinibridgeScreen;
import br.com.sbk.sbking.gui.screens.PositiveKingScreen;
import br.com.sbk.sbking.gui.screens.WelcomeScreen;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class MainNetworkGame {

    public static void main(String[] args) {
        ClientApplicationState.startAppState();
        SBKingClientJFrame sbkingClientJFrame = new SBKingClientJFrame();

        WelcomeScreen welcomeScreen = new WelcomeScreen(sbkingClientJFrame);
        welcomeScreen.run();

        SBKingClient sbkingClient = welcomeScreen.getSBKingClient();
        MinibridgeScreen minibridgeScreen = new MinibridgeScreen(sbkingClientJFrame, sbkingClient);
        minibridgeScreen.run();

        PositiveKingScreen screen = new PositiveKingScreen(sbkingClientJFrame, sbkingClient);

        LOGGER.info("Exiting main thread.");
    }

}
