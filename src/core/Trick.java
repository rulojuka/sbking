package core;

import java.util.ArrayList;
import java.util.List;

public class Trick {
	private static final int COMPLETE_TRICK_NUMBER_OF_CARDS = 4;
	private List<Card> trick = new ArrayList<Card>();
	private Direction leader;
	private Direction winner;
	private boolean lastTwo;

	public void addCard(Card card) {
		if (!this.isComplete()) {
			trick.add(card);
		} else {
			throw new RuntimeException("Trying to add card to a complete trick.");
		}
	}

	public Card getLeadCard() {
		return trick.get(0);
	}

	public Suit getSuit() {
		return this.getLeadCard().getSuit();
	}

	public void discard() {
		trick.clear();
		leader = null;
		winner = null;
		lastTwo = false;
	}

	public void setLeader(Direction direction) {
		leader = direction;
	}

	public Direction getLeader() {
		return this.leader;
	}

	private int getNumberOfCards() {
		return getTrickCards().size();
	}

	public Direction getWinner() {
		if (this.winner != null) {
			return this.winner;
		}
		Suit leadSuit = this.getSuit();

		int resp = 0;
		Card highest, current;
		highest = trick.get(0);
		for (int i = 1; i < this.getNumberOfCards(); i++) {
			current = trick.get(i);
			if (current.getSuit() == leadSuit) {
				if (highest.compareTo(current) < 0) {
					resp = i;
					highest = current;
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
		return this.getNumberOfCards() == 0;
	}

	public List<Card> getTrickCards() {
		return trick;
	}

	public int getNumberOfMen() {
		int men = 0;
		for (Card c : trick) {
			if (c.isMan()) {
				men++;
			}
		}
		return men;
	}

	public int getNumberOfWomen() {
		int women = 0;
		for (Card c : trick) {
			if (c.isQueen()) {
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
		for (Card c : trick) {
			if (c.isKingOfHearts()) {
				return true;
			}
		}
		return false;
	}

	public int getNumberOfHeartsCards() {
		int hearts = 0;
		for (Card c : trick) {
			if (c.isHeart()) {
				hearts++;
			}
		}
		return hearts;
	}

}