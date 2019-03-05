package br.com.sbk.sbking.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import br.com.sbk.sbking.core.exceptions.TrickAlreadyFullException;

public class Trick {
	private static final int COMPLETE_TRICK_NUMBER_OF_CARDS = 4;
	private List<Card> listOfCards;
	private Direction leader;
	private boolean lastTwo;

	public Trick(Direction leader) {
		this.leader = leader;
		this.listOfCards = new ArrayList<Card>();
		this.lastTwo = false;
	}

	public void addCard(Card card) {
		if (!this.isComplete()) {
			listOfCards.add(card);
		} else {
			throw new TrickAlreadyFullException();
		}
	}

	public boolean isEmpty() {
		return this.listOfCards.isEmpty();
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
		Card card = highestCardOfSuit(this.getLeadSuit());
		return directionOfCard(card);
	}

	public Direction getWinnerWithTrumpSuit(Suit trumpSuit) {
		Card card;
		if (this.hasSuit(trumpSuit)) {
			card = highestCardOfSuit(trumpSuit);
		} else {
			card = highestCardOfSuit(this.getLeadSuit());
		}
		return directionOfCard(card);
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

	private Card highestCardOfSuit(Suit suit) {
		SortedSet<Card> sortedCardsOfSuit = new TreeSet<Card>(new CardOfSameSuitComparator());
		for (Card card : this.listOfCards) {
			if (card.getSuit() == suit) {
				sortedCardsOfSuit.add(card);
			}
		}
		return sortedCardsOfSuit.last();
	}

	private Direction directionOfCard(Card card) {
		Direction current = leader;
		for (Card cardFromList : listOfCards) {
			if (card.equals(cardFromList)) {
				return current;
			} else {
				current = current.next();
			}
		}
		throw new RuntimeException("Card not found");
	}

	private boolean hasSuit(Suit suit) {
		for (Card card : listOfCards) {
			if (card.getSuit() == suit) {
				return true;
			}
		}
		return false;
	}

	private Card getLeadCard() {
		return listOfCards.get(0);
	}

	private int getNumberOfCards() {
		return this.listOfCards.size();
	}

}