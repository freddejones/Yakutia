package nu.danielsundberg.yakutia.application.service;

import nu.danielsundberg.yakutia.application.service.impl.GenerateGameBoard;
import nu.danielsundberg.yakutia.testcore.EjbTestCase;
import nu.danielsundberg.yakutia.testcore.JpaTestCase;
import org.junit.Before;
import org.junit.Test;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class GenerateGameBoardTest extends EjbTestCase {

 /*   @Test
    public void testMe() {
        GenerateGameBoard gameBoard = new GenerateGameBoard();
        gameBoard.setEntityManager(entityManager);

        gameBoard.generateGameBoard(1);
    }
   */

    @Test
    public void embeddedContainerTest() throws NamingException {



        GenerateGameBoard gameBoard = (GenerateGameBoard) context.lookup("java:global/yakutia-service/GenerateGameBoard");
        int gameID = gameBoard.createGame();
        gameBoard.generateGameBoard(gameID);


    }
}
