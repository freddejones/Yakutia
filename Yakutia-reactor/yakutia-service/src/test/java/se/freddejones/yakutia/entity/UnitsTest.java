package se.freddejones.yakutia.entity;

import se.freddejones.yakutia.application.service.landAreas.LandArea;
import se.freddejones.yakutia.testcore.JpaTestCase;
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
        Game g = new Game();
        Player p = new Player();
        entityManager.persist(p);
        entityManager.persist(g);
        entityManager.refresh(g);
        GamePlayer gp = new GamePlayer();
        gp.setGame(g);
        gp.setGameId(g.getGameId());
        gp.setPlayer(p);
        gp.setPlayerId(p.getPlayerId());
        entityManager.persist(gp);
        g.getPlayers().add(gp);
        entityManager.merge(g);

        Unit tank = new Unit();
        tank.setTypeOfUnit(UnitType.TANK);
        tank.setLandArea(LandArea.SVERIGE);
        tank.setStrength(15);
        tank.setGamePlayer(gp);
        entityManager.persist(tank);
        entityManager.merge(gp);

        Unit tankGet = (Unit) entityManager.createNamedQuery("Unit.getUnitsByLandAreaAndGamePlayer")
                .setParameter("laName",LandArea.SVERIGE)
                .setParameter("gp", gp)
                .getSingleResult();

        assertEquals(LandArea.SVERIGE,tankGet.getLandArea());
    }

}
