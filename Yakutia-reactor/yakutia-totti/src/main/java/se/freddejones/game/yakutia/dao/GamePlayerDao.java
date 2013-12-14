package se.freddejones.game.yakutia.dao;

import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.model.dto.GameDTO;

import java.util.List;

/**
 * User: Fredde
 * Date: 12/9/13 11:09 PM
 */
public interface GamePlayerDao {

    List<GamePlayer> getGamePlayersByPlayerId(Long playerId);
    GamePlayer getGamePlayerByGameIdAndPlayerId(Long playerId, Long gameId);

}
