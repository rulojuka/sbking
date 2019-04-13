package br.com.sbk.sbking.core;

import java.util.ArrayList;
import java.util.List;

public class BoardDealer {

	private static final int SIZE_OF_HAND = 13;

	public static Board dealBoard(Direction dealer) {
		List<Hand> hands = new ArrayList<Hand>();
		ShuffledDeck shuffledDeck = new ShuffledDeck();

		for (Direction direction : Direction.values()) {
			Hand currentHand = new Hand();
			for (int i = 0; i < SIZE_OF_HAND; i++) {
				currentHand.addCard(shuffledDeck.dealCard());
			}
			hands.add(currentHand);
		}

		return new Board(hands.get(0), hands.get(1), hands.get(2), hands.get(3), dealer);
	}

}
