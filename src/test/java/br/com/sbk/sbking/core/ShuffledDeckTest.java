package br.com.sbk.sbking.core;

import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ShuffledDeckTest {

	@Test
	public void constructorShouldReturnADeckWithAllCardsAndAlreadyShuffled() {
		ShuffledDeck shuffledDeck = new ShuffledDeck();
		Set<Card> setOfCards = new HashSet<Card>();
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				Card card = shuffledDeck.dealCard();
				setOfCards.add(card);
			}
		}
		int deckSize = Suit.values().length * Rank.values().length;
		assertTrue(setOfCards.size() == deckSize);
	}

}
