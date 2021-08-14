package br.com.sbk.sbking.core.boarddealer;

import java.util.ArrayDeque;
import java.util.Deque;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Rank;
import br.com.sbk.sbking.core.Suit;

public class Complete52CardDeck {

  private Deque<Card> deck;

  public Complete52CardDeck() {
    this.deck = new ArrayDeque<Card>();
    for (Suit suit : Suit.values()) {
      for (Rank rank : Rank.values()) {
        Card card = new Card(suit, rank);
        this.deck.add(card);
      }
    }
  }

  public Deque<Card> getDeck() {
    return this.deck;
  }

}
