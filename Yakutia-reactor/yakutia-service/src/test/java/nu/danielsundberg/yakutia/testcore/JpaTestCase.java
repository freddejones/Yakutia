package nu.danielsundberg.yakutia.testcore;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;

public abstract class JpaTestCase {

    private static EntityManagerFactory entityManagerFactory;
    private static Connection connection;
    protected static EntityManager entityManager;

    @BeforeClass
    public static void setUpDataBase() throws Exception {
        Class.forName("org.hsqldb.jdbcDriver");
        connection = DriverManager.getConnection("jdbc:hsqldb:mem:unit-testing-jpa", "sa", "");
        entityManagerFactory = Persistence.createEntityManagerFactory("testPU");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Before
    public void startTransaction() throws Exception {
        entityManager.getTransaction().begin();
    }

    @After
    public void endTransaction() throws Exception {
        entityManager.getTransaction().rollback();
    }


    @AfterClass
    public static void tearDownDataBase() throws Exception {
        if (entityManager != null) {
            entityManager.close();
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
        try {
            connection.createStatement().execute("SHUTDOWN");
        } catch (Exception ex) {
        }
    }

}
