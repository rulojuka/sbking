package br.com.sbk.sbking.core.comparators;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import br.com.sbk.sbking.core.Suit;

public class BridgeSuitComparator implements Comparator<Suit> {

  private static Map<Suit, Integer> suitRank;

  static {
    suitRank = new HashMap<Suit, Integer>();
    suitRank.put(Suit.SPADES, 4);
    suitRank.put(Suit.HEARTS, 3);
    suitRank.put(Suit.DIAMONDS, 2);
    suitRank.put(Suit.CLUBS, 1);
  }

  @Override
  public int compare(Suit suit1, Suit suit2) {
    return suitRank.get(suit1) - suitRank.get(suit2);
  }
}
