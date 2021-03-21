package br.com.sbk.sbking.gui.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.sbk.sbking.gui.frames.MinibridgeClientScreen;
import br.com.sbk.sbking.gui.frames.NetworkClientScreen;

public class MainNetworkGame {

	final static Logger logger = LogManager.getLogger(MainNetworkGame.class);

	public static void main(String args[]) {
		ClientApplicationState.startAppState();
		NetworkClientScreen networkClientScreen = new MinibridgeClientScreen();
		networkClientScreen.run();
		logger.info("Exiting main thread.");
	}

}
