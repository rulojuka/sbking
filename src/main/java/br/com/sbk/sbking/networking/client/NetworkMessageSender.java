package br.com.sbk.sbking.networking.client;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Direction;
import br.com.sbk.sbking.networking.core.serialization.Serializator;

public class NetworkMessageSender {

	private Serializator serializator;

	public NetworkMessageSender(Serializator serializator) {
		this.serializator = serializator;
	}

	public void play(Card card) {
		this.serializator.tryToSerialize(card);
	}

	public void sit(Direction direction) {
		this.serializator.tryToSerialize(direction);
	}

}
