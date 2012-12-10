package nu.danielsundberg.yakutia.entity;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.List;

public class TankTest {


    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static EntityTransaction tx;

    @BeforeClass
    public static void initEntityManager() throws Exception {
        emf = Persistence.createEntityManagerFactory("testPU");
        em = emf.createEntityManager();
    }

    @AfterClass
    public static void closeEntityManager() {
        em.close();
        emf.close();
    }

    @Before
    public void initTransaction() {
        tx = em.getTransaction();
    }

    @Test
    public void createTank() {
        Tank tank = new Tank();
        tank.setHealth(100);

        tx.begin();
        em.persist(tank);
        tx.commit();

        List<Tank> tanks = (List<Tank>)
                em.createNamedQuery("findAllTanks").getResultList();

         assertEquals(1,tanks.size());
    }

}
