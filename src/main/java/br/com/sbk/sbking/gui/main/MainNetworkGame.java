package br.com.sbk.sbking.gui.main;

import static br.com.sbk.sbking.logging.SBKingLogger.LOGGER;

import br.com.sbk.sbking.gui.frames.MinibridgeClientScreen;
import br.com.sbk.sbking.gui.frames.NetworkClientScreen;

public class MainNetworkGame {

    public static void main(String[] args) {
        ClientApplicationState.startAppState();
        NetworkClientScreen networkClientScreen = new MinibridgeClientScreen();
        networkClientScreen.run();
        LOGGER.info("Exiting main thread.");
    }

}
