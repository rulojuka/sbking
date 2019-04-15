package br.com.sbk.sbking.networking.server.main;

import br.com.sbk.sbking.networking.server.GameServer;

public class GameServerStarter {

	public static void main(String[] args) throws Exception {
		GameServer gameServer = new GameServer();
		gameServer.run();
	}
}
