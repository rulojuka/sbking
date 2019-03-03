package core;

import java.util.ArrayList;
import java.util.List;

public class Trick {
	private static final int COMPLETE_TRICK_NUMBER_OF_CARDS = 4;
	private List<Card> trick = new ArrayList<Card>();
	private Direction leader;
	private Direction winner;

	public void addCard(Card card) {
		if (!this.isComplete()) {
			getTrickCards().add(card);
		} else {
			throw new RuntimeException("Trying to add card to a complete trick.");
		}
	}

	public Card getLeadCard() {
		return getTrickCards().get(0);
	}

	public Suit getSuit() {
		return this.getLeadCard().getSuit();
	}

	public void discard() {
		getTrickCards().clear();
		winner = null;
		leader = null;
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
		if(this.winner!=null) {
			return this.winner;
		}
		Suit leadSuit = this.getSuit();

		int resp = 0;
		Card highest, current;
		highest = this.getTrickCards().get(0);
		for (int i = 1; i < this.getNumberOfCards(); i++) {
			current = getTrickCards().get(i);
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

}