package nu.danielsundberg.yakutia.entity;

import static junit.framework.Assert.*;
import nu.danielsundberg.yakutia.testcore.JpaTestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class PlayerTest extends JpaTestCase {

    private Player player1;

    @Before
    public void initTest() {
        player1 = new Player();
    }

    @Test
    public void persistPlayer() {
        entityManager.persist(player1);
        assertNotNull(player1.getPlayerId());
    }

}
