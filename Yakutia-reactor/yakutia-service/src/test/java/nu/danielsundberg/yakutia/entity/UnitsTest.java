package nu.danielsundberg.yakutia.entity;

import nu.danielsundberg.yakutia.testcore.JpaTestCase;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnitsTest extends JpaTestCase {

    @Test
    public void testToPersistTank() {

        Unit tank = new Unit();
        tank.setHealth(1);
        tank.setTypeOfUnit(UnitType.TANK);

        entityManager.persist(tank);

        assertNotNull(tank.getId());
    }

}
