package se.freddejones.yakutia.application.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import se.freddejones.yakutia.application.service.landAreas.LandArea;
import se.freddejones.yakutia.application.service.random.RandomEventHandler;
import se.freddejones.yakutia.entity.statuses.GameStatus;
import se.freddejones.yakutia.application.service.exceptions.NotEnoughPlayers;
import se.freddejones.yakutia.application.service.iface.GameEngineInterface;
import se.freddejones.yakutia.entity.Game;
import se.freddejones.yakutia.entity.GamePlayer;
import se.freddejones.yakutia.entity.Unit;
import se.freddejones.yakutia.entity.statuses.GamePlayerStatus;


@Stateless(mappedName = "kickass")
public class GameEngineBean implements GameEngineInterface {

    @PersistenceContext(name = "yakutiaPU")
    protected EntityManager em;
    protected RandomEventHandler randomEventHandler;

    @Override
    public void startNewGame(long gameId) throws NotEnoughPlayers {
        randomEventHandler = new RandomEventHandler();

        // initiate land areas
        List<LandArea> landAreas = new ArrayList<LandArea>();
        landAreas.add(LandArea.SVERIGE);
        landAreas.add(LandArea.FINLAND);
        landAreas.add(LandArea.NORGE);

        /* shuffle the land areas */
        landAreas = randomEventHandler.shuffleLandAreas(landAreas);

        // TODO well fix this then...
        /* shuffle up the players and assign turn order */
        List<GamePlayer> gamePlayers = getGamePlayerForGame(gameId);

        if (gamePlayers.size() < 2
                || isLessThanMinimumAcceptedPlayers(gamePlayers)) {
            throw new NotEnoughPlayers("Could only find " + gamePlayers.size() +" of players");
        }

        for (GamePlayer gp : gamePlayers) {
            if (gp.getGamePlayerStatus() == GamePlayerStatus.ACCEPTED) {
                gp.setGamePlayerStatus(GamePlayerStatus.ALIVE);
                em.merge(gp);
            } else if (gp.getGamePlayerStatus().equals(GamePlayerStatus.INVITED)) {
                gp.setGamePlayerStatus(GamePlayerStatus.DECLINED);
                em.merge(gp);
            }
        }

        /* update game players */
        gamePlayers = getGamePlayerForGameAlive(gameId);


        // TODO re enable this later
        gamePlayers = randomEventHandler.shufflePlayer(gamePlayers);

        // assign turn order of game players
        for (int i=0; i<gamePlayers.size(); i++) {
            GamePlayer gp = gamePlayers.get(i);
            if (i==0) {
                gp.setActivePlayerTurn(true);
                gp.setNextGamePlayerIdTurn(gamePlayers.get(i+1).getGamePlayerId());
            } else if(i==gamePlayers.size()-1) {
                gp.setActivePlayerTurn(false);
                gp.setNextGamePlayerIdTurn(gamePlayers.get(0).getGamePlayerId());
            } else {
                gp.setActivePlayerTurn(false);
                gp.setNextGamePlayerIdTurn(gamePlayers.get(i+1).getGamePlayerId());
            }
            em.merge(gp);
        }

        /* assign gameplayers their landAreas */
        int playerIdx = 0;
        for (LandArea landArea : landAreas) {
            System.out.println("idx: " + playerIdx);
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
        game.setStartedTime(new Date());
        em.merge(game);
    }

    private boolean isLessThanMinimumAcceptedPlayers(List<GamePlayer> gamePlayers) {
        int acceptedPlayers = 0;
        for (GamePlayer gp : gamePlayers) {
            if (gp.getGamePlayerStatus().equals(GamePlayerStatus.ACCEPTED)) {
                acceptedPlayers++;
            }
        }

        return (acceptedPlayers < 2);
    }

    @Override
    public boolean isGameFinished(long gameId) {
        @SuppressWarnings("unchecked")  // TODO How to get rid of supress warnings
        List<GamePlayer> gamePlayers = (List<GamePlayer>) em.createNamedQuery(
                "GamePlayer.getGamePlayersFromGameId")
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

    @Override
    public void endGame(long gameId) {
        Game game = em.find(Game.class, gameId);
        game.setGameStatus(GameStatus.FINISHED);
        game.setFinshedTime(new Date());
        em.merge(game);
    }

    @Override
    public boolean isGameStarted(long gameId) {
        // TODO ADD LOGIC HERE
        return true;
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
