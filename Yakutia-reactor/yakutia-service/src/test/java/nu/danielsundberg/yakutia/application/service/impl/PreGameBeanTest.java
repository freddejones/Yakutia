package nu.danielsundberg.yakutia.application.service.impl;

import junit.framework.Assert;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.application.service.exceptions.PlayerAlreadyExists;
import nu.danielsundberg.yakutia.testcore.JpaTestCase;
import org.junit.Test;

public class PreGameBeanTest extends JpaTestCase {

    private String playerName = "PLAYER";
    private String email = "";
    private Player player;

    @Test
    public void createANewPlayerTest() throws PlayerAlreadyExists {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;
        long pId =  preGameBean.createNewPlayer(playerName, "mail");
        Assert.assertNotNull(pId);
    }

    @Test(expected = PlayerAlreadyExists.class)
    public void playerNameAlreadyExist() throws PlayerAlreadyExists{
        createPlayer();
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;
        preGameBean.createNewPlayer(playerName, "mail");
    }

    @Test
    public void playerExistCheckTrue() {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: one player with a email
        email = "email";
        createPlayer();

        // When: check if player exists method is called
        boolean doExist = preGameBean.playerExists(email);

        // Then: return true since same email
        Assert.assertTrue(doExist);
    }

    @Test
    public void createANewGameTest() {
        PreGameBean preGameBean = new PreGameBean();
        preGameBean.em = entityManager;

        // Given: a player
        createPlayer();

        // When: creating a game
        long gameId = preGameBean.createNewGame(player.getPlayerId());

        // Then: a game is created
        Assert.assertNotNull(gameId);
    }


    private void createPlayer() {
        player = new Player();
        player.setName(playerName);
        player.setEmail(email);
        entityManager.persist(player);
    }
}
