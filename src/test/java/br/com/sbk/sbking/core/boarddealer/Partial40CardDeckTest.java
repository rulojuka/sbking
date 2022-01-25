package br.com.sbk.sbking.core.boarddealer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Rank;
import br.com.sbk.sbking.core.Suit;

public class Partial40CardDeckTest {

  private Partial40CardDeck subject;

  @Before
  public void setup() {
    this.subject = new Partial40CardDeck();
  }

  @Test
  public void getDeckshouldReturnADeckWith40DifferentCards() {
    Set<Card> set = new HashSet<Card>(subject.getDeck());
    int TOTAL_NUMBER_OF_CARDS = 40;
    assertEquals(TOTAL_NUMBER_OF_CARDS, set.size());
  }

  @Test
  public void getDeckshouldReturnADeckWithAllRanksGreaterThanFour() {
    Card fourOfHearts = new Card(Suit.HEARTS, Rank.FOUR);
    for (Card card : subject.getDeck()) {
      boolean isGreaterThanFour = card.compareRank(fourOfHearts) > 0;
      assertTrue(isGreaterThanFour);
    }
  }

}
