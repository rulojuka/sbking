package br.com.sbk.sbking.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import br.com.sbk.sbking.core.boarddealer.Complete52CardDeck;
import br.com.sbk.sbking.core.exceptions.DealingCardFromAnEmptyDeckException;

public class ShuffledDeckTest {

    private static final int DECK_SIZE = Suit.values().length * Rank.values().length;
    private static Deque<Card> gameDeck;

    @BeforeClass
    public static void setup() {
        Complete52CardDeck complete52CardDeck = new Complete52CardDeck();
        gameDeck = complete52CardDeck.getDeck();
    }

    @Test
    public void constructorShouldReturnADeckWithAllCards() {
        ShuffledDeck shuffledDeck = new ShuffledDeck(gameDeck);

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
        // This test fails 1/(52!) of the time :)
        ShuffledDeck firstShuffledDeck = new ShuffledDeck(gameDeck);
        ShuffledDeck secondShuffledDeck = new ShuffledDeck(gameDeck);
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

    @Test(expected = DealingCardFromAnEmptyDeckException.class)
    public void shouldThrowExceptionWhenDealingCardFromAnEmptyDeck() {
        ShuffledDeck shuffledDeck = new ShuffledDeck(gameDeck);
        for (int i = 0; i < DECK_SIZE; i++) {
            shuffledDeck.dealCard();
        }

        shuffledDeck.dealCard();
    }

}
