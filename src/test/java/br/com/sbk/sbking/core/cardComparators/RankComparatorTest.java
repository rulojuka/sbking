package br.com.sbk.sbking.core.cardComparators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Rank;
import br.com.sbk.sbking.core.Suit;

public class RankComparatorTest {

  private Card aceOfClubs = new Card(Suit.CLUBS, Rank.ACE);
  private Card aceOfDiamonds = new Card(Suit.DIAMONDS, Rank.ACE);
  private Card aceOfHearts = new Card(Suit.HEARTS, Rank.ACE);
  private Card aceOfSpades = new Card(Suit.SPADES, Rank.ACE);

  private Card kingOfClubs = new Card(Suit.CLUBS, Rank.KING);
  private Card tenOfDiamonds = new Card(Suit.DIAMONDS, Rank.TEN);
  private Card sevenOfHearts = new Card(Suit.HEARTS, Rank.SEVEN);
  private Card twoOfSpades = new Card(Suit.SPADES, Rank.TWO);

  @Test
  public void shouldCompareRanksCorrectlyInsideSuit() {
    RankComparator rankComparator = new RankComparator();

    assertTrue(rankComparator.compare(kingOfClubs, aceOfClubs) < 0);
    assertTrue(rankComparator.compare(tenOfDiamonds, aceOfDiamonds) < 0);
    assertTrue(rankComparator.compare(sevenOfHearts, aceOfHearts) < 0);
    assertTrue(rankComparator.compare(twoOfSpades, aceOfSpades) < 0);

    assertEquals(0, rankComparator.compare(kingOfClubs, kingOfClubs));
    assertEquals(0, rankComparator.compare(tenOfDiamonds, tenOfDiamonds));
    assertEquals(0, rankComparator.compare(sevenOfHearts, sevenOfHearts));
    assertEquals(0, rankComparator.compare(twoOfSpades, twoOfSpades));

  }

  @Test
  public void shouldIgnoreSuit() {
    RankComparator rankComparator = new RankComparator();

    assertEquals(rankComparator.compare(kingOfClubs, aceOfClubs), rankComparator.compare(kingOfClubs, aceOfDiamonds));
    assertEquals(0, rankComparator.compare(aceOfClubs, aceOfDiamonds));

  }

}
