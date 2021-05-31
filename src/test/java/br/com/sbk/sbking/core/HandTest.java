package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void shouldTransformToStringBySpaceSeparatingSuits() {
        Card aceOfSpades = mock(Card.class);
        when(aceOfSpades.getRank()).thenReturn(Rank.ACE);
        when(aceOfSpades.getSuit()).thenReturn(Suit.SPADES);
        Card kingOfHearts = mock(Card.class);
        when(kingOfHearts.getRank()).thenReturn(Rank.KING);
        when(kingOfHearts.getSuit()).thenReturn(Suit.HEARTS);
        Hand hand = new Hand();
        hand.addCard(aceOfSpades);
        hand.addCard(kingOfHearts);
        String finalString = "A K";

        assertEquals(finalString, hand.toString());
    }

}
