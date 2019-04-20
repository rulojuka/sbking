package br.com.sbk.sbking.networking.server.main;

import br.com.sbk.sbking.networking.server.LobbyServer;

public class LobbyServerStarter {

	public static void main(String[] args) {
		LobbyServer lobbyServer = new LobbyServer();
		lobbyServer.run();
	}
}
