package br.com.sbk.sbking.core;

import java.util.Comparator;

public class CardOfSameSuitComparator implements Comparator<Card> {

	@Override
	public int compare(Card card1, Card card2) {
		return card1.compareRank(card2);
	}

}
