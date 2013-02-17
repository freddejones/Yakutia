package nu.danielsundberg.yakutia.entity;

import nu.danielsundberg.yakutia.testcore.JpaTestCase;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.*;

public class GamePlayerTest extends JpaTestCase {

//    private static Game g;
//    private static Player p;

    @BeforeClass
    public static void setup() {
//        g = new Game();
//        p = new Player();
    }


    @Test
    public void testGetAllGamePlayersQuery() {
        Game g = new Game();
        Player p = new Player();
        entityManager.persist(p);
        entityManager.persist(g);
//        entityManager.flush();
        entityManager.refresh(g);
//        entityManager.refresh(p);
        GamePlayer gp = new GamePlayer();
        gp.setGame(g);
        gp.setGameId(g.getGameId());
        gp.setPlayer(p);
        gp.setPlayerId(p.getPlayerId());
        entityManager.persist(gp);
        g.getPlayers().add(gp);
        entityManager.merge(g);

        assertEquals(1,
                entityManager.createNamedQuery("quicky").getResultList().size());
    }
}
