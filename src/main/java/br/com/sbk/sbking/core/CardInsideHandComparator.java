package br.com.sbk.sbking.core;

import java.util.Comparator;

public class CardInsideHandComparator implements Comparator<Card> {

	@Override
	public int compare(Card card1, Card card2) {

		int suitDiff = card1.getSuit().compareTo(card2.getSuit());
		int rankDiff = card1.getRank().compareTo(card2.getRank());

		if (suitDiff != 0)
			return -suitDiff;
		else
			return -rankDiff;
	}

}
