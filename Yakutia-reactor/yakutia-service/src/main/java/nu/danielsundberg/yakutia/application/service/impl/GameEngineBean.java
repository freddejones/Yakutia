package nu.danielsundberg.yakutia.application.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    public void startNewGame(long gameId) {

        List<LandArea> landAreas = new ArrayList<LandArea>();
        landAreas.add(LandArea.SVERIGE);
        landAreas.add(LandArea.FINLAND);
        landAreas.add(LandArea.NORGE);
        // TODO Extend number of LandAreas

        Collections.shuffle(landAreas);

        @SuppressWarnings("unchecked")  // TODO How to get rid of supress warnings
        List<GamePlayer> gamePlayers = (List<GamePlayer>) em.createNamedQuery("GamePlayer.getGamePlayersFromGameId")
                .setParameter("gameId",gameId)
                .getResultList();

        /* assign gameplayers their landAreas */
        int playerIdx = 0;
        for (LandArea landArea : landAreas) {
            Unit u = new Unit();
            u.setGamePlayer(gamePlayers.get(playerIdx));
            u.setLandArea(landArea);
            u.setStrength(1);
            em.persist(u);

            playerIdx++;
            if (playerIdx == gamePlayers.size()) {
                playerIdx = 0;
            }
        }

        /* also assign gameplayers their units to place */
        for (GamePlayer gp : gamePlayers) {
            Unit unassignedUnit = new Unit();
            unassignedUnit.setStrength(14);
            unassignedUnit.setGamePlayer(gp);
            unassignedUnit.setLandArea(LandArea.UNASSIGNEDLAND);
            em.persist(unassignedUnit);
        }

    }
}
