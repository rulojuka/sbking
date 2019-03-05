package br.com.sbk.sbking.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Trick {
	private static final int COMPLETE_TRICK_NUMBER_OF_CARDS = 4;
	private List<Card> listOfCards = new ArrayList<Card>();
	private Direction leader;
	private Direction winner;
	private boolean lastTwo;
	private Suit trumpSuit;

	public void addCard(Card card) {
		if (!this.isComplete()) {
			listOfCards.add(card);
		} else {
			throw new RuntimeException("Trying to add card to a complete trick.");
		}
	}

	public Suit getLeadSuit() {
		return this.getLeadCard().getSuit();
	}

	public void discard() {
		listOfCards.clear();
		leader = null;
		winner = null;
		lastTwo = false;
		trumpSuit = null;
	}

	public void setLeader(Direction direction) {
		leader = direction;
	}

	public Direction getLeader() {
		return this.leader;
	}

	public Direction getWinner() {
		if (this.winner != null) {
			return this.winner;
		}
		Suit leadSuit = this.getLeadSuit();

		int resp = 0;
		Card highest, current;
		highest = listOfCards.get(0);
		for (int i = 1; i < this.getNumberOfCards(); i++) {
			current = listOfCards.get(i);
			if (current.getSuit() == leadSuit) {
				if (highest.compareRank(current) < 0) {
					resp = i;
					highest = current;
				}
			}
		}

		if (trumpSuit != null) {
			for (int i = 0; i < this.getNumberOfCards(); i++) {
				current = listOfCards.get(i);
				if (current.getSuit() == trumpSuit) {
					if (highest.getSuit() != trumpSuit) {
						resp = i;
						highest = current;
						continue;
					} else if (highest.compareRank(current) < 0) {
						resp = i;
						highest = current;
					}
				}
			}
		}

		this.winner = leader.next(resp);
		return winner;
	}

	public boolean isComplete() {
		return this.getNumberOfCards() == COMPLETE_TRICK_NUMBER_OF_CARDS;
	}

	public boolean isEmpty() {
		return this.listOfCards.isEmpty();
	}

	public List<Card> getListOfCards() {
		return Collections.unmodifiableList(this.listOfCards);
	}

	public int getNumberOfMen() {
		int men = 0;
		for (Card c : listOfCards) {
			if (c.isMan()) {
				men++;
			}
		}
		return men;
	}

	public int getNumberOfWomen() {
		int women = 0;
		for (Card c : listOfCards) {
			if (c.isWoman()) {
				women++;
			}
		}
		return women;
	}

	public boolean isLastTwo() {
		return this.lastTwo;
	}

	public void setLastTwo() {
		this.lastTwo = true;
	}

	public boolean hasKingOfHearts() {
		for (Card c : listOfCards) {
			if (c.isKingOfHearts()) {
				return true;
			}
		}
		return false;
	}

	public int getNumberOfHeartsCards() {
		int hearts = 0;
		for (Card c : listOfCards) {
			if (c.isHeart()) {
				hearts++;
			}
		}
		return hearts;
	}

	public void setTrumpSuit(Suit trumpSuit) {
		this.trumpSuit = trumpSuit;

	}

	private Card getLeadCard() {
		return listOfCards.get(0);
	}

	private int getNumberOfCards() {
		return this.listOfCards.size();
	}

}