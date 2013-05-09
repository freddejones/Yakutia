package nu.danielsundberg.yakutia.application.service.impl;

import junit.framework.Assert;
import nu.danielsundberg.yakutia.application.service.exceptions.NotEnoughPlayers;
import nu.danielsundberg.yakutia.entity.*;
import nu.danielsundberg.yakutia.testcore.JpaTestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * User: Fredde
 * Date: 5/9/13 11:04 AM
 */
public class GameEngineBeanTest extends JpaTestCase {

    private Game game;

    @Before
    public void setup() {
        game = new Game();
        entityManager.persist(game);
        entityManager.refresh(game);
    }

    @Test
    public void startNewGameTest() throws NotEnoughPlayers {
        GameEngineBean gameEngineBean = new GameEngineBean();
        gameEngineBean.em = entityManager;

        // Given: two players
        Player player1 = new Player();
        player1.setName("stickan");
        player1.setEmail("test");

        Player player2 = new Player();
        player2.setName("tratten");
        player2.setEmail("test2");

        // When: both players has accepted status
        addNewPlayer(player1, GamePlayerStatus.ACCEPTED);
        addNewPlayer(player2, GamePlayerStatus.ACCEPTED);

        // Then: will get a new game started
        gameEngineBean.startNewGame(game.getGameId());
    }

    @Test(expected = NotEnoughPlayers.class)
    public void onePlayerStartNewGameTest() throws NotEnoughPlayers {
        GameEngineBean gameEngineBean = new GameEngineBean();
        gameEngineBean.em = entityManager;

        // Given: one player
        Player player1 = new Player();
        player1.setName("stickan");
        player1.setEmail("test");

        // When: has accepted status
        addNewPlayer(player1, GamePlayerStatus.ACCEPTED);

        // Then: NotEnoughPlayers exception thrown
        gameEngineBean.startNewGame(game.getGameId());
    }

    @Test
    public void gameIsNotFinishedTest() {
        GameEngineBean gameEngineBean = new GameEngineBean();
        gameEngineBean.em = entityManager;

        // Given: two players
        Player player1 = new Player();
        player1.setName("stickan");
        player1.setEmail("test");

        Player player2 = new Player();
        player2.setName("tot");
        player2.setEmail("test2");

        // When: not finished status
        addNewPlayer(player1, GamePlayerStatus.ALIVE);
        addNewPlayer(player2, GamePlayerStatus.ALIVE);

        // Then: false returned
        Assert.assertFalse(gameEngineBean.isGameFinished(game.getGameId()));
    }

    @Test
    public void gameIsFinishedTest() {
        GameEngineBean gameEngineBean = new GameEngineBean();
        gameEngineBean.em = entityManager;

        // Given: two players
        Player player1 = new Player();
        player1.setName("stickan");

        Player player2 = new Player();
        player2.setName("tot");

        Player player3 = new Player();
        player2.setName("tat");

        Player player4 = new Player();
        player2.setName("tit");

        // When: not finished status
        addNewPlayer(player1, GamePlayerStatus.ALIVE);
        addNewPlayer(player2, GamePlayerStatus.DEAD);
        addNewPlayer(player3, GamePlayerStatus.DECLINED);
        addNewPlayer(player4, GamePlayerStatus.INVITED);

        // Then: false returned
        Assert.assertTrue(gameEngineBean.isGameFinished(game.getGameId()));
    }

    @Test
    public void endGameTest() {
        GameEngineBean gameEngineBean = new GameEngineBean();
        gameEngineBean.em = entityManager;
        // Given: a game

        // When: calling endGame method
        gameEngineBean.endGame(game.getGameId());

        // Then: game object has status FINISHED
        Assert.assertTrue(entityManager.find(Game.class,game.getGameId())
                .getGameStatus() == GameStatus.FINISHED);
    }

    private void addNewPlayer(Player p, GamePlayerStatus gpStatus) {
        entityManager.refresh(game);
        entityManager.persist(p);
        GamePlayer genericGamePlayer = new GamePlayer();
        genericGamePlayer.setGame(game);
        genericGamePlayer.setGameId(game.getGameId());
        genericGamePlayer.setPlayer(p);
        genericGamePlayer.setPlayerId(p.getPlayerId());
        genericGamePlayer.setGamePlayerStatus(gpStatus);
        entityManager.persist(genericGamePlayer);
        game.getPlayers().add(genericGamePlayer);
        entityManager.merge(game);
    }
}
