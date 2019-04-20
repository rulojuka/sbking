package br.com.sbk.sbking.networking.server;

import java.net.Socket;

import br.com.sbk.sbking.networking.core.serialization.Serializator;

public class PlayerNetworkInformation {
	private Socket socket;
	private Serializator serializator;

	public PlayerNetworkInformation(Socket socket, Serializator serializator) {
		this.socket = socket;
		this.serializator = serializator;
	}

	public Socket getSocket() {
		return socket;
	}

	public Serializator getSerializator() {
		return serializator;
	}

}
