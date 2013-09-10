package nu.danielsundberg.yakutia.entity;

import junit.framework.Assert;
import nu.danielsundberg.yakutia.testcore.JpaTestCase;
import org.junit.Test;

import java.util.Date;

/**
 * User: Fredde
 * Date: 9/9/13 10:27 PM
 */
public class GameTest extends JpaTestCase {

    @Test
    public void createdGameIncludesCreatedPlayerId() {
        // Given: a entity with a game creator id persisted
        Game g = new Game();
        g.setGameCreatorPlayerId(12L);
        entityManager.persist(g);

        // When: reading back
        entityManager.refresh(g);

        // That: it could be persisted and reread
        Assert.assertEquals(12L, g.getGameCreatorPlayerId());
        Assert.assertNotNull(g.getGameId());
    }

    @Test
    public void testDates() {
        // Given: a entity with the dates set and persisted
        Game g = new Game();
        Date d = new Date();
        g.setCreationTime(d);
        g.setStartedTime(d);
        g.setFinshedTime(d);
        entityManager.persist(g);

        // When: reading back
        entityManager.refresh(g);

        // That: it could be persisted and reread
        Assert.assertEquals(d, g.getCreationTime());
        Assert.assertEquals(d, g.getStartedTime());
        Assert.assertEquals(d, g.getFinshedTime());
        Assert.assertNotNull(g.getGameId());
    }

}
