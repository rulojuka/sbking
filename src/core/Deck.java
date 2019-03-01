package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
	private List<Card> deck;
	private int index;
	private static final int DECK_SIZE = 52;

	public Deck() {
		deck = new ArrayList<Card>();
		index = 0;
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				Card card = new Card(suit, rank);
				deck.add(card);
			}
		}
	}

	public Card dealCard() {
		if (index >= DECK_SIZE)
			return null;
		else
			return deck.get(index++);
	}

	public void shuffle() {
		Collections.shuffle(deck);
	}

	public void print() {
		index = 0;
		Card card;
		while ((card = this.dealCard()) != null) {
			System.out.print(card.toString() + "\n");
		}
	}

	public void restore() {
		index = 0;
	}

}
