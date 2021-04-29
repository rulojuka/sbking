package br.com.sbk.sbking.core.comparators;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Rank;
import br.com.sbk.sbking.core.Suit;

public class CardInsideHandWithSuitComparatorTest {

  private Card aceOfClubs = new Card(Suit.CLUBS, Rank.ACE);
  private Card aceOfDiamonds = new Card(Suit.DIAMONDS, Rank.ACE);
  private Card aceOfHearts = new Card(Suit.HEARTS, Rank.ACE);
  private Card aceOfSpades = new Card(Suit.SPADES, Rank.ACE);

  @Test
  public void shouldCompareSuitsCorrectlyWithClubsAsTrumpSuit() {
    CardInsideHandWithSuitComparator cardInsideHandWithSuitComparator = new CardInsideHandWithSuitComparator(
        Suit.CLUBS);

    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfClubs, aceOfClubs) == 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfDiamonds, aceOfDiamonds) == 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfHearts) == 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfSpades) == 0);

    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfClubs, aceOfHearts) < 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfClubs, aceOfSpades) < 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfClubs, aceOfDiamonds) < 0);

    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfSpades) < 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfDiamonds) < 0);

    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfDiamonds) < 0);

  }

  @Test
  public void shouldCompareSuitsCorrectlyWithDiamondsAsTrumpSuit() {
    CardInsideHandWithSuitComparator cardInsideHandWithSuitComparator = new CardInsideHandWithSuitComparator(
        Suit.DIAMONDS);

    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfClubs, aceOfClubs) == 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfDiamonds, aceOfDiamonds) == 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfHearts) == 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfSpades) == 0);

    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfDiamonds, aceOfSpades) < 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfDiamonds, aceOfHearts) < 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfDiamonds, aceOfClubs) < 0);

    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfHearts) < 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfClubs) < 0);

    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfClubs) < 0);

  }

  @Test
  public void shouldCompareSuitsCorrectlyWithHeartsAsTrumpSuit() {
    CardInsideHandWithSuitComparator cardInsideHandWithSuitComparator = new CardInsideHandWithSuitComparator(
        Suit.HEARTS);

    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfClubs, aceOfClubs) == 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfDiamonds, aceOfDiamonds) == 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfHearts) == 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfSpades) == 0);

    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfSpades) < 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfDiamonds) < 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfClubs) < 0);

    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfDiamonds) < 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfClubs) < 0);

    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfDiamonds, aceOfClubs) < 0);

  }

  @Test
  public void shouldCompareSuitsCorrectlyWithSpadesAsTrumpSuit() {
    CardInsideHandWithSuitComparator cardInsideHandWithSuitComparator = new CardInsideHandWithSuitComparator(
        Suit.SPADES);

    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfClubs, aceOfClubs) == 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfDiamonds, aceOfDiamonds) == 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfHearts) == 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfSpades) == 0);

    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfHearts) < 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfClubs) < 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfSpades, aceOfDiamonds) < 0);

    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfClubs) < 0);
    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfHearts, aceOfDiamonds) < 0);

    assertTrue(cardInsideHandWithSuitComparator.compare(aceOfClubs, aceOfDiamonds) < 0);

  }

}
