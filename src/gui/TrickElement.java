package gui;

import java.awt.Container;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import core.Card;
import core.Direction;
import core.Trick;

public class TrickElement {

	private List<CardButton> trick;
	private final int CARD_WIDTH = 72;
	private final int CARD_HEIGHT = 96;

	public TrickElement(Trick currentTrick, Container container, Point point) {
		trick = new ArrayList<CardButton>();
		Direction leader = currentTrick.getLeader();
		List<Card> listOfCards = currentTrick.getTrickCards();
		for (int i = listOfCards.size() - 1; i >= 0; i--) {
			Card card = listOfCards.get(i);
			CardButton cardButton = new CardButton(card, DeckCardImageInformation.getFrontImage(card),
					DeckCardImageInformation.getBackImage());
			trick.add(cardButton);
			container.add(cardButton); // This line needs to go before setting the button location
			cardButton.setLocation(cardLocation(leader.next(i), point)); // This line needs to go after adding the
																			// button to the container
		}
	}

	private Point cardLocation(Direction direction, Point center) {
		int x = center.x - CARD_WIDTH / 2;
		int y = center.y - CARD_HEIGHT / 2;
		int diff = 30;
		int dx;
		int dy;
		if (direction == Direction.NORTH) {
			dx = 0;
			dy = -diff;
		} else if (direction == Direction.EAST) {
			dx = +diff;
			dy = 0;
		} else if (direction == Direction.SOUTH) {
			dx = 0;
			dy = +diff;
		} else {
			dx = -diff;
			dy = 0;
		}
		return new Point(x + dx, y + dy);
	}

}
