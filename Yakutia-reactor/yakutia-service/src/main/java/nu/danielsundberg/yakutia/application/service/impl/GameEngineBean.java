package nu.danielsundberg.yakutia.application.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import nu.danielsundberg.yakutia.entity.*;
import nu.danielsundberg.yakutia.GameEngineInterface;


@Stateless(mappedName = "kickass")
public class GameEngineBean implements GameEngineInterface {


    @PersistenceContext(name = "yakutiaPU")
    private EntityManager em;

    public List<String> getPlayers() {
        List<String> players = em.createQuery("SELECT p.name from Player p").getResultList();
        return players;
    }

    public void deleteAllPlayers() {
        List<Player> players = em.createQuery("SELECT p from Player p").getResultList();
        for(Player p : players) {
            em.remove(p);
        }
    }

    public long createNewPlayer(String name) {
        Player player = new Player();

        player.setName(name);
        em.persist(player);
        return player.getPlayerId();
    }

	public long startNewGame(List<Long> playerIds) {

		Game game = new Game();
        em.persist(game);

        for (Long id : playerIds) {
            Player play = em.find(Player.class, id);

            GamePlayer gamePlayer = new GamePlayer();
            gamePlayer.setGame(game);
            gamePlayer.setGameId(game.getGameId());
            gamePlayer.setPlayer(play);
            gamePlayer.setPlayerId(play.getPlayerId());
            em.persist(gamePlayer);
            em.refresh(game);
            game.getPlayers().add(gamePlayer);
        }

//        game.setPlayers(players);
        em.merge(game);

        return game.getGameId();
    }

    public void addUnitsToGamePlayer(long gameId, long playerId) {
        List<GamePlayer> gps = em.createNamedQuery("quicky").getResultList();

        System.out.println("Size: " + gps.size());

        Unit unit = new Unit();
        unit.setStrength(15);
        unit.setLandArea(LandArea.SVERIGE);
        unit.setTypeOfUnit(UnitType.TANK);
        unit.setGamePlayer(gps.get(0));
        em.persist(unit);
    }


}
