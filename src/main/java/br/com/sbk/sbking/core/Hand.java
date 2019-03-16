package br.com.sbk.sbking.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Hand {

	private List<Card> listOfCards = new ArrayList<Card>();

	public void addCard(Card card) {
		listOfCards.add(card);
	}

	public void removeCard(Card card) {
		listOfCards.remove(card);
	}

	public void sort() {
		Collections.sort(listOfCards, new CardInsideHandComparator());
	}

	public boolean containsCard(Card card) {
		return this.listOfCards.contains(card);
	}

	public boolean hasSuit(Suit suit) {
		for (Card card : listOfCards) {
			if (card.getSuit() == suit) {
				return true;
			}
		}
		return false;
	}

	public int HCP() {
		int resp = 0;
		for (Card card : listOfCards) {
			resp += card.points();
		}
		return resp;
	}

	public List<Card> getListOfCards() {
		return Collections.unmodifiableList(this.listOfCards);
	}

	public boolean onlyHasHearts() {
		for (Card card : listOfCards) {
			if (!card.isHeart()) {
				return false;
			}
		}
		return true;
	}

	private class CardInsideHandComparator implements Comparator<Card> {

		@Override
		public int compare(Card card1, Card card2) {
			int suitDiff = card1.compareSuit(card2);
			if (suitDiff != 0) {
				return -suitDiff;
			} else {
				int rankDiff = card1.compareRank(card2);
				return -rankDiff;
			}
		}

	}

}