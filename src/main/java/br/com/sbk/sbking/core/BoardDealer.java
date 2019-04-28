package br.com.sbk.sbking.core;

import java.util.ArrayList;
import java.util.List;

public class BoardDealer {

	private List<Hand> hands = new ArrayList<Hand>();

	public Board dealBoard(Direction dealer, ShuffledDeck shuffledDeck) {
		Direction currentDirection;
		Hand currentHand;
		for (Direction direction : Direction.values()) {
			hands.add(new Hand());
		}
		for (currentDirection = dealer; shuffledDeck.hasCard(); currentDirection = currentDirection.next()) {
			currentHand = getCurrentHand(currentDirection);
			currentHand.addCard(shuffledDeck.dealCard());
		}
		return new Board(hands.get(0), hands.get(1), hands.get(2), hands.get(3), dealer);
	}

	private Hand getCurrentHand(Direction currentDirection) {
		int currentIndex = currentDirection.ordinal();
		return this.hands.get(currentIndex);
	}

}
