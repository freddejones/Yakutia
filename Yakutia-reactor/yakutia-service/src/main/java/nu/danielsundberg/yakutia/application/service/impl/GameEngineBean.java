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
        game.setGameStatus(GameStatus.CREATED);
        em.persist(game);

        Player player = em.find(Player.class, playerId);

        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setGame(game);
        gamePlayer.setGameId(game.getGameId());
        gamePlayer.setPlayer(player);
        gamePlayer.setPlayerId(player.getPlayerId());
        gamePlayer.setGamePlayerStatus(GamePlayerStatus.ACCEPTED);
        em.persist(gamePlayer);
        em.refresh(game);
        game.getPlayers().add(gamePlayer);
        em.merge(game);

        return game.getGameId();
    }

    @Override
    public void startNewGame(long gameId) {

        // TODO Add temporal check for when players are wiped out of invite

        /* setup the land areas */
        List<LandArea> landAreas = new ArrayList<LandArea>();
        landAreas.add(LandArea.SVERIGE);
        landAreas.add(LandArea.FINLAND);
        landAreas.add(LandArea.NORGE);
        // TODO Extend number of LandAreas

        Collections.shuffle(landAreas);

        List<GamePlayer> gamePlayers = getGamePlayerForGame(gameId);

        /* set status for gameplayer to be alive */
        for (GamePlayer gp : gamePlayers) {
            if (gp.getGamePlayerStatus() == GamePlayerStatus.ACCEPTED) {
                gp.setGamePlayerStatus(GamePlayerStatus.ALIVE);
                em.merge(gp);
            }
        }

        /* update game players */
        gamePlayers = getGamePlayerForGameAlive(gameId);

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

        /* set status of the game to ongoing */
        Game game = em.find(Game.class, gameId);
        game.setGameStatus(GameStatus.ONGOING);
        em.merge(game);
    }

    @Override
    public boolean isGameFinished(long gameId) {
        @SuppressWarnings("unchecked")  // TODO How to get rid of supress warnings
                List<GamePlayer> gamePlayers = (List<GamePlayer>) em.createNamedQuery("GamePlayer.getGamePlayersFromGameId")
                .setParameter("gameId",gameId)
                .getResultList();

        int numberOfActivePlayers = 0;
        for (GamePlayer gamePlayer : gamePlayers) {
            if (gamePlayer.getGamePlayerStatus() == GamePlayerStatus.ALIVE) {
                numberOfActivePlayers++;
            }
        }

        return numberOfActivePlayers == 1;
    }

    @SuppressWarnings("unchecked")  // TODO How to get rid of supress warnings
    private List<GamePlayer> getGamePlayerForGame(long gameId) {
        return (List<GamePlayer>) em.createNamedQuery("GamePlayer.getGamePlayersFromGameId")
                .setParameter("gameId",gameId)
                .getResultList();
    }

    @SuppressWarnings("unchecked")  // TODO How to get rid of supress warnings
    private List<GamePlayer> getGamePlayerForGameAlive(long gameId) {
        List<GamePlayer> gamePlayers = getGamePlayerForGame(gameId);
        List<GamePlayer> gamePlayersAccepted = new ArrayList<GamePlayer>();
        for (GamePlayer gp : gamePlayers) {
            if (gp.getGamePlayerStatus() == GamePlayerStatus.ALIVE) {
                gamePlayersAccepted.add(gp);
            }
        }
        return gamePlayersAccepted;
    }

}
