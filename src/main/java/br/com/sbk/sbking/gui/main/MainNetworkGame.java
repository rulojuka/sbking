package br.com.sbk.sbking.gui.main;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import br.com.sbk.sbking.gui.frames.NetworkClientScreen;

public class MainNetworkGame {

	final static Logger logger = LogManager.getLogger(NetworkClientScreen.class);

	public static void main(String args[]) {
		NetworkClientScreen networkClientScreen = new NetworkClientScreen();
		networkClientScreen.run();
		logger.info("Exiting main thread.");
	}

}
