package nu.danielsundberg.yakutia.application.service.impl;

import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.application.service.exceptions.PlayerAlreadyExists;
import nu.danielsundberg.yakutia.testcore.JpaTestCase;
import org.junit.Test;

public class PreGameBeanTest extends JpaTestCase {

    private String playerName = "PLAYER";
    private Player player;

    @Test(expected = PlayerAlreadyExists.class)
    public void playerNameAlreadyExistingPlayer() throws PlayerAlreadyExists{
        createPlayer();

        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;
        preGameBean.createNewPlayer("PLAYER","mail");

    }


    private void createPlayer() {
        player = new Player();
        player.setName(playerName);
        entityManager.persist(player);
    }
}
