package se.freddejones.yakutia.entity;

import se.freddejones.yakutia.testcore.JpaTestCase;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class GamePlayerTest extends JpaTestCase {

    private Game g;
    private Player p;
    private GamePlayer gp;

    @Before
    public void setup() {

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
    }

    @Test
    public void testGetGamePlayersFromGameIdAndPlayerId() {
        assertEquals(1,
                entityManager.createNamedQuery("GamePlayer.getGamePlayer")
                        .setParameter("gameId",g.getGameId())
                        .setParameter("playerId",p.getPlayerId())
                        .getResultList().size());
    }

    @Test
    public void testGetGamePlayersFromGameId() {
        assertEquals(1,
                entityManager.createNamedQuery("GamePlayer.getGamePlayersFromGameId")
                        .setParameter("gameId",g.getGameId())
                        .getResultList().size());
    }

    @Test
    public void testGetGamePlayersFromPlayerId() {
        assertEquals(1,
                entityManager.createNamedQuery("GamePlayer.getGamePlayersFromPlayerId")
                        .setParameter("playerId",p.getPlayerId())
                        .getResultList().size());
    }
}
