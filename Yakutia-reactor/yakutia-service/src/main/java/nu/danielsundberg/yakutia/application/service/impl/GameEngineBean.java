package nu.danielsundberg.yakutia.application.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nu.danielsundberg.yakutia.GameplayerId;
import nu.danielsundberg.yakutia.landAreas.LandArea;
import nu.danielsundberg.yakutia.entity.*;
import nu.danielsundberg.yakutia.GameEngineInterface;


@Stateless(mappedName = "kickass")
public class GameEngineBean implements GameEngineInterface {

    @PersistenceContext(name = "yakutiaPU")
    protected EntityManager em;

    // MOVE TO PREGAME INTEFACE
    @Override
    public long createNewGame(long playerId) {
        Game game = new Game();
        em.persist(game);

        Player player = em.find(Player.class, playerId);

        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setGame(game);
        gamePlayer.setGameId(game.getGameId());
        gamePlayer.setPlayer(player);
        gamePlayer.setPlayerId(player.getPlayerId());
        em.persist(gamePlayer);
        em.refresh(game);
        game.getPlayers().add(gamePlayer);
        em.merge(game);

        return game.getGameId();
    }

    @Override
    public void startNewGame(List<GameplayerId> gamePlayers) {

        //TODO assign landmasses:

        List<LandArea> landAreas = new ArrayList<LandArea>();
        landAreas.add(LandArea.SVERIGE);
        landAreas.add(LandArea.FINLAND);
        landAreas.add(LandArea.NORGE);

        Collections.shuffle(landAreas);

        Stack<LandArea> landAreaStack = new Stack<LandArea>();
        for (LandArea landArea : landAreas) {
            landAreaStack.push(landArea);
        }

        // TODO add some check that number of players have been reached

        // TODO turn around the loop to go on landAreas instead
        // TODO Move Landarea to API level instead
        for (GameplayerId gamePlayerId : gamePlayers) {
            // TODO FIX QUERY
            @SuppressWarnings("unchecked")
            List<GamePlayer> gplist = em.createQuery("SELECT g FROM GamePlayer g WHERE g.playerId = "+gamePlayerId.getPlayerId()).getResultList();
            Unit u = new Unit();
            u.setGamePlayer(gplist.get(0));
            u.setLandArea(landAreaStack.pop());
            u.setStrength(5);
            em.persist(u);
        }

    }
}
