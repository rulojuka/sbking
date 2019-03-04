package br.com.sbk.sbking.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Deck {
	private List<Card> deck; // List is best because we need to shuffle it
	private static final int DECK_SIZE = 52;
	private Iterator<Card> iterator;

	public Deck() {
		deck = new ArrayList<Card>(DECK_SIZE);
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				Card card = new Card(suit, rank);
				deck.add(card);
			}
		}
		iterator = deck.iterator();
	}

	public Card dealCard() {
		if (iterator.hasNext())
			return iterator.next();
		throw new RuntimeException("Trying to deal card from am empty deck.");
	}

	public void shuffle() {
		Collections.shuffle(deck);
		this.iterator = deck.iterator();
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		Iterator<Card> iterator = deck.iterator();
		while (iterator.hasNext()) {
			stringBuilder.append(iterator.next().toString() + "\n");
		}
		return stringBuilder.toString();
	}

}
