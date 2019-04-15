package br.com.sbk.sbking.gui.listeners;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.gui.JElements.CardButton;
import br.com.sbk.sbking.networking.client.NetworkCardPlayer;

public class PlayCardActionListener implements java.awt.event.ActionListener {
	private NetworkCardPlayer networkCardPlayer;

	public PlayCardActionListener(NetworkCardPlayer networkCardPlayer) {
		super();
		this.networkCardPlayer = networkCardPlayer;
	}

	public void actionPerformed(java.awt.event.ActionEvent event) {
		CardButton clickedCardButton = (CardButton) event.getSource();
		Card card = clickedCardButton.getCard();
		try {
			networkCardPlayer.play(card);
		} catch (RuntimeException e) {
			throw e;
		}
	}
}
