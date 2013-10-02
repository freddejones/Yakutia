package se.freddejones.yakutia.application.service.impl;

import junit.framework.Assert;
import org.mockito.Matchers;
import se.freddejones.yakutia.application.service.exceptions.NotEnoughPlayers;
import se.freddejones.yakutia.application.service.random.RandomEventHandler;
import se.freddejones.yakutia.entity.Game;
import se.freddejones.yakutia.entity.GamePlayer;
import se.freddejones.yakutia.entity.statuses.GamePlayerStatus;
import se.freddejones.yakutia.entity.statuses.GameStatus;
import se.freddejones.yakutia.testcore.JpaTestCase;
import org.junit.Before;
import org.junit.Test;
import se.freddejones.yakutia.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    public void startedGamePutsInvitedToDeclineAutomatically() throws NotEnoughPlayers {
        GameEngineBean gameEngineBean = new GameEngineBean();
        gameEngineBean.em = entityManager;
        String invitedUserName="finkel";

        // Given: three players, both players has ACCPETED status and the third INVITED
        Player player1 = new Player();
        player1.setName("stickan");
        player1.setEmail("test");

        Player player2 = new Player();
        player2.setName("tratten");
        player2.setEmail("test2");

        Player player3 = new Player();
        player3.setName(invitedUserName);
        player3.setEmail("test3");

        addNewPlayer(player1, GamePlayerStatus.ACCEPTED);
        addNewPlayer(player2, GamePlayerStatus.ACCEPTED);
        addNewPlayer(player3, GamePlayerStatus.INVITED);


        // When: starting a new game
        gameEngineBean.startNewGame(game.getGameId());

        // Then: will get a new game started and player 3 will be DECLINED
        for (GamePlayer gp : gameEngineBean.em.find(Game.class, game.getGameId()).getPlayers()) {
            if (gp.getPlayer().getName().equals(invitedUserName)) {
                Assert.assertTrue(gp.getGamePlayerStatus().equals(GamePlayerStatus.DECLINED));
            }
        }
    }

    @Test(expected = NotEnoughPlayers.class)
    public void tryingToStartWithToFewAcceptsForGame() throws NotEnoughPlayers {
        GameEngineBean gameEngineBean = new GameEngineBean();
        gameEngineBean.em = entityManager;

        // Given: three players, one players has ACCPETED status and the others have status INVITED
        Player player1 = new Player();
        player1.setName("stickan");
        player1.setEmail("test");

        Player player2 = new Player();
        player2.setName("tratten");
        player2.setEmail("test2");

        Player player3 = new Player();
        player3.setName("finkel");
        player3.setEmail("test3");

        addNewPlayer(player1, GamePlayerStatus.ACCEPTED);
        addNewPlayer(player2, GamePlayerStatus.INVITED);
        addNewPlayer(player3, GamePlayerStatus.INVITED);

        // When:
        gameEngineBean.startNewGame(game.getGameId());

        // Then: an exception is thrown
    }

    @Test
    public void nextPlayersTurnTest() throws NotEnoughPlayers {
        GameEngineBean gameEngineBean = new GameEngineBean();
        RandomEventHandler reh = new RandomEventHandler();
        gameEngineBean.em = entityManager;
        gameEngineBean.randomEventHandler = reh;

        // Given: three players accepted
        Player player1 = new Player();
        player1.setName("stickan");
        player1.setEmail("test");

        Player player2 = new Player();
        player2.setName("tratten");
        player2.setEmail("test2");

        addNewPlayer(player1, GamePlayerStatus.ACCEPTED);
        addNewPlayer(player2, GamePlayerStatus.ACCEPTED);

        List<GamePlayer> players = new ArrayList<GamePlayer>();

        List<GamePlayer> list = entityManager.createQuery("SELECT gp FROM GamePlayer gp").getResultList();
        long gamePlayerIdFirst = list.get(0).getGamePlayerId();
        long gamePlayerIdSecond = list.get(1).getGamePlayerId();
        GamePlayer gp1 = entityManager.find(GamePlayer.class, gamePlayerIdFirst);
        GamePlayer gp2 = entityManager.find(GamePlayer.class, gamePlayerIdSecond);

        players.add(gp1);
        players.add(gp2);

        when(mock(RandomEventHandler.class).shufflePlayer(anyList())).thenReturn(players);

        // When: starting a new game
        gameEngineBean.startNewGame(game.getGameId());

        // Then: next player in turn are correctly pointing to each other
//        entityManager.refresh(gp1);
//        entityManager.refresh(gp2);
        Assert.assertEquals(gamePlayerIdSecond, gp1.getNextGamePlayerIdTurn());
        Assert.assertEquals(gamePlayerIdFirst, gp2.getNextGamePlayerIdTurn());
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
