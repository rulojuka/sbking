package br.com.sbk.sbking.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Hand {

	private List<Card> cards = new ArrayList<Card>();

	public void addCard(Card card) {
		this.cards.add(card);
	}

	public void removeCard(Card card) {
		this.cards.remove(card);
	}

	public Card get(int position) {
		return this.getCards().get(position);
	}

	public int size() {
		return this.getCards().size();
	}

	public void sort() {
		Collections.sort(this.cards, new CardInsideHandComparator());
	}

	private static class CardInsideHandComparator implements Comparator<Card> {

		@Override
		public int compare(Card card1, Card card2) {
			int suitDifference = card1.compareSuit(card2);
			if (suitDifference != 0) {
				return -suitDifference;
			} else {
				int rankDifference = card1.compareRank(card2);
				return -rankDifference;
			}
		}

	}

	public boolean containsCard(Card card) {
		return this.getCards().contains(card);
	}

	public boolean hasSuit(Suit suit) {
		for (Card card : this.getCards()) {
			if (card.getSuit() == suit) {
				return true;
			}
		}
		return false;
	}

	public boolean onlyHasHearts() {
		for (Card card : this.getCards()) {
			if (!card.isHeart()) {
				return false;
			}
		}
		return true;
	}

	private List<Card> getCards() {
		return Collections.unmodifiableList(this.cards);
	}

}