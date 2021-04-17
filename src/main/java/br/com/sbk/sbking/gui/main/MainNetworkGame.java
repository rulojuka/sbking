package br.com.sbk.sbking.gui.main;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.gui.frames.SBKingClientJFrame;
import br.com.sbk.sbking.gui.screens.MinibridgeScreen;
import br.com.sbk.sbking.gui.screens.SBKingScreen;
import br.com.sbk.sbking.gui.screens.WelcomeScreen;
import br.com.sbk.sbking.networking.client.SBKingClient;

public class MainNetworkGame {

    public static void main(String[] args) {
        ClientApplicationState.startAppState();
        SBKingClientJFrame sbkingClientJFrame = new SBKingClientJFrame();

        WelcomeScreen welcomeScreen = new WelcomeScreen();
        welcomeScreen.runAt(sbkingClientJFrame);

        SBKingClient sbkingClient = welcomeScreen.getSBKingClient();

        SBKingScreen currentScreen;
        currentScreen = new MinibridgeScreen(sbkingClient);
        currentScreen.runAt(sbkingClientJFrame);

        LOGGER.info("Exiting main thread.");
    }

}
