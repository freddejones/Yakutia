package nu.danielsundberg.yakutia.application.service.impl;

import junit.framework.Assert;
import nu.danielsundberg.yakutia.application.service.exceptions.LandIsNotNeighbourException;
import nu.danielsundberg.yakutia.application.service.landAreas.LandArea;
import nu.danielsundberg.yakutia.entity.*;
import nu.danielsundberg.yakutia.testcore.JpaTestCase;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerActionBeanTest extends JpaTestCase {

    private Player player1;
    private Player player2;
    private Game game;

    @Test(expected = LandIsNotNeighbourException.class)
    public void attackLandAreasNotConnected() throws LandIsNotNeighbourException {
        PlayerActionsBean playerActionsBean = new PlayerActionsBean();
        playerActionsBean.attackLandArea(LandArea.FINLAND, LandArea.NORGE,0);
    }


    // TODO Add more tests to this area later..
    @Test
    public void attackLandArea() throws LandIsNotNeighbourException {
        PlayerActionsBean playerActionsBean = new PlayerActionsBean();
        playerActionsBean.em = entityManager;

        // Given: 2 players, 1 game
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

        Player p2 = new Player();
        entityManager.persist(p2);
        entityManager.refresh(g);
        GamePlayer gp2 = new GamePlayer();
        gp2.setGame(g);
        gp2.setGameId(g.getGameId());
        gp2.setPlayer(p2);
        gp2.setPlayerId(p2.getPlayerId());
        entityManager.persist(gp2);
        g.getPlayers().add(gp2);
        entityManager.merge(g);

        Unit u = new Unit();
        u.setGamePlayer(gp);
        u.setLandArea(LandArea.FINLAND);
        u.setStrength(15);
        u.setTypeOfUnit(UnitType.TANK);
        entityManager.persist(u);

        Unit u2 = new Unit();
        u2.setGamePlayer(gp2);
        u2.setLandArea(LandArea.SVERIGE);
        u2.setStrength(15);
        u2.setTypeOfUnit(UnitType.TANK);
        entityManager.persist(u2);


        // When: 1 player attacking a connected landarea
        boolean wonLandArea = playerActionsBean.attackLandArea(
                LandArea.FINLAND, LandArea.SVERIGE,g.getGameId());

        // Then: 1 player did win (true returned)
        Assert.assertTrue(wonLandArea);
    }

}
