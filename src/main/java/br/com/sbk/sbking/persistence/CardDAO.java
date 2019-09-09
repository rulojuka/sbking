package br.com.sbk.sbking.persistence;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import br.com.sbk.sbking.core.Card;
import br.com.sbk.sbking.core.Rank;
import br.com.sbk.sbking.core.Suit;

public class CardDAO {

	private SessionFactory factory;

	public CardDAO() {
		this.factory = new Configuration().configure().addAnnotatedClass(Card.class).buildSessionFactory();
	}

	public void saveAllCards() {
		Session session = factory.openSession();

		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				Transaction transaction = session.beginTransaction();
				Card card = new Card(suit, rank);
				if (find(card) == null) {
					session.save(card);
				}
				transaction.commit();
			}
		}
	}

	public Card find(Card card) {
		Session session = factory.openSession();
		Card cardFromDatabase = null;

		try {
			cardFromDatabase = session
					.createQuery("select c from Card c where c.rank = :rank AND c.suit = :suit", Card.class)
					.setParameter("rank", card.getRank()).setParameter("suit", card.getSuit()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

		return cardFromDatabase;
	}
}
