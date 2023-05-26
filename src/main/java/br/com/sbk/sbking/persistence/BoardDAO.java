package br.com.sbk.sbking.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import jakarta.persistence.NoResultException;

public class BoardDAO {

    private SessionFactory factory;

    public BoardDAO() {
        Configuration config = new Configuration();
        config.configure();
        this.factory = config
                .addAnnotatedClass(BoardEntity.class)
                .buildSessionFactory();
    }

    public void saveBoard(BoardEntity board) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();

            String pavlicekNumber = board.getPavlicekNumber();
            // System.out.println("Checking number:" + pavlicekNumber);
            if (findByPavlicekNumber(pavlicekNumber) == null) {
                session.persist(board);
                // System.out.println("Does not exist on DB. Persisting it.");
            }

            session.getTransaction().commit();
        }
    }

    public BoardEntity findByPavlicekNumber(String pavlicekNumber) {
        try (Session session = factory.openSession()) {
            return session.createQuery("from Board b where b.pavlicekNumber = :pav", BoardEntity.class)
                    .setParameter("pav", pavlicekNumber)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
