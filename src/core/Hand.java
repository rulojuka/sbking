package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hand {

	private List<Card> listOfCards = new ArrayList<Card>();
	private Direction owner;

	public void addCard(Card card) {
		listOfCards.add(card);
	}

	public Card getCard(int index) {
		return listOfCards.get(index);
	}

	public void removeCard(Card card) {
		listOfCards.remove(card);
	}

	public void discard() {
		listOfCards.clear();
	}

	public int getNumberOfCards() {
		return listOfCards.size();
	}

	public void sort() {
		Collections.sort(listOfCards);
	}

	public boolean isEmpty() {
		return listOfCards.isEmpty();
	}

	public boolean containsCard(Card card) {
		return this.listOfCards.contains(card);
	}

	public String toString() {
		return listOfCards.toString();
	}

	public void setOwner(Direction direction) {
		owner = direction;
	}

	public boolean hasSuit(Suit suit) {
		int n = getNumberOfCards();
		for (int i = 0; i < n; i++) {
			if (this.getCard(i).getSuit() == suit)
				return true;
		}
		return false;
	}

	public Direction getOwner() {
		return owner;
	}

	public void print() {
		int i;
		int n;
		Card auxCard;
		Rank auxRank;
		Suit auxSuit;

		n = getNumberOfCards();
		for (i = 0; i < n; i++) {
			auxCard = listOfCards.get(i);
			auxRank = auxCard.getRank();
			auxSuit = auxCard.getSuit();
			if (i != 0)
				System.out.print(" | ");
			System.out.print(auxSuit.getSymbol() + auxRank.getSymbol());
		}
		System.out.println();
	}

	public int HCP() {
		int i, n;
		int resp = 0;
		Card auxCard;
		n = getNumberOfCards();
		for (i = 0; i < n; i++) {
			auxCard = listOfCards.get(i);
			resp += auxCard.points();
		}
		return resp;
	}

	public List<Card> getListOfCards() {
		return this.listOfCards;
	}

}