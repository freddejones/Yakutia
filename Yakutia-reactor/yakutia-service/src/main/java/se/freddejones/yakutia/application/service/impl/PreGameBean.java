package se.freddejones.yakutia.application.service.impl;


import se.freddejones.yakutia.application.service.exceptions.NoPlayerFoundException;
import se.freddejones.yakutia.application.service.iface.PreGameInterface;
import se.freddejones.yakutia.application.service.exceptions.PlayerAlreadyExists;
import se.freddejones.yakutia.entity.Game;
import se.freddejones.yakutia.entity.GamePlayer;
import se.freddejones.yakutia.entity.statuses.GamePlayerStatus;
import se.freddejones.yakutia.entity.statuses.GameStatus;
import se.freddejones.yakutia.entity.Player;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless(mappedName = "preGameBean", name = "pregamebean")
public class PreGameBean implements PreGameInterface {

    @PersistenceContext(name = "yakutiaPU")
    protected EntityManager em;

    @Override
    public boolean playerExists(String email) {
        if(em.createNamedQuery("Player.findPlayerByEmail")
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
    public long createNewGame(long playerId, String name) {
        Game game = new Game();
        game.setGameStatus(GameStatus.CREATED);
        game.setCreationTime(new Date());
        game.setName(name);
        game.setGameCreatorPlayerId(playerId);
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
    public Game getGameById(long gameId) {
        Game g = em.find(Game.class, gameId);
        return g;
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
        List<GamePlayer> gamePlayers = em.createNamedQuery("GamePlayer.getGamePlayersFromPlayerId")
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
    public boolean acceptInvite(long playerId, long gameId) {
        GamePlayer gp;
        try {
            gp = (GamePlayer) em.createNamedQuery("GamePlayer.getGamePlayer")
                .setParameter("gameId",gameId)
                .setParameter("playerId",playerId).getSingleResult();
        } catch (NoResultException nre) {
            return false;
        }

        gp.setGamePlayerStatus(GamePlayerStatus.ACCEPTED);
        em.merge(gp);
        em.flush();

        return true;
    }

    @Override
    public boolean declineInvite(long playerId, long gameId) {
        GamePlayer gp;
        try {
            gp = (GamePlayer) em.createNamedQuery("GamePlayer.getGamePlayer")
                .setParameter("gameId",gameId)
                .setParameter("playerId",playerId).getSingleResult();
        } catch (NoResultException nre) {
            return false;
        }

        gp.setGamePlayerStatus(GamePlayerStatus.DECLINED);
        em.merge(gp);
        em.flush();

        return true;
    }

    @Override
    public List<Player> getPlayers() {
        List<Player> players = em.createNamedQuery("Player.getAllPlayers").getResultList();
        return players;
    }

    @Override
    public Player getPlayerByName(String name) throws NoPlayerFoundException {
        Player p;
        try {
            p = (Player) em.createNamedQuery("Player.findPlayerByName")
                    .setParameter("pName", name)
                    .getSingleResult();
        } catch (NoResultException nre) {
            throw new NoPlayerFoundException("No player named " + name + " found.");
        }
        return p;
    }

    @Override
    public Player getPlayerById(long id) throws NoPlayerFoundException {
        Player p;
        try {
            p = (Player) em.createNamedQuery("Player.findPlayerById")
                    .setParameter("pId", id)
                    .getSingleResult();
        } catch (NoResultException nre) {
            throw new NoPlayerFoundException("No player with id " + id + " found.");
        }
        return p;
    }

    @Override
    public Player getPlayerByEmail(String email) throws NoPlayerFoundException {
        Player p;
        try {
            p = (Player) em.createNamedQuery("Player.findPlayerByEmail")
                    .setParameter("pEmail", email)
                    .getSingleResult();
        } catch (NoResultException nre) {
            throw new NoPlayerFoundException("No player with email " + email + " found.");
        }
        return p;
    }

    @Override
    public void deletePlayerById(long id) {
        Player p;
        p = em.find(Player.class, id);
        em.remove(p);
    }
}
