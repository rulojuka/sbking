package core;

import java.util.Iterator;

public class Dealer {
	
	private final int NUMBER_OF_HANDS = 4;
	private final int SIZE_OF_HAND = 13;
	
	public Board deal() {
		/* Initializing Hands */
		Hand[] hands = new Hand[NUMBER_OF_HANDS];
		Deck deck = new Deck();
		deck.restore();
		deck.shuffle();
		deck.restore();
		Iterator<Direction> directionIterator = Direction.VALUES.iterator();
		for (int k = 0; k < NUMBER_OF_HANDS; k++) {
			if (directionIterator.hasNext()) {
				Direction direction = directionIterator.next();
				hands[k] = new Hand();
				hands[k].setOwner(direction);
				for(int i=0; i<SIZE_OF_HAND; i++) {
					hands[k].addCard(deck.dealCard());
				}
			}
		}
		return new Board(hands[0],hands[1],hands[2],hands[3]);
	}

}
