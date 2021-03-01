package br.com.sbk.sbking.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("serial")
public class Hand implements Serializable {

	private List<Card> cards = new ArrayList<Card>();

	public void addCard(Card card) {
		this.cards.add(card);
	}

	public void removeCard(Card card) {
		this.cards.remove(card);
	}

	public Card removeOneRandomCard(){
		int size = cards.size();
		int lastCardPosition = size - 1;
		Card removedCard = null;
		if(size>0){
			this.shuffle(); // FIXME make this more performatic

			removedCard = cards.get(lastCardPosition);
			cards.remove(lastCardPosition);

			this.sort();
		}
		return removedCard;
	}

	public Card get(int position) {
		return this.getCards().get(position);
	}

	public int size() {
		return this.getCards().size();
	}

	private void shuffle(){
		Collections.shuffle(this.cards);
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

	@Override
	public String toString() {
		StringBuilder response = new StringBuilder();
		response.append("|");
		for (Card card : cards) {
			response.append(card.toString());
			response.append("|");
		}
		return response.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cards == null) ? 0 : cards.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Hand other = (Hand) obj;
		if (cards == null) {
			if (other.cards != null)
				return false;
		} else if (!cards.equals(other.cards))
			return false;
		return true;
	}

}