package nu.danielsundberg.yakutia.application.service.impl;

import java.util.HashSet;
import java.util.Set;

import javassist.NotFoundException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import nu.danielsundberg.yakutia.entity.Game;
import nu.danielsundberg.yakutia.entity.Player;


@Stateless
public class GameEngineBean {

    @EJB
    private GameBoardBean gameBoardBean;

    @PersistenceContext(name = "yakutiaPU")
    private EntityManager em;

	public long startNewGame(Set<Long> playerIds) throws NotFoundException, NamingException {

        Context ctx = new InitialContext();
		Game game = new Game();
        em.persist(game);


        Set<Player> players = new HashSet<Player>();

        // Players already exists right
        for (Long id : playerIds) {
            Player play = em.find(Player.class, id);
            players.add(play);
        }
        game.setPlayers(players);
        em.merge(game);

        gameBoardBean.generateGameBoard(game.getGameId());
        em.merge(game);

        gameBoardBean.assignPlayers(players, game.getGameId());
        em.merge(game);

        return game.getGameId();
    }


}
