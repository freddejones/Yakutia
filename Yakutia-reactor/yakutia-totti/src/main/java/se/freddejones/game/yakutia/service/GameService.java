package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.exception.NotEnoughUnitsException;
import se.freddejones.game.yakutia.exception.PlayerAlreadyExistsException;
import se.freddejones.game.yakutia.exception.TerritoryNotConnectedException;
import se.freddejones.game.yakutia.model.YakutiaModel;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.model.dto.GameDTO;
import se.freddejones.game.yakutia.model.dto.GameStateModelDTO;

import java.util.List;

/**
 * User: Fredde
 * Date: 11/30/13 12:12 AM
 */
public interface GameService {

    public Long createNewGame(CreateGameDTO createGameDTO);
    public List<GameDTO> getGamesForPlayerById(Long playerid);
    public List<YakutiaModel> getGameModelForPlayerAndGameId(Long playerId, Long gameId);
    public void setGameToStarted(Long gameId) throws Exception;
    public GameStateModelDTO getGameStateModel(Long gameId, Long playerId);
    public GameStateModelDTO updateStateModel(GameStateModelDTO gameStateModelDTO) throws NotEnoughUnitsException, TerritoryNotConnectedException;
}
