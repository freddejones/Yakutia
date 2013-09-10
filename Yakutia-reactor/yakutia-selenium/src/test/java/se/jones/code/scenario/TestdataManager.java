package se.jones.code.scenario;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * User: Fredde
 * Date: 9/8/13 12:25 PM
 */
public class TestdataManager {

    private static Connection conn;
    private static Liquibase liquibase;
    private static Database database;

    public static void setupConnection() throws SQLException,
            ClassNotFoundException, DatabaseException {
        Class.forName("org.apache.derby.jdbc.ClientDriver");

        //"jdbc:derby://localhost:1527/yakutiaDB";
        String connectionUrl = System.getProperty("DB_CONN_URL");
        conn = DriverManager.getConnection(connectionUrl, "APP", "APP");

        database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(conn));
    }

    public static void resetAndRebuild() throws SQLException,
            ClassNotFoundException, LiquibaseException {

        if (database == null) {
            setupConnection();
        }

        liquibase = new Liquibase("db/testdata/db.reset.xml",
                new ClassLoaderResourceAccessor(), database);
        liquibase.update(null);

        liquibase = new Liquibase("db/db.changelog-master.xml",
                new ClassLoaderResourceAccessor(), database);
        liquibase.update(null);
    }

    public static void loadTestdataSet1() throws LiquibaseException, SQLException, ClassNotFoundException {

        if (database == null) {
            setupConnection();
        }

        liquibase = new Liquibase("db/testdata/db.testdata.xml",
                new ClassLoaderResourceAccessor(), database);
        liquibase.update(null);
    }

}
