package br.com.sbk.sbking.core.boarddealer;

import java.util.ArrayDeque;
import java.util.Deque;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Rank;
import br.com.sbk.sbking.core.Suit;

public class Partial40CardDeck implements CardDeck {

  private Deque<Card> deck;

  public Partial40CardDeck() {
    this.deck = new ArrayDeque<Card>();
    for (Suit suit : Suit.values()) {
      for (Rank rank : Rank.values()) {
        if (rank.compareTo(Rank.FIVE) >= 0) { // Rank is greater or equal than five
          Card card = new Card(suit, rank);
          this.deck.add(card);
        }
      }
    }
  }

  public Deque<Card> getDeck() {
    return this.deck;
  }

}
