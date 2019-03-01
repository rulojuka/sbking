package gui;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import core.Card;
import core.Hand;

public class HandElement {

	private List<CardButton> cards;
	private final int BETWEEN_CARDS_WIDTH = 26;

	public HandElement(Hand hand, Container container, ActionListener actionListener, Point handLocation) {
		cards = new ArrayList<CardButton>();
		List<Card> listOfCards = hand.getListOfCards();
		for (int i = listOfCards.size() - 1; i >= 0; i--) { // This way, it draws correctly
			Card card = listOfCards.get(i);
			CardButton cardButton = new CardButton(card, DeckCardImageInformation.getFrontImage(card),
					DeckCardImageInformation.getBackImage());
			cardButton.addActionListener(actionListener);
			cards.add(cardButton);
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
