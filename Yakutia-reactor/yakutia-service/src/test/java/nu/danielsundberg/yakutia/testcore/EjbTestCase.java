package nu.danielsundberg.yakutia.testcore;

import org.apache.openejb.api.LocalClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.annotation.Resource;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;
import java.util.Properties;

@LocalClient
public class EjbTestCase {

    public static EJBContainer container;
    public static Context context;
    private static Properties prop;

    @Resource
    protected UserTransaction userTransaction;

    @PersistenceContext
    protected EntityManager entityManager;

    @BeforeClass
    public static void setup() {
        prop = new Properties();
        prop.put("jdbc/__default", "new://Resource?type=DataSource");
        prop.put("jdbc/__default.JdbcDriver", "org.hsqldb.jdbcDriver");
        prop.put("jdbc/__default.JdbcUrl", "jdbc:hsqldb:mem:yakutia");
        prop.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.openejb.client.LocalInitialContextFactory");

        container = EJBContainer.createEJBContainer(prop);
        context = container.getContext();
    }

    @Before
    public void setupEm() throws NamingException, SystemException,
            NotSupportedException {
        context.bind("inject",this);
        userTransaction.begin();
    }

    @After
    public void tearDownEm() throws HeuristicRollbackException, RollbackException,
            HeuristicMixedException, SystemException {
        userTransaction.rollback();
    }

    @AfterClass
    public static void tearDown() {
        container.close();
    }
}
