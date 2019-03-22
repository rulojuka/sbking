package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.gui.DeckCardImageInformation;
import br.com.sbk.sbking.gui.JElements.CardButton;

public class HandElement {

	private static final int BETWEEN_CARDS_WIDTH = 26;
	private DeckCardImageInformation deckCardImageInformation;

	public HandElement(Hand hand, Container container, ActionListener actionListener, Point handLocation) {
		deckCardImageInformation = new DeckCardImageInformation();
		for (int i = hand.size() - 1; i >= 0; i--) { // This way, it draws correctly
			Card card = hand.get(i);
			CardButton cardButton = new CardButton(card, deckCardImageInformation.getFrontImage(card),
					deckCardImageInformation.getBackImage());
			cardButton.addActionListener(actionListener);
			container.add(cardButton); // This line needs to go before setting the button location
			cardButton.setLocation(locationOfCard(i, handLocation)); // This line needs to go after adding the button to
																		// the container
		}
	}

	private Point locationOfCard(int index, Point handLocation) {
		Point cardLocation = (Point) handLocation.clone();
		cardLocation.translate(index * BETWEEN_CARDS_WIDTH, 0);
		return cardLocation;
	}

}
