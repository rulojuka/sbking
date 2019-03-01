package core;

import java.util.*;

public class Trick {
	private static final int COMPLETE_TRICK_NUMBER_OF_CARDS = 4;
	private List<Card> trick = new ArrayList<Card>();
	private Direction leader;
	private Suit trumpSuit;

	public void addCard(Card card) {
		if (this.getNumberOfCards() < 4)
			trick.add(card);
	}

	public Card getCard(int index) {
		return trick.get(index);
	}

	public void discard() {
		trick.clear();
		trumpSuit = null;
		leader = null;
	}

	public void setLeader(Direction direction) {
		leader = direction;
	}

	public Direction getLeader() {
		return this.leader;
	}

	public void setTrump(Suit suit) {
		trumpSuit = suit;
	}

	public Suit getTrump() {
		return trumpSuit;
	}

	public int getNumberOfCards() {
		return trick.size();
	}

	public Direction winner() {
		Card firstCard = trick.get(0);
		Suit leadSuit = firstCard.getSuit();
		
		// Trump suits not implemented yet
		boolean hasTrump = false;
		for (int i = 0; i < 4; i++) {
			if ((trick.get(i)).getSuit() == trumpSuit)
				hasTrump = true;
		}

		int resp = 0;
		Card bigger, current;
		for (int i = 1; i < 4; i++) {
			bigger = trick.get(resp);
			current = trick.get(i);
			if (current.getSuit() == trumpSuit) {
				if (bigger.getSuit() != trumpSuit) {
					resp = i;
				} else {
					if (bigger.compareTo(current) > 0)
						resp = i;
				}
			} else if (current.getSuit() == leadSuit) {
				if (bigger.getSuit() != trumpSuit) {
					if (bigger.compareTo(current) > 0)
						resp = i;
				}
			}
		}
		return leader.next(resp);
	}

	public boolean isComplete() {
		return this.getNumberOfCards() == COMPLETE_TRICK_NUMBER_OF_CARDS;
	}
	
	public boolean isEmpty() {
		return this.getNumberOfCards() == 0;
	}

}