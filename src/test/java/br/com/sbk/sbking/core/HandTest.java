package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;
import org.mockito.Mockito;

public class HandTest {

	@Test
	public void shouldBeConstructedEmpty() {
		Hand hand = new Hand();

		assertEquals(0, hand.size());
	}

	@Test
	public void shouldAddAndGetACard() {
		Hand hand = new Hand();

		Card card = Mockito.mock(Card.class);
		hand.addCard(card);

		assertEquals(1, hand.size());
		assertEquals(card, hand.get(0));
	}

	@Test
	public void shouldRemoveOnlyTheCorrectCard() {
		Hand hand = new Hand();

		Card firstCard = Mockito.mock(Card.class);
		Card secondCard = Mockito.mock(Card.class);
		hand.addCard(firstCard);
		hand.addCard(secondCard);

		hand.removeCard(firstCard);

		assertEquals(1, hand.size());
		assertEquals(secondCard, hand.get(0));
	}

	@Test
	public void shouldSortUsingCardInsideHandRules() {
		// The rules should be these:
		// The cards are ordered by suit, first Spades, then Hearts, then Clubs, then
		// Diamonds
		// Then, inside each suit, the cards are ordered by rank, so
		// Ace first, then King, ... , then Three, then Two

		Card kingOfSpades = mock(Card.class);
		Card queenOfSpades = mock(Card.class);
		Card aceOfHearts = mock(Card.class);
		Card kingOfHearts = mock(Card.class);

		when(kingOfSpades.compareSuit(queenOfSpades)).thenReturn(0);
		when(kingOfSpades.compareSuit(aceOfHearts)).thenReturn(1);
		when(kingOfSpades.compareSuit(kingOfHearts)).thenReturn(1);

		when(queenOfSpades.compareSuit(kingOfSpades)).thenReturn(0);
		when(queenOfSpades.compareSuit(aceOfHearts)).thenReturn(1);
		when(queenOfSpades.compareSuit(kingOfHearts)).thenReturn(1);

		when(aceOfHearts.compareSuit(kingOfSpades)).thenReturn(-1);
		when(aceOfHearts.compareSuit(queenOfSpades)).thenReturn(-1);
		when(aceOfHearts.compareSuit(kingOfHearts)).thenReturn(0);

		when(kingOfHearts.compareSuit(kingOfSpades)).thenReturn(-1);
		when(kingOfHearts.compareSuit(queenOfSpades)).thenReturn(-1);
		when(kingOfHearts.compareSuit(aceOfHearts)).thenReturn(0);

		when(kingOfSpades.compareRank(queenOfSpades)).thenReturn(1);
		when(queenOfSpades.compareRank(kingOfSpades)).thenReturn(-1);
		when(aceOfHearts.compareRank(kingOfHearts)).thenReturn(1);
		when(kingOfHearts.compareRank(aceOfHearts)).thenReturn(-1);

		ArrayList<Card> listOfCards = new ArrayList<Card>();
		listOfCards.add(kingOfSpades);
		listOfCards.add(queenOfSpades);
		listOfCards.add(aceOfHearts);
		listOfCards.add(kingOfHearts);
		Collections.shuffle(listOfCards);

		Hand hand = new Hand();
		for (Card card : listOfCards) {
			hand.addCard(card);
		}

		hand.sort();

		assertEquals(kingOfSpades, hand.get(0));
		assertEquals(queenOfSpades, hand.get(1));
		assertEquals(aceOfHearts, hand.get(2));
		assertEquals(kingOfHearts, hand.get(3));

		verify(kingOfSpades, never()).compareRank(aceOfHearts);
		verify(kingOfSpades, never()).compareRank(kingOfHearts);

		verify(queenOfSpades, never()).compareRank(aceOfHearts);
		verify(queenOfSpades, never()).compareRank(kingOfHearts);

		verify(aceOfHearts, never()).compareRank(kingOfSpades);
		verify(aceOfHearts, never()).compareRank(queenOfSpades);

		verify(kingOfHearts, never()).compareRank(kingOfSpades);
		verify(kingOfHearts, never()).compareRank(queenOfSpades);
	}

	@Test
	public void shouldReturnIfItContainsACard() {
		Hand hand = new Hand();

		Card firstCard = Mockito.mock(Card.class);
		Card secondCard = Mockito.mock(Card.class);
		hand.addCard(firstCard);

		assertTrue(hand.containsCard(firstCard));
		assertFalse(hand.containsCard(secondCard));
	}

	@Test
	public void shouldReturnIfItHasASuit() {
		Suit spades = Suit.SPADES;
		Suit hearts = Suit.HEARTS;
		Suit clubs = Suit.CLUBS;
		Suit diamonds = Suit.DIAMONDS;

		Card aceOfSpades = mock(Card.class);
		Card kingOfHearts = mock(Card.class);
		when(aceOfSpades.getSuit()).thenReturn(spades);
		when(kingOfHearts.getSuit()).thenReturn(hearts);

		Hand hand = new Hand();

		hand.addCard(aceOfSpades);
		hand.addCard(kingOfHearts);

		assertTrue(hand.hasSuit(spades));
		assertTrue(hand.hasSuit(hearts));
		assertFalse(hand.hasSuit(clubs));
		assertFalse(hand.hasSuit(diamonds));
	}

	@Test
	public void shouldReturnIfItOnlyHasHearts() {
		Card aceOfSpades = mock(Card.class);
		when(aceOfSpades.isHeart()).thenReturn(false);
		Card kingOfHearts = mock(Card.class);
		when(kingOfHearts.isHeart()).thenReturn(true);
		Hand hand = new Hand();
		hand.addCard(kingOfHearts);
		assertTrue(hand.onlyHasHearts());
		hand.addCard(aceOfSpades);
		assertFalse(hand.onlyHasHearts());
	}

	@Test
	public void shouldTransformToStringByPipeSeparatingCards() {
		Card aceOfSpades = mock(Card.class);
		String aceOfSpadesString = "sA";
		when(aceOfSpades.toString()).thenReturn(aceOfSpadesString);
		Card kingOfHearts = mock(Card.class);
		String kingOfHeartsString = "hK";
		when(kingOfHearts.toString()).thenReturn(kingOfHeartsString);
		Hand hand = new Hand();
		hand.addCard(aceOfSpades);
		hand.addCard(kingOfHearts);
		String finalString = "|sA|hK|";

		assertEquals(finalString, hand.toString());
	}

}
