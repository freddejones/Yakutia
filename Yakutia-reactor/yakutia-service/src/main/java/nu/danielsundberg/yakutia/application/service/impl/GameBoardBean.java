package nu.danielsundberg.yakutia.application.service.impl;

import nu.danielsundberg.yakutia.entity.Game;
import nu.danielsundberg.yakutia.entity.LandArea;
import nu.danielsundberg.yakutia.entity.Player;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Stateless
public class GameBoardBean {

    @PersistenceContext(name = "yakutiaPU")
    private EntityManager em;


    // TODO create a board factory?
    // TODO and just make the assingment here in this bean?
    // TODO figure out how to map the domain models

    public void generateGameBoard(long gameId) {

        Game game = em.find(Game.class, gameId);

        if (game == null) {
            throw new RuntimeException("Could not find any game with id: " + gameId);
        }

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

    public void assignPlayers(Set<Player> players, long gameId) {
        Game game = em.find(Game.class, gameId);

        Player p1;
        Player p2;

        Iterator<Player> playerIt = players.iterator();
        p1 = playerIt.next();
        p2 = playerIt.next();

        Iterator<LandArea> landAreaIterable = game.getGameBoard().iterator();
        Set<LandArea> la1 = new HashSet<LandArea>();
        la1.add(landAreaIterable.next());
        la1.add(landAreaIterable.next());
        p1.setLandAreas(la1);

        Set<LandArea> la2 = new HashSet<LandArea>();
        la2.add(landAreaIterable.next());
        p2.setLandAreas(la2);

        em.merge(p1);
        em.merge(p2);
    }

}
