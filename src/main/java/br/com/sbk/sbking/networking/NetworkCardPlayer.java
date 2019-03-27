package br.com.sbk.sbking.networking;

import br.com.sbk.sbking.core.Card;

public class NetworkCardPlayer {

	private Serializator serializator;

	public NetworkCardPlayer(Serializator serializator) {
		this.serializator = serializator;
	}

	public void play(Card card) {
		this.serializator.tryToSerialize(card);
		System.out.println("NetworkCardPlayer.play isEventDispatchThread: " + javax.swing.SwingUtilities.isEventDispatchThread());
		System.exit(1);
	}

}
