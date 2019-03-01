package core;

import java.util.ArrayList;
import java.util.List;

public class Dealer {

	private final int SIZE_OF_HAND = 13;

	public Board deal() {
		/* Initializing Hands */
		List<Hand> hands = new ArrayList<Hand>();
		Deck deck = new Deck();
		deck.restore();
		deck.shuffle();
		deck.restore();

		for (Direction direction : Direction.values()) {
			Hand currentHand = new Hand();
			currentHand.setOwner(direction);
			for (int i = 0; i < SIZE_OF_HAND; i++) {
				currentHand.addCard(deck.dealCard());
			}
			hands.add(currentHand);
		}
		return new Board(hands);
	}

}
