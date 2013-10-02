package se.freddejones.yakutia.application.service.impl;

import junit.framework.Assert;
import se.freddejones.yakutia.application.service.exceptions.LandIsNotNeighbourException;
import se.freddejones.yakutia.application.service.exceptions.NotSufficientUnitException;
import se.freddejones.yakutia.application.service.exceptions.PlayerDoNotOwnLandAreaException;
import se.freddejones.yakutia.application.service.landAreas.LandArea;
import se.freddejones.yakutia.entity.*;
import se.freddejones.yakutia.testcore.JpaTestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PlayerActionBeanTest extends JpaTestCase {

    private Game g;
    private Player p,p2;
    private GamePlayer gp,gp2;
    private Unit u,u2;

    @Before
    public void setup() {
        createPreStuff();
    }

    @Test(expected = LandIsNotNeighbourException.class)
    public void attackLandAreasNotConnected() throws LandIsNotNeighbourException {
        PlayerActionsBean playerActionsBean = new PlayerActionsBean();
        playerActionsBean.attackLandArea(LandArea.FINLAND, LandArea.NORGE,0);
    }

    @Test
    public void attackLandArea() throws LandIsNotNeighbourException {
        PlayerActionsBean playerActionsBean = new PlayerActionsBean();
        playerActionsBean.em = entityManager;

        // Given: 2 players, 1 game
        // Already in setup method

        // When: 1 player attacking a connected landarea
        boolean wonLandArea = playerActionsBean.attackLandArea(
                LandArea.FINLAND, LandArea.SVERIGE,g.getGameId());

        // Then: 1 player did win (true returned)
        Assert.assertTrue(wonLandArea);
    }

    @Test
    public void getPlayersLandAreasTest() {
        PlayerActionsBean playerActionsBean = new PlayerActionsBean();
        playerActionsBean.em = entityManager;

        // Given: a player connected to a game

        // When: fetching a players landarea
        List<LandArea> landAreas = playerActionsBean.getPlayersLandAreas(
                p.getPlayerId(),g.getGameId());

        // Then: the correct land is returned
        Assert.assertEquals(1, landAreas.size());
        Assert.assertTrue(landAreas.get(0).equals(LandArea.FINLAND));
    }

    @Test
    public void getUnitsByLandAreaWrongGameIdConnectedTest() {
        PlayerActionsBean playerActionsBean = new PlayerActionsBean();
        playerActionsBean.em = entityManager;

        // Given: a player connected to a game

        // When: fetching a players landarea
        List<Unit> units = playerActionsBean.getUnitsByLandArea(LandArea.FINLAND,-1);

        // Then: the correct land is returned
        Assert.assertEquals(0, units.size());
    }

    @Test
    public void endTurnTest() {
        PlayerActionsBean playerActionsBean = new PlayerActionsBean();
        playerActionsBean.em = entityManager;

        // Given: a player connected to a game that has assigned landarea

        // When: ending a turn
        boolean wasEnded = playerActionsBean.endTurn(p.getPlayerId(),g.getGameId());

        // Then: the turn is ended
        Assert.assertTrue(wasEnded);
    }

    @Test
    public void endTurnTestUnassignedLand() {
        PlayerActionsBean playerActionsBean = new PlayerActionsBean();
        playerActionsBean.em = entityManager;

        // Given: a player connected to a game that has an unassigned landarea
        u2.setLandArea(LandArea.UNASSIGNEDLAND);
        entityManager.merge(u2);
        entityManager.refresh(gp2);

        // When: ending a turn
        boolean wasEnded = playerActionsBean.endTurn(p.getPlayerId(),g.getGameId());

        // Then: no turn is ended
        Assert.assertFalse(wasEnded);
    }

    @Test (expected = NotSufficientUnitException.class)
    public void placeUnitsExceptionTest() throws NotSufficientUnitException, PlayerDoNotOwnLandAreaException {
        PlayerActionsBean playerActionsBean = new PlayerActionsBean();
        playerActionsBean.em = entityManager;

        // Given: a existing gameplayer with unassigned land
        Unit unassignedUnit = new Unit();
        unassignedUnit.setGamePlayer(gp);
        unassignedUnit.setLandArea(LandArea.UNASSIGNEDLAND);
        unassignedUnit.setStrength(15);
        unassignedUnit.setTypeOfUnit(UnitType.TANK);
        entityManager.persist(unassignedUnit);
        gp.getUnits().add(unassignedUnit);
        entityManager.merge(gp);
        entityManager.merge(unassignedUnit);

        // When: placing to many units (more then actually one have)
        playerActionsBean.placeUnits(p.getPlayerId(),g.getGameId(),LandArea.FINLAND,16);

        // Then: Exception is thrown
    }

    @Test (expected = PlayerDoNotOwnLandAreaException.class)
    public void placeUnitsNotOwningLandAreaExceptionTest()
            throws NotSufficientUnitException, PlayerDoNotOwnLandAreaException {
        PlayerActionsBean playerActionsBean = new PlayerActionsBean();
        playerActionsBean.em = entityManager;

        // Given: a existing gameplayer with unassigned land
        entityManager.refresh(u);
        u.setLandArea(LandArea.UNASSIGNEDLAND);
        entityManager.merge(u);

        // When: trying to place units to land that you dont own
        playerActionsBean.placeUnits(p.getPlayerId(),g.getGameId(),LandArea.FINLAND,16);

        // Then: Exception is thrown
    }

    @Test
    public void placeUnitsHappyCaseTest() throws NotSufficientUnitException, PlayerDoNotOwnLandAreaException {
        PlayerActionsBean playerActionsBean = new PlayerActionsBean();
        playerActionsBean.em = entityManager;

        // Given: a existing gameplayer with unassigned land
        Unit unassignedUnit = new Unit();
        unassignedUnit.setGamePlayer(gp);
        unassignedUnit.setLandArea(LandArea.UNASSIGNEDLAND);
        unassignedUnit.setStrength(15);
        unassignedUnit.setTypeOfUnit(UnitType.TANK);
        entityManager.persist(unassignedUnit);
        gp.getUnits().add(unassignedUnit);
        entityManager.merge(gp);
        entityManager.merge(unassignedUnit);

        // When: trying to place units to land that you own
        entityManager.refresh(gp);
        Assert.assertEquals(2, gp.getUnits().size());
        playerActionsBean.placeUnits(p.getPlayerId(),g.getGameId(),LandArea.FINLAND,15);

        // Then: only FINLAND landarea is left
        entityManager.refresh(gp);
        Assert.assertEquals(1, gp.getUnits().size());
    }

    @Test
    public void placeUnitsNotAllUnitsPlacedTest() throws NotSufficientUnitException, PlayerDoNotOwnLandAreaException {
        PlayerActionsBean playerActionsBean = new PlayerActionsBean();
        playerActionsBean.em = entityManager;

        // Given: a existing gameplayer with unassigned land
        Unit unassignedUnit = new Unit();
        unassignedUnit.setGamePlayer(gp);
        unassignedUnit.setLandArea(LandArea.UNASSIGNEDLAND);
        unassignedUnit.setStrength(15);
        unassignedUnit.setTypeOfUnit(UnitType.TANK);
        entityManager.persist(unassignedUnit);
        gp.getUnits().add(unassignedUnit);
        entityManager.merge(gp);
        entityManager.merge(unassignedUnit);

        // When: trying to place units to land that you own
        entityManager.refresh(gp);
        Assert.assertEquals(2, gp.getUnits().size());
        playerActionsBean.placeUnits(p.getPlayerId(),g.getGameId(),LandArea.FINLAND,10);

        // Then: only FINLAND landarea is left
        entityManager.refresh(gp);
        Assert.assertEquals(2, gp.getUnits().size());
        entityManager.refresh(unassignedUnit);
        Assert.assertEquals(5, unassignedUnit.getStrength());
    }

    @Test
    public void playerIsActiveTest() {
        PlayerActionsBean playerActionsBean = new PlayerActionsBean();
        playerActionsBean.em = entityManager;

        // Given: two players in one game
        entityManager.refresh(p);
        entityManager.refresh(gp);
        gp.setActivePlayerTurn(false);
        gp2.setActivePlayerTurn(true);

        // When: checking active player status
        Boolean isActivePlayer = playerActionsBean.isPlayerTurn(gp.getPlayerId(), gp.getGameId());
        Boolean isActivePlayer2 = playerActionsBean.isPlayerTurn(gp2.getPlayerId(),gp2.getGameId());

        // Then: one of them is active
        Assert.assertFalse(isActivePlayer);
        Assert.assertTrue(isActivePlayer2);
    }

    private void createPreStuff() {
        g = new Game();
        p = new Player();
        entityManager.persist(p);
        entityManager.persist(g);
        entityManager.refresh(g);
        gp = new GamePlayer();
        gp.setGame(g);
        gp.setGameId(g.getGameId());
        gp.setPlayer(p);
        gp.setPlayerId(p.getPlayerId());
        entityManager.persist(gp);
        g.getPlayers().add(gp);
        entityManager.merge(g);

        p2 = new Player();
        entityManager.persist(p2);
        entityManager.refresh(g);
        gp2 = new GamePlayer();
        gp2.setGame(g);
        gp2.setGameId(g.getGameId());
        gp2.setPlayer(p2);
        gp2.setPlayerId(p2.getPlayerId());
        entityManager.persist(gp2);
        g.getPlayers().add(gp2);
        entityManager.merge(g);

        u = new Unit();
        u.setGamePlayer(gp);
        u.setLandArea(LandArea.FINLAND);
        u.setStrength(15);
        u.setTypeOfUnit(UnitType.TANK);
        entityManager.persist(u);
        List<Unit> units = new ArrayList<Unit>();
        units.add(u);
        gp.setUnits(units);
        entityManager.merge(gp);
        entityManager.merge(u);

        u2 = new Unit();
        u2.setGamePlayer(gp2);
        u2.setLandArea(LandArea.SVERIGE);
        u2.setStrength(15);
        u2.setTypeOfUnit(UnitType.TANK);
        entityManager.persist(u2);
        List<Unit> units2 = new ArrayList<Unit>();
        units.add(u2);
        gp2.setUnits(units2);
        entityManager.merge(gp2);
        entityManager.merge(u2);
    }

}
