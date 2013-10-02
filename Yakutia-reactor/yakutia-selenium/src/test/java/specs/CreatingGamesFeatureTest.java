package specs;

import liquibase.exception.LiquibaseException;
import org.agileinsider.concordion.junit.ConcordionPlus;
import org.junit.Test;
import org.junit.runner.RunWith;
import se.freddejones.yakutia.scenario.DriverHandler;
import se.freddejones.yakutia.scenario.TestdataManager;

import java.sql.SQLException;

/**
 * User: Fredde
 * Date: 9/13/13 10:45 PM
 */
@RunWith(ConcordionPlus.class)
public class CreatingGamesFeatureTest {

    private static String baseUrl;
    private DriverHandler dh;

    static {
        baseUrl = System.getProperty("BASE_URL");
    }

    @Test
    public void tempTest() throws LiquibaseException, SQLException, ClassNotFoundException {
        TestdataManager.resetAndRebuild();
        TestdataManager.loadTestdataSet1();
    }

}
