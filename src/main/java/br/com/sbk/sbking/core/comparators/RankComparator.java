package br.com.sbk.sbking.core.comparators;

import java.util.Comparator;

import br.com.sbk.sbking.core.Card;

public class RankComparator implements Comparator<Card> {

  @Override
  public int compare(Card card1, Card card2) {
    return card1.compareRank(card2);
  }

}
