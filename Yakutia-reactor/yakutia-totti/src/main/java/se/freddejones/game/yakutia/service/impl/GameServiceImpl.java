package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.GameDao;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.model.YakutiaModel;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.model.dto.GameDTO;
import se.freddejones.game.yakutia.service.GameService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service("gameservice")
@Transactional(readOnly = true)
public class GameServiceImpl implements GameService {

    private Logger log = Logger.getLogger(GameServiceImpl.class.getName());

    @Autowired
    protected GameDao gameDao;
    @Autowired
    protected GamePlayerDao gamePlayerDao;


    @Override
    @Transactional(readOnly = false)
    public Long createNewGame(CreateGameDTO createGameDTO) {
        return gameDao.createNewGame(createGameDTO.getCreatedByPlayerId(),
                createGameDTO.getGameName());
    }

    @Override
    public List<GameDTO> getGamesForPlayerById(Long playerid) {
        List<GameDTO> gamesForPlayer = new ArrayList<GameDTO>();
        List<GamePlayer> gamePlayersList = gamePlayerDao.getGamePlayersByPlayerId(playerid);
        for(GamePlayer gamePlayer : gamePlayersList) {
            Game game = gameDao.getGameByGameId(gamePlayer.getGameId());
            GameDTO gameDto = new GameDTO();
            gameDto.setName(game.getName());
            gameDto.setDate(game.getCreationTime().toString());
            gameDto.setStatus(game.getGameStatus().toString());
            gamesForPlayer.add(gameDto);
        }
        return gamesForPlayer;
    }

    @Override
    public List<YakutiaModel> getGameModelForPlayerAndGameId(Long playerId, Long gameId) {
        GamePlayer gp = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(playerId, gameId);
        if (gp != null) {
            gameDao.getGameByGameId(gp.getGameId());
        }
        ArrayList<YakutiaModel> yakutiaModels = new ArrayList<YakutiaModel>();
        yakutiaModels.add(new YakutiaModel("sweden", 12, true));
        yakutiaModels.add(new YakutiaModel("norway", 5, true));
        yakutiaModels.add(new YakutiaModel("finland", 45, false));
        return yakutiaModels;
    }
}
