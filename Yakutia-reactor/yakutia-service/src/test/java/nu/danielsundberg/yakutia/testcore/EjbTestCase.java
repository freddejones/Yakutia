package nu.danielsundberg.yakutia.testcore;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EjbTestCase {

    public static EJBContainer container;
    public static Context context;

    @BeforeClass
    public static void setup() {

        final Properties p = new Properties();
        p.put("jdbc/__default", "new://Resource?type=DataSource");
        p.put("jdbc/__default.JdbcDriver", "org.hsqldb.jdbcDriver");
        p.put("jdbc/__default.JdbcUrl", "jdbc:hsqldb:mem:yakutia");

        container = EJBContainer.createEJBContainer(p);
        context = container.getContext();
    }

    @AfterClass
    public static void teardown() {
        container.close();
    }

}
