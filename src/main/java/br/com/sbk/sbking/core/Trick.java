package br.com.sbk.sbking.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import br.com.sbk.sbking.core.exceptions.TrickAlreadyFullException;

public class Trick {
	private static final int COMPLETE_TRICK_NUMBER_OF_CARDS = 4;
	private List<Card> cards;
	private Direction leader;
	private boolean lastTwo;

	public Trick(Direction leader) {
		this.leader = leader;
		this.cards = new ArrayList<Card>();
		this.lastTwo = false;
	}

	public void addCard(Card card) {
		if (!this.isComplete()) {
			this.cards.add(card);
		} else {
			throw new TrickAlreadyFullException();
		}
	}

	public boolean isEmpty() {
		return this.cards.isEmpty();
	}

	public boolean isComplete() {
		return this.getNumberOfCards() == COMPLETE_TRICK_NUMBER_OF_CARDS;
	}

	public Direction getLeader() {
		return this.leader;
	}

	public Suit getLeadSuit() {
		return this.getLeadCard().getSuit();
	}

	public Direction getWinnerWithoutTrumpSuit() {
		Card card = this.highestCardOfSuit(this.getLeadSuit());
		return this.directionOfCard(card);
	}

	public Direction getWinnerWithTrumpSuit(Suit trumpSuit) {
		Card card;
		if (this.hasSuit(trumpSuit)) {
			card = this.highestCardOfSuit(trumpSuit);
		} else {
			card = this.highestCardOfSuit(this.getLeadSuit());
		}
		return this.directionOfCard(card);
	}

	public List<Card> getCards() {
		return Collections.unmodifiableList(this.cards);
	}

	public int getNumberOfMen() {
		int men = 0;
		for (Card c : this.getCards()) {
			if (c.isMan()) {
				men++;
			}
		}
		return men;
	}

	public int getNumberOfWomen() {
		int women = 0;
		for (Card c : this.getCards()) {
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
		for (Card c : this.getCards()) {
			if (c.isKingOfHearts()) {
				return true;
			}
		}
		return false;
	}

	public int getNumberOfHeartsCards() {
		int hearts = 0;
		for (Card c : this.getCards()) {
			if (c.isHeart()) {
				hearts++;
			}
		}
		return hearts;
	}

	private Card highestCardOfSuit(Suit suit) {
		SortedSet<Card> sortedCardsOfSuit = new TreeSet<Card>(new CardOfSameSuitComparator());
		for (Card card : this.getCards()) {
			if (card.getSuit() == suit) {
				sortedCardsOfSuit.add(card);
			}
		}
		return sortedCardsOfSuit.last();
	}

	private class CardOfSameSuitComparator implements Comparator<Card> {
		@Override
		public int compare(Card card1, Card card2) {
			return card1.compareRank(card2);
		}
	}

	private Direction directionOfCard(Card card) {
		Direction currentDirection = leader;
		for (Card cardFromTrick : this.getCards()) {
			if (card.equals(cardFromTrick)) {
				return currentDirection;
			} else {
				currentDirection = currentDirection.next();
			}
		}
		throw new RuntimeException("Card not found");
	}

	private boolean hasSuit(Suit suit) {
		for (Card card : this.getCards()) {
			if (card.getSuit() == suit) {
				return true;
			}
		}
		return false;
	}

	private Card getLeadCard() {
		return this.getCards().get(0);
	}

	private int getNumberOfCards() {
		return this.getCards().size();
	}

}