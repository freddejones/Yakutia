package nu.danielsundberg.yakutia.application.service.impl;


import nu.danielsundberg.yakutia.GameplayerId;
import nu.danielsundberg.yakutia.PreGameInterface;
import nu.danielsundberg.yakutia.entity.Game;
import nu.danielsundberg.yakutia.entity.GamePlayer;
import nu.danielsundberg.yakutia.entity.GamePlayerStatus;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.exceptions.PlayerAlreadyExists;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Stateless(mappedName = "preGameBean")
public class PreGameBean implements PreGameInterface {

    @PersistenceContext(name = "yakutiaPU")
    protected EntityManager em;

    @Override
    public long createNewPlayer(String name) throws PlayerAlreadyExists {

        if (em.createNamedQuery("Player.findPlayerByName")
                .setParameter("pName", name)
                .getResultList().size() != 0) {
            throw new PlayerAlreadyExists("player already exists");
        }

        Player player = new Player();

        player.setName(name);
        em.persist(player);
        return player.getPlayerId();
    }

    @Override
    public void invitePlayerToGame(String playerName, long gameId) {
        // TODO fix invitePlayersToGame
        playerName = "%" + playerName + "%";
        Player playerToInvite = (Player) em.createNamedQuery("Player.findPlayerBySearchName")
                .setParameter("pName",playerName).getSingleResult();
        Game gameToInvite = em.find(Game.class, gameId);

        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setGame(gameToInvite);
        gamePlayer.setGameId(gameToInvite.getGameId());
        gamePlayer.setPlayer(playerToInvite);
        gamePlayer.setPlayerId(playerToInvite.getPlayerId());
        gamePlayer.setGamePlayerStatus(GamePlayerStatus.INVITED);

        em.persist(gamePlayer);
        em.refresh(gameToInvite);
        gameToInvite.getPlayers().add(gamePlayer);
        em.merge(gameToInvite);

    }

    @Override
    public List<Long> getInvites(long playerId) {
        @SuppressWarnings("unchecked") //TODO REMOVE UNCHECKED WARNING
        List<GamePlayer> gamePlayers = em.createNamedQuery("GamePlayer.getGamePlayerFromPlayerId")
                .setParameter("playerId", playerId).getResultList();
        List<Long> gameIds = new ArrayList<Long>();
        for (GamePlayer gp : gamePlayers) {
            if (gp.getGamePlayerStatus() == GamePlayerStatus.INVITED) {
                gameIds.add(gp.getGameId());
            }

        }
        return gameIds;
    }


    @Override
    public void acceptInvite(long playerId, long gameId) {
        GamePlayer gp = (GamePlayer) em.createNamedQuery("GamePlayer.getGamePlayer")
                .setParameter("gameId",gameId)
                .setParameter("playerId",playerId).getSingleResult();

        gp.setGamePlayerStatus(GamePlayerStatus.ACCEPTED);
        em.merge(gp);
        em.flush();
    }

    @Override
    public void declineInvite(long playerId, long gameId) {
        GamePlayer gp = (GamePlayer) em.createNamedQuery("GamePlayer.getGamePlayer")
                .setParameter("gameId",gameId)
                .setParameter("playerId",playerId).getSingleResult();

        gp.setGamePlayerStatus(GamePlayerStatus.DECLINED);
        em.merge(gp);
        em.flush();
    }

    @Override
    public List<String> getPlayers() {
        // TODO fix query for Player entity
        List<String> players = em.createQuery("SELECT p.name from Player p").getResultList();
        return players;
    }

    @Override
    public void deleteAllPlayers() {
        // TODO fix query for Player entity
        List<Player> players = em.createQuery("SELECT p from Player p").getResultList();
        for(Player p : players) {
            em.remove(p);
        }
    }
}
