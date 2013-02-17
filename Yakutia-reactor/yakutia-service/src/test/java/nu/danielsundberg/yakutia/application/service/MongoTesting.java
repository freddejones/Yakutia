package nu.danielsundberg.yakutia.application.service;

import javassist.NotFoundException;
import nu.danielsundberg.yakutia.application.service.impl.GameEngineBean;
import nu.danielsundberg.yakutia.entity.Game;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.testcore.EjbTestCase;
import org.junit.Test;

import javax.ejb.EJB;
import javax.naming.NamingException;
import javax.transaction.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MongoTesting extends EjbTestCase {

    @EJB
//    private GameBoardBean gbb;
    private GameEngineBean geb;

    @Test
    public void setupGameBoard() throws NamingException {
//        Game game = new Game();
//        entityManager.persist(game);
//
//        GameBoardBean gameBoardBean = (GameBoardBean) context.lookup("java:global/yakutia-service/GameBoardBean");
//        gameBoardBean.generateGameBoard(game.getGameId());
//
//        Game g = entityManager.find(Game.class, game.getGameId());
//        System.out.println("Gameboardsize = " + g.getGameBoard().size());
//
//        assertEquals(3, g.getGameBoard().size());
    }

}
