package nu.danielsundberg.yakutia.application.service.impl;


import nu.danielsundberg.yakutia.application.service.PlayerApi;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.entity.Game;
import nu.danielsundberg.yakutia.entity.GamePlayer;
import nu.danielsundberg.yakutia.entity.GamePlayerStatus;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.application.service.exceptions.PlayerAlreadyExists;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Stateless(mappedName = "preGameBean", name = "pregamebean")
public class PreGameBean implements PreGameInterface {

    @PersistenceContext(name = "yakutiaPU")
    protected EntityManager em;

    @Override
    public boolean playerExists(String email) {
        if(em.createNamedQuery("Player.findPlayerBySearchEmail")
            .setParameter("pEmail", email)
            .getResultList().size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public long createNewPlayer(String name, String email) throws PlayerAlreadyExists {

        if (em.createNamedQuery("Player.findPlayerByName")
                .setParameter("pName", name)
                .getResultList().size() != 0) {
            throw new PlayerAlreadyExists("player already exists");
        }

        Player player = new Player();

        player.setName(name);
        player.setEmail(email);
        em.persist(player);
        return player.getPlayerId();
    }

    @Override
    public void invitePlayerToGame(long playerId, long gameId) {

        Player playerToInvite = (Player) em.createNamedQuery("Player.findPlayerById")
                .setParameter("pId",playerId).getSingleResult();
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
    public List<Player> getPlayers() {
        // TODO fix query for PlayerApi entity
        List<Player> players = em.createNamedQuery("Player.getAllPlayers").getResultList();

        return players;
    }

    @Override
    public String getPlayerByEmail(String email) {
        Player p = (Player) em.createNamedQuery("Player.findPlayerBySearchEmail")
                .setParameter("pEmail", email)
                .getSingleResult();
        System.out.println("PLAYER FOUND: " + p.getName());
        return p.getName();
    }

    @Override
    public PlayerApi getPlayerByName(String name) {
        Player p = (Player) em.createNamedQuery("Player.findPlayerByName")
                .setParameter("pName", name)
                .getSingleResult();
        PlayerApi pApi = new PlayerApi();
        pApi.setPlayerId(p.getPlayerId());
        pApi.setPlayerName(p.getName());
        return pApi;
    }

    @Override
    public PlayerApi getPlayerById(long id) {
        Player p = (Player) em.createNamedQuery("Player.findPlayerById")
                .setParameter("pId", id)
                .getSingleResult();
        PlayerApi pApi = new PlayerApi();
        pApi.setPlayerId(p.getPlayerId());
        pApi.setPlayerName(p.getName());
        return pApi;
    }
//    @Override
//    public void deleteAllPlayers() {
//        // TODO fix query for PlayerApi entity
//        List<PlayerApi> players = em.createQuery("SELECT p from PlayerApi p").getResultList();
//        for(PlayerApi p : players) {
//            em.remove(p);
//        }
//    }
}
