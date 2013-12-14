package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.exception.PlayerAlreadyExistsException;
import se.freddejones.game.yakutia.model.YakutiaModel;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.model.dto.GameDTO;

import java.util.List;

/**
 * User: Fredde
 * Date: 11/30/13 12:12 AM
 */
public interface GameService {

    public Long createNewGame(CreateGameDTO createGameDTO);

    List<GameDTO> getGamesForPlayerById(Long playerid);
    List<YakutiaModel> getGameModelForPlayerAndGameId(Long playerId, Long gameId);
}
