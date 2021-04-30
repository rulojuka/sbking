package br.com.sbk.sbking.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class HandEvaluationsTest {

  @Mock
  private Card aceOfSpades;
  @Mock
  private Card kingOfHearts;
  @Mock
  private Card threeOfClubs;
  @Mock
  private Card sevenOfDiamonds;
  @Mock
  private Hand emptyHand;

  private HandEvaluations emptyHandEvaluations;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    when(aceOfSpades.getRank()).thenReturn(Rank.ACE);
    when(aceOfSpades.getSuit()).thenReturn(Suit.SPADES);

    when(kingOfHearts.getRank()).thenReturn(Rank.KING);
    when(kingOfHearts.getSuit()).thenReturn(Suit.HEARTS);

    when(threeOfClubs.getRank()).thenReturn(Rank.THREE);
    when(threeOfClubs.getSuit()).thenReturn(Suit.CLUBS);

    when(sevenOfDiamonds.getRank()).thenReturn(Rank.SEVEN);
    when(sevenOfDiamonds.getSuit()).thenReturn(Suit.DIAMONDS);

    when(emptyHand.getCards()).thenReturn(new ArrayList<Card>());
    emptyHandEvaluations = new HandEvaluations(emptyHand);
  }

  @Test
  public void shouldReturnTheCorrectHCP() {
    int acePoints = 4;
    int kingPoints = 3;
    int totalPoints = acePoints + kingPoints;
    when(aceOfSpades.getPoints()).thenReturn(acePoints);
    when(kingOfHearts.getPoints()).thenReturn(kingPoints);

    Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 1, 0, 0));
    Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(1, 1, 0, 0));

    HandEvaluations onlyKingHandEvaluations = new HandEvaluations(firstHand);
    HandEvaluations aceAndKingHandEvaluations = new HandEvaluations(secondHand);

    assertEquals(kingPoints, onlyKingHandEvaluations.getHCP());
    assertEquals(totalPoints, aceAndKingHandEvaluations.getHCP());
    assertEquals(0, emptyHandEvaluations.getHCP());
  }

  @Test
  public void shouldReturnTheCorrectShortestSuitLength() {

    Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(1, 1, 0, 1));
    Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(1, 1, 1, 1));

    HandEvaluations onlyThreeSuitsHandEvaluations = new HandEvaluations(firstHand);
    HandEvaluations allSuitsHandEvaluations = new HandEvaluations(secondHand);

    assertEquals(0, emptyHandEvaluations.getShortestSuitLength());
    assertEquals(0, onlyThreeSuitsHandEvaluations.getShortestSuitLength());
    assertEquals(1, allSuitsHandEvaluations.getShortestSuitLength());
  }

  @Test
  public void shouldReturnTheCorrectLongestSuitLength() {

    Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(1, 1, 0, 1));

    int numberOfHearts = 4;
    Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(1, numberOfHearts, 1, 1));

    HandEvaluations threeSuitsWithOneCardHandEvaluations = new HandEvaluations(firstHand);
    HandEvaluations manyHeartsHandEvaluations = new HandEvaluations(secondHand);

    assertEquals(0, emptyHandEvaluations.getLongestSuitLength());
    assertEquals(1, threeSuitsWithOneCardHandEvaluations.getLongestSuitLength());
    assertEquals(numberOfHearts, manyHeartsHandEvaluations.getLongestSuitLength());
  }

  @Test
  public void shouldReturnTheCorrectNumberOfDoubletons() {

    Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 0, 0, 2));
    Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(2, 2, 0, 2));

    HandEvaluations oneDoubletonHandEvaluations = new HandEvaluations(firstHand);
    HandEvaluations threeDoubletonsHandEvaluations = new HandEvaluations(secondHand);

    assertEquals(0, emptyHandEvaluations.getNumberOfDoubletonSuits());
    assertEquals(1, oneDoubletonHandEvaluations.getNumberOfDoubletonSuits());
    assertEquals(3, threeDoubletonsHandEvaluations.getNumberOfDoubletonSuits());
  }

  @Test
  public void shouldReturnIfItOnlyHasHearts() {
    when(aceOfSpades.isHeart()).thenReturn(false);
    when(kingOfHearts.isHeart()).thenReturn(true);

    Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 1, 0, 0));
    Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(1, 1, 0, 0));

    HandEvaluations onlyHeartsHandEvaluations = new HandEvaluations(firstHand);
    HandEvaluations notOnlyHeartsHandEvaluations = new HandEvaluations(secondHand);

    assertTrue(onlyHeartsHandEvaluations.onlyHasHearts());
    assertFalse(notOnlyHeartsHandEvaluations.onlyHasHearts());
  }

  @Test
  public void shouldReturnIfItHasFiveOrMoreCardsInAMajorSuit() {
    Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 0, 0, 5));
    Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 5, 0, 5));
    Hand thirdHand = this.createMockedHandWithDistribution(this.createSuitDistribution(7, 0, 0, 5));

    HandEvaluations fiveCardClubsHandEvaluations = new HandEvaluations(firstHand);
    HandEvaluations fiveCardClubsAndHeartsHandEvaluations = new HandEvaluations(secondHand);
    HandEvaluations fiveCardClubsAndSevenSpadesHandEvaluations = new HandEvaluations(thirdHand);

    assertFalse(emptyHandEvaluations.hasFiveOrMoreCardsInAMajorSuit());
    assertFalse(fiveCardClubsHandEvaluations.hasFiveOrMoreCardsInAMajorSuit());
    assertTrue(fiveCardClubsAndHeartsHandEvaluations.hasFiveOrMoreCardsInAMajorSuit());
    assertTrue(fiveCardClubsAndSevenSpadesHandEvaluations.hasFiveOrMoreCardsInAMajorSuit());
  }

  @Test
  public void shouldReturnIfItHasThreeOrMoreCardsInAMinorSuit() {
    Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 0, 0, 3));
    Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(0, 5, 3, 5));
    Hand thirdHand = this.createMockedHandWithDistribution(this.createSuitDistribution(7, 0, 0, 2));

    HandEvaluations threeClubCardHandEvaluations = new HandEvaluations(firstHand);
    HandEvaluations threeClubCardAndFiveHeartsHandEvaluations = new HandEvaluations(secondHand);
    HandEvaluations twoClubCardHandEvaluations = new HandEvaluations(thirdHand);

    assertFalse(emptyHandEvaluations.hasThreeOrMoreCardsInAMinorSuit());
    assertTrue(threeClubCardHandEvaluations.hasThreeOrMoreCardsInAMinorSuit());
    assertTrue(threeClubCardAndFiveHeartsHandEvaluations.hasThreeOrMoreCardsInAMinorSuit());
    assertFalse(twoClubCardHandEvaluations.hasThreeOrMoreCardsInAMinorSuit());
  }

  @Test
  public void shouldReturnIfItIsBalanced() {
    Hand firstHand = this.createMockedHandWithDistribution(this.createSuitDistribution(4, 4, 5, 0));
    Hand secondHand = this.createMockedHandWithDistribution(this.createSuitDistribution(4, 1, 4, 4));
    Hand thirdHand = this.createMockedHandWithDistribution(this.createSuitDistribution(2, 2, 3, 6));
    Hand fourthHand = this.createMockedHandWithDistribution(this.createSuitDistribution(2, 3, 3, 5));
    Hand fifthHand = this.createMockedHandWithDistribution(this.createSuitDistribution(2, 2, 4, 5));

    HandEvaluations voidClubsHandEvaluations = new HandEvaluations(firstHand);
    HandEvaluations singletonHeartsHandEvaluations = new HandEvaluations(secondHand);
    HandEvaluations sixClubCardsHandEvaluations = new HandEvaluations(thirdHand);
    HandEvaluations fiveThreeTwoTwoHandEvaluations = new HandEvaluations(fourthHand);
    HandEvaluations twoDoubletonsHandEvaluations = new HandEvaluations(fifthHand);

    assertFalse(emptyHandEvaluations.isBalanced());
    assertFalse(voidClubsHandEvaluations.isBalanced());
    assertFalse(singletonHeartsHandEvaluations.isBalanced());
    assertFalse(sixClubCardsHandEvaluations.isBalanced());
    assertTrue(fiveThreeTwoTwoHandEvaluations.isBalanced());
    assertFalse(twoDoubletonsHandEvaluations.isBalanced());
  }

  private Hand createMockedHandWithDistribution(Map<Suit, Integer> suitDistribution) {
    Hand hand = mock(Hand.class);
    List<Card> mockedCards = new ArrayList<Card>();
    Map<Suit, Card> mockedCardOfSuit = new HashMap<Suit, Card>();
    mockedCardOfSuit.put(Suit.SPADES, aceOfSpades);
    mockedCardOfSuit.put(Suit.HEARTS, kingOfHearts);
    mockedCardOfSuit.put(Suit.DIAMONDS, sevenOfDiamonds);
    mockedCardOfSuit.put(Suit.CLUBS, threeOfClubs);

    for (Suit suit : Suit.values()) {
      Integer numberOfCards = suitDistribution.get(suit);
      if (numberOfCards == null) {
        continue;
      }
      for (int i = 0; i < numberOfCards; i++) {
        mockedCards.add(mockedCardOfSuit.get(suit));
      }
    }

    when(hand.getCards()).thenReturn(mockedCards);
    return hand;
  }

  private Map<Suit, Integer> createSuitDistribution(int spades, int hearts, int diamonds, int clubs) {
    Map<Suit, Integer> suitDistribution = new HashMap<Suit, Integer>();
    suitDistribution.put(Suit.SPADES, spades);
    suitDistribution.put(Suit.HEARTS, hearts);
    suitDistribution.put(Suit.DIAMONDS, diamonds);
    suitDistribution.put(Suit.CLUBS, clubs);
    return suitDistribution;
  }
}
