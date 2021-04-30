package br.com.sbk.sbking.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class HandEvaluations {

  private final List<Card> cards;

  public HandEvaluations(Hand hand) {
    List<Card> modifiableCards = new ArrayList<Card>();
    for (Card card : hand.getCards()) {
      modifiableCards.add(card);
    }
    this.cards = Collections.unmodifiableList(modifiableCards);
  }

  private List<Card> getCards() {
    return this.cards;
  }

  private Map<Suit, Integer> getCardsPerSuit() {
    Map<Suit, Integer> numberOfCards = new HashMap<Suit, Integer>();
    for (Suit suit : Suit.values()) {
      numberOfCards.put(suit, 0);
    }
    for (Card card : this.getCards()) {
      Suit currentSuit = card.getSuit();
      int currentValue = numberOfCards.get(currentSuit);
      currentValue++;
      numberOfCards.put(currentSuit, currentValue);
    }
    return numberOfCards;
  }

  public int getHCP() {
    return this.getCards().stream().map(Card::getPoints).reduce(0, Math::addExact);
  }

  public int getShortestSuitLength() {
    return this.getCardsPerSuit().values().stream().reduce(Math::min).orElse(0);
  }

  public int getLongestSuitLength() {
    return this.getCardsPerSuit().values().stream().reduce(Math::max).orElse(0);
  }

  public int getNumberOfDoubletonSuits() {
    return this.getCardsPerSuit().values().stream().reduce(0, (subtotal, element) -> {
      if (element == 2) {
        return subtotal + 1;
      } else {
        return subtotal;
      }
    });
  }

  public boolean onlyHasHearts() {
    return this.getCards().stream().map(Card::isHeart).reduce(true, Boolean::logicalAnd);
  }

  public boolean hasFiveOrMoreCardsInAMajorSuit() {
    int longestMajor = this.getCardsPerSuit().entrySet().stream().filter(this::isMajorSuit).map(Entry::getValue)
        .reduce(0, Math::max);
    return longestMajor >= 5;
  }

  public boolean hasThreeOrMoreCardsInAMinorSuit() {
    int longestMinor = this.getCardsPerSuit().entrySet().stream().filter(this::isMinorSuit).map(Entry::getValue)
        .reduce(0, Math::max);
    return longestMinor >= 3;
  }

  private boolean isMajorSuit(Map.Entry<Suit, Integer> entry) {
    Suit suit = entry.getKey();
    return Suit.SPADES.equals(suit) || Suit.HEARTS.equals(suit);
  }

  private boolean isMinorSuit(Map.Entry<Suit, Integer> entry) {
    Suit suit = entry.getKey();
    return Suit.DIAMONDS.equals(suit) || Suit.CLUBS.equals(suit);
  }

  public boolean isBalanced() {
    return this.getShortestSuitLength() >= 2 && this.getLongestSuitLength() <= 5
        && this.getNumberOfDoubletonSuits() <= 1;
  }

}
