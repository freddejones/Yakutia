package se.freddejones.yakutia.testcore;


//@LocalClient
public class EjbTestCase {

//    public static EJBContainer container;
//    public static Context context;
//    private static Properties prop;
//
//    @Resource
//    protected UserTransaction userTransaction;
//
//    @PersistenceContext
//    protected EntityManager entityManager;
//
//    @BeforeClass
//    public static void setup() {
//        prop = new Properties();
//        prop.put("jdbc/__default", "new://Resource?type=DataSource");
//        prop.put("jdbc/__default.JdbcDriver", "org.hsqldb.jdbcDriver");
//        prop.put("jdbc/__default.JdbcUrl", "jdbc:hsqldb:mem:yakutia");
//        prop.put(Context.INITIAL_CONTEXT_FACTORY,
//                "org.apache.openejb.client.LocalInitialContextFactory");
//
//        container = EJBContainer.createEJBContainer(prop);
//        context = container.getContext();
//    }
//
//    @Before
//    public void setupEm() throws NamingException, SystemException,
//            NotSupportedException {
//        context.bind("inject",this);
//        userTransaction.begin();
//    }
//
//    @After
//    public void tearDownEm() throws HeuristicRollbackException, RollbackException,
//            HeuristicMixedException, SystemException {
//        userTransaction.rollback();
//    }
//
//    @AfterClass
//    public static void tearDown() {
//        container.close();
//    }
}
