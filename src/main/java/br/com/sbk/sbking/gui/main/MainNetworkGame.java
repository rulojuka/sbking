package br.com.sbk.sbking.gui.main;

import org.apache.log4j.Logger;

import br.com.sbk.sbking.gui.frames.NetworkClientScreen;

public class MainNetworkGame {

	final static Logger logger = Logger.getLogger(NetworkClientScreen.class);

	public static void main(String args[]) {

		NetworkClientScreen networkClientScreen = new NetworkClientScreen();
		networkClientScreen.run();
		logger.info("Exiting main thread.");
	}

}
