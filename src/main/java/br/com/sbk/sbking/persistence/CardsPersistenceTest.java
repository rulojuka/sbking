package br.com.sbk.sbking.persistence;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Rank;
import br.com.sbk.sbking.core.Suit;

public class CardsPersistenceTest {

	public static void main(String[] args) {

		CardDAO cardDAO = new CardDAO();
		cardDAO.saveAllCards();
		Card aceOfClubs = new Card(Suit.CLUBS, Rank.ACE);
		System.out.println("Achou: " + cardDAO.find(aceOfClubs));
	}
}
