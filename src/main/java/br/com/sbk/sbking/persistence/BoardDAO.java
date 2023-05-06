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
        Session session = factory.openSession();
        session.beginTransaction();

        String pavlicekNumber = board.getPavlicekNumber();
        System.out.println("Checking number:" + pavlicekNumber);
        if (findByPavlicekNumber(pavlicekNumber) == null) {
            session.persist(board);
            System.out.println("Does not exist on DB. Persisting it.");
        }

        session.getTransaction().commit();
        session.close();

    }

    public BoardEntity findByPavlicekNumber(String pavlicekNumber) {
        Session session = factory.openSession();

        try {
            BoardEntity returnEntity = session
                    .createQuery("from Board b where b.pavlicekNumber = :pav",
                            BoardEntity.class)
                    .setParameter("pav", pavlicekNumber)
                    .getSingleResult();
            return returnEntity;
        } catch (NoResultException e) {
            return null;
        }
    }

}
