package br.com.sbk.sbking.networking.client;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.networking.core.serialization.Serializator;

public class NetworkCardPlayer {

	private Serializator serializator;

	public NetworkCardPlayer(Serializator serializator) {
		this.serializator = serializator;
	}

	public void play(Card card) {
		this.serializator.tryToSerialize(card);
	}

}
