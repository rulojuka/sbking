package br.com.sbk.sbking.core.boarddealer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Rank;
import br.com.sbk.sbking.core.Suit;

public class Partial32CardDeckTest {

  private Partial32CardDeck subject;

  @BeforeEach
  public void setup() {
    this.subject = new Partial32CardDeck();
  }

  @Test
  public void getDeckshouldReturnADeckWith32DifferentCards() {
    Set<Card> set = new HashSet<Card>(subject.getDeck());
    int TOTAL_NUMBER_OF_CARDS = 32;
    assertEquals(TOTAL_NUMBER_OF_CARDS, set.size());
  }

  @Test
  public void getDeckshouldReturnADeckWithAllRanksGreaterThanSix() {
    Card sixOfHearts = new Card(Suit.HEARTS, Rank.SIX);
    for (Card card : subject.getDeck()) {
      boolean isGreaterThanSix = card.compareRank(sixOfHearts) > 0;
      assertTrue(isGreaterThanSix);
    }
  }

}
