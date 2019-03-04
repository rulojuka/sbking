package core;

import java.util.ArrayList;
import java.util.List;

import core.rulesets.Ruleset;

public class Dealer {

	private final int SIZE_OF_HAND = 13;

	public Board deal(Ruleset ruleset) {
		/* Initializing Hands */
		List<Hand> hands = new ArrayList<Hand>();
		Deck deck = new Deck();
		deck.shuffle();

		for (Direction direction : Direction.values()) {
			Hand currentHand = new Hand();
			for (int i = 0; i < SIZE_OF_HAND; i++) {
				currentHand.addCard(deck.dealCard());
			}
			hands.add(currentHand);
		}
		return new Board(hands, ruleset);
	}

}
