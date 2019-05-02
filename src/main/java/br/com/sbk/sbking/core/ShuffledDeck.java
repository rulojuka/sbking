package br.com.sbk.sbking.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import br.com.sbk.sbking.core.exceptions.DealingCardFromAnEmptyDeckException;

public class ShuffledDeck {
	private List<Card> deck; // List is best because we need to shuffle it
	private Iterator<Card> iterator;

	public ShuffledDeck() {
		this.deck = new ArrayList<Card>();
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				Card card = new Card(suit, rank);
				this.deck.add(card);
			}
		}
		Collections.shuffle(this.deck);
		iterator = this.deck.iterator();
	}

	public Card dealCard() {
		if (iterator.hasNext())
			return iterator.next();
		throw new DealingCardFromAnEmptyDeckException();
	}

	public boolean hasCard() {
		return iterator.hasNext();
	}

}
