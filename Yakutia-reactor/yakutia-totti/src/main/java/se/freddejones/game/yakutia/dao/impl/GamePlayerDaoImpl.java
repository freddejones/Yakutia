package se.freddejones.game.yakutia.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.LandArea;

import java.util.List;

@Repository
public class GamePlayerDaoImpl extends AbstractDaoImpl implements GamePlayerDao {

    @Override
    public List<GamePlayer> getGamePlayersByPlayerId(Long playerId) {
        return (List<GamePlayer>) getCurrentSession()
                .getNamedQuery("GamePlayer.getGamePlayersFromPlayerId")
                .setParameter("playerId", playerId).list();
    }

    @Override
    public List<GamePlayer> getGamePlayersByGameId(Long gameId) {
        return (List<GamePlayer>) getCurrentSession()
                .getNamedQuery("GamePlayer.getGamePlayersForGameId")
                .setParameter("gameId", gameId).list();
    }

    @Override
    public GamePlayer getGamePlayerByGameIdAndPlayerId(Long playerId, Long gameId) {
        return (GamePlayer) getCurrentSession()
                .getNamedQuery("GamePlayer.getGamePlayersFromPlayerIdAndGameId")
                .setParameter("playerId", playerId)
                .setParameter("gameId", gameId).uniqueResult();
    }

    @Override
    public void setUnitsToGamePlayer(Long gamePlayerId, Unit unit) {
        Session session = getCurrentSession();
        GamePlayer gamePlayer = (GamePlayer) session.get(GamePlayer.class, gamePlayerId);
        unit.setGamePlayer(gamePlayer);
        session.saveOrUpdate(unit);
    }

    @Override
    public Unit getUnassignedLand(Long gamePlayerId) {
        GamePlayer gamePlayer = (GamePlayer) getCurrentSession().get(GamePlayer.class, gamePlayerId);
        for (Unit unit : gamePlayer.getUnits()) {
            if (unit.getLandArea() == LandArea.UNASSIGNEDLAND) {
                return unit;
            }
        }
        return new Unit();
    }


}
