package br.com.sbk.sbking.core.rulesets.interfaces;

import java.util.Comparator;

import br.com.sbk.sbking.core.Card;

public interface CardComparable {

  Comparator<Card> getComparator();

}
