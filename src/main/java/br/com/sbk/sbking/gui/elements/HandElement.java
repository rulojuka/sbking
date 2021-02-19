package br.com.sbk.sbking.gui.elements;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Hand;
import br.com.sbk.sbking.core.Player;
import br.com.sbk.sbking.gui.JElements.CardButton;
import br.com.sbk.sbking.gui.models.DeckCardImageInformation;

public class HandElement {

	private static final int BETWEEN_CARDS_WIDTH = 22; /* 26 is good for the eyes. 22 to fit everything */
	private DeckCardImageInformation deckCardImageInformation;

	public HandElement(Hand hand, Container container, ActionListener actionListener, Point handCenter, Player player) {
		this.deckCardImageInformation = new DeckCardImageInformation();

		int x_offset = ((hand.size() + 1) * BETWEEN_CARDS_WIDTH) / 2;
		x_offset *= -1;
		int y_offset = deckCardImageInformation.getCardHeight() / 2;
		y_offset *= -1;
		Point handTopLeftCorner = new Point(handCenter);
		handTopLeftCorner.translate(x_offset, y_offset);

		for (int i = hand.size() - 1; i >= 0; i--) { // This way, it draws correctly
			Card card = hand.get(i);
			CardButton cardButton = new CardButton(card, deckCardImageInformation);
			if (actionListener != null) {
				cardButton.addActionListener(actionListener);
			}
			container.add(cardButton); // This line needs to go before setting the button location
			cardButton.setLocation(locationOfCard(i, handTopLeftCorner)); // This line needs to go after adding the
																			// button to
			// the container
		}


		JLabel nameLabel = new JLabel(player.getName());
		nameLabel.setSize(200, 15);
		Point inicio = handTopLeftCorner;
		handTopLeftCorner.translate(0, deckCardImageInformation.getCardHeight()+5);
		nameLabel.setLocation(inicio);
		container.add(nameLabel);
	}

	private Point locationOfCard(int index, Point handTopLeftCorner) {
		Point cardLocation = (Point) handTopLeftCorner.clone();
		cardLocation.translate(index * BETWEEN_CARDS_WIDTH, 0);
		return cardLocation;
	}

}
