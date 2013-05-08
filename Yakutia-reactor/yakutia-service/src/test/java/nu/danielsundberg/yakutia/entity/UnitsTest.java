package nu.danielsundberg.yakutia.entity;

import nu.danielsundberg.yakutia.application.service.landAreas.LandArea;
import nu.danielsundberg.yakutia.testcore.JpaTestCase;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnitsTest extends JpaTestCase {

    @Test
    public void testToPersistTank() {

        Unit tank = new Unit();
        tank.setTypeOfUnit(UnitType.TANK);
        tank.setLandArea(LandArea.FINLAND);

        entityManager.persist(tank);

        assertNotNull(tank.getId());
    }

    @Test
    public void testNamedQueryFindUnitsByLandAreaName() {
        Unit tank = new Unit();
        tank.setTypeOfUnit(UnitType.TANK);
        tank.setLandArea(LandArea.SVERIGE);
        tank.setStrength(15);

        entityManager.persist(tank);

        Unit tankGet = (Unit) entityManager.createNamedQuery("Unit.getUnitsByLandArea")
                .setParameter("laName",LandArea.SVERIGE)
                .getSingleResult();

        assertEquals(LandArea.SVERIGE,tankGet.getLandArea());
    }

}
