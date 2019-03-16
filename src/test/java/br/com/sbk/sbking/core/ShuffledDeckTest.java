package br.com.sbk.sbking.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ShuffledDeckTest {

	private final static int DECK_SIZE = Suit.values().length * Rank.values().length;

	@Test
	public void constructorShouldReturnADeckWithAllCards() {
		ShuffledDeck shuffledDeck = new ShuffledDeck();

		Set<Card> setOfCards = new HashSet<Card>();
		for (int i = 0; i < DECK_SIZE; i++) {
			Card card = shuffledDeck.dealCard();
			assertFalse(setOfCards.contains(card));
			setOfCards.add(card);
		}
		assertTrue(setOfCards.size() == DECK_SIZE);
	}

	@Test
	public void constructorShouldReturnAShuffledDeck() {
		// This test fails 1/(52!) of the times :)
		ShuffledDeck firstShuffledDeck = new ShuffledDeck();
		ShuffledDeck secondShuffledDeck = new ShuffledDeck();
		int equalCardCounter = 0;

		for (int i = 0; i < DECK_SIZE; i++) {
			Card cardOfFirstDeck = firstShuffledDeck.dealCard();
			Card cardOfSecondDeck = secondShuffledDeck.dealCard();
			if (cardOfFirstDeck.equals(cardOfSecondDeck)) {
				equalCardCounter++;
			}
		}
		assertTrue(equalCardCounter < 52);

	}

}
