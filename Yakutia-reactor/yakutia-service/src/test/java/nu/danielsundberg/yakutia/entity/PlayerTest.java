package nu.danielsundberg.yakutia.entity;

import static junit.framework.Assert.*;
import nu.danielsundberg.yakutia.testcore.JpaTestCase;
import org.junit.Test;

import java.util.Set;

public class PlayerTest extends JpaTestCase {

    @Test
    public void persistPlayer() {
        Player player = new Player();
        entityManager.persist(player);
        assertNotNull(player.getPlayerId());
    }

}
