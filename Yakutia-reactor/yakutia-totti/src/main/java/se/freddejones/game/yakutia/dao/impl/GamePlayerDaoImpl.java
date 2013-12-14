package se.freddejones.game.yakutia.dao.impl;

import org.springframework.stereotype.Repository;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.GamePlayer;
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
    public GamePlayer getGamePlayerByGameIdAndPlayerId(Long playerId, Long gameId) {
        return (GamePlayer) getCurrentSession()
                .getNamedQuery("GamePlayer.getGamePlayersFromPlayerIdAndGameId")
                .setParameter("playerId", playerId)
                .setParameter("gameId", gameId);
    }
}
