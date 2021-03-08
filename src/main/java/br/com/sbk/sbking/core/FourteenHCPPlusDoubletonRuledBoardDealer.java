package br.com.sbk.sbking.core;

import java.util.HashMap;
import java.util.Map;

public class FourteenHCPPlusDoubletonRuledBoardDealer implements BoardDealer {

	private Map<Direction, Hand> hands = new HashMap<Direction, Hand>();

	@Override
	public Board dealBoard(Direction dealer) {
		Direction currentDirection;
		Hand currentHand;
		ShuffledDeck currentDeck;
		do {
			currentDeck = new ShuffledDeck();
			hands.clear();
			for (Direction direction : Direction.values()) {
				hands.put(direction, new Hand());
			}
			for (currentDirection = dealer; currentDeck.hasCard(); currentDirection = currentDirection.next()) {
				currentHand = this.hands.get(currentDirection);
				currentHand.addCard(currentDeck.dealCard());
			}
		} while (!validateHand(hands.get(dealer.getPositiveOrNegativeChooserWhenDealer())));
		return new Board(hands, dealer);
	}

	private boolean validateHand(Hand hand) {
		return hand.getHCP() >= 14 && hand.getShortestSuitLength() >= 2;
	}

}