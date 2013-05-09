package nu.danielsundberg.yakutia.application.service.impl;

import nu.danielsundberg.yakutia.application.service.exceptions.NotEnoughPlayers;
import nu.danielsundberg.yakutia.entity.Game;
import nu.danielsundberg.yakutia.entity.GamePlayer;
import nu.danielsundberg.yakutia.entity.GamePlayerStatus;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.testcore.JpaTestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * User: Fredde
 * Date: 5/9/13 11:04 AM
 */
public class GameEngineBeanTest extends JpaTestCase {

    private Game game;
    private Player player1;
    private Player player2;
    private GamePlayer gamePlayer1;
    private GamePlayer gamePlayer2;

    @Before
    public void setup() {

        game = new Game();
        player1 = new Player();
        player1.setName("stickan");
        player1.setEmail("test");
        entityManager.persist(player1);
        entityManager.persist(game);
        entityManager.refresh(game);

        gamePlayer1 = new GamePlayer();
        gamePlayer1.setGame(game);
        gamePlayer1.setGameId(game.getGameId());
        gamePlayer1.setPlayer(player1);
        gamePlayer1.setPlayerId(player1.getPlayerId());
        gamePlayer1.setGamePlayerStatus(GamePlayerStatus.ACCEPTED);
        entityManager.persist(gamePlayer1);
        game.getPlayers().add(gamePlayer1);
        entityManager.merge(game);

        entityManager.refresh(game);
        player2 = new Player();
        player2.setName("tratten");
        player2.setEmail("test2");
        entityManager.persist(player2);
        entityManager.refresh(player2);
        gamePlayer2 = new GamePlayer();
        gamePlayer2.setGame(game);
        gamePlayer2.setGameId(game.getGameId());
        gamePlayer2.setPlayer(player2);
        gamePlayer2.setPlayerId(player2.getPlayerId());
        gamePlayer2.setGamePlayerStatus(GamePlayerStatus.ACCEPTED);
        entityManager.persist(gamePlayer2);
//        game.getPlayers().add(gamePlayer2);
//        entityManager.merge(game);
    }

    @Test
    public void startNewGameTest() throws NotEnoughPlayers {
        GameEngineBean gameEngineBean = new GameEngineBean();
        gameEngineBean.em = entityManager;
        gameEngineBean.startNewGame(game.getGameId());
    }

}
