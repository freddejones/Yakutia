package nu.danielsundberg.yakutia.application.service.impl;


import nu.danielsundberg.yakutia.GameplayerId;
import nu.danielsundberg.yakutia.PreGameInterface;
import nu.danielsundberg.yakutia.entity.Game;
import nu.danielsundberg.yakutia.entity.GamePlayer;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.exceptions.PlayerAlreadyExists;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    public void invitePlayerToGame(String playerName) {
        // TODO fix invitePlayersToGame
    }


    @Override
    public boolean acceptInvite(long playerId, long gameId) {
        Game gameToAccept = em.find(Game.class, gameId);
        Player player = em.find(Player.class, playerId);
        // TODO GameNotFoundException
        // TODO PlayerNotFoundException

        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setGame(gameToAccept);
        gamePlayer.setGameId(gameToAccept.getGameId());
        gamePlayer.setPlayer(player);
        gamePlayer.setPlayerId(player.getPlayerId());
        em.persist(gamePlayer);
        em.refresh(gameToAccept);
        gameToAccept.getPlayers().add(gamePlayer);
        em.merge(gameToAccept);

        return true;
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
