package nu.danielsundberg.yakutia.application.service.impl;

import nu.danielsundberg.yakutia.entity.Game;
import nu.danielsundberg.yakutia.entity.LandArea;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.HashSet;
import java.util.Set;

@Stateless
public class GenerateGameBoard {

    @PersistenceContext(name = "yakutiaPU")
    private EntityManager em;

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public int createGame() {
        Game gameC = new Game();
        em.persist(gameC);

        System.out.println(gameC.getGameId());
        return gameC.getGameId();
    }

    public void generateGameBoard(int gameId) {
        Game game = em.find(Game.class, gameId);

        LandArea la1 = new LandArea();
        la1.setName("AAAAA");
        em.persist(la1);

        LandArea la2 = new LandArea();
        la2.setName("BBBBB");
        em.persist(la2);

        LandArea la3 = new LandArea();
        la3.setName("CCCCCC");
        em.persist(la3);

        Set<LandArea> la1Neighbourghs = new HashSet<LandArea>();
        la1Neighbourghs.add(la2);
        la1Neighbourghs.add(la3);
        la1.setNeighbours(la1Neighbourghs);

        Set<LandArea> la2Neighbourghs = new HashSet<LandArea>();
        la2Neighbourghs.add(la1);
        la1.setNeighbours(la2Neighbourghs);

        Set<LandArea> la3Neighbourghs = new HashSet<LandArea>();
        la3Neighbourghs.add(la1);
        la1.setNeighbours(la3Neighbourghs);

        Set<LandArea> gameBoard = new HashSet<LandArea>();
        gameBoard.add(la1);
        gameBoard.add(la2);
        gameBoard.add(la3);

        game.setGameBoard(gameBoard);
        em.persist(game);
    }

}
