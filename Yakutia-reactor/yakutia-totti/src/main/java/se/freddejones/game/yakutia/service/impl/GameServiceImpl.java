package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.GameDao;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.model.LandArea;
import se.freddejones.game.yakutia.model.UnitType;
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
            GameDTO gameDto = buildGameDTO(playerid, game);
            gamesForPlayer.add(gameDto);
        }
        return gamesForPlayer;
    }

    private GameDTO buildGameDTO(Long playerid, Game game) {
        GameDTO gameDto = new GameDTO();
        gameDto.setId(game.getGameId());
        gameDto.setCanStartGame((playerid == game.getGameCreatorPlayerId()));
        gameDto.setName(game.getName());
        gameDto.setDate(game.getCreationTime().toString());
        gameDto.setStatus(game.getGameStatus().toString());
        return gameDto;
    }

    @Override
    public List<YakutiaModel> getGameModelForPlayerAndGameId(Long playerId, Long gameId) {
        List<YakutiaModel> yakutiaModels = new ArrayList<YakutiaModel>();
        GamePlayer gp = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(playerId, gameId);
        if (gp != null) {
            for (Unit unit : gp.getUnits()) {
                yakutiaModels.add(new YakutiaModel(unit.getLandArea().toString(), unit.getStrength(), true));
            }

        }
        return yakutiaModels;
    }

    @Override
    @Transactional(readOnly = false)
    public void setGameToStarted(Long gameId) {

        List<GamePlayer> gamePlayers = gamePlayerDao.getGamePlayersByGameId(gameId);

        // TODO throw exception
//        if (gamePlayers.isEmpty() || gamePlayers.size() <= 1) {
//
//        }
        List<LandArea> landAreas = getLandAreas();

        for(LandArea landArea : landAreas) {
            Unit u = new Unit();
            u.setLandArea(landArea);
            u.setStrength(5);
            u.setTypeOfUnit(UnitType.TANK);
            gamePlayerDao.setUnitsToGamePlayer(gamePlayers.get(0).getGamePlayerId(), u);
        }

        gameDao.startGame(gameId);
    }

    private List<LandArea> getLandAreas() {
        List<LandArea> landAreas = new ArrayList<LandArea>();
        landAreas.add(LandArea.SWEDEN);
        landAreas.add(LandArea.FINLAND);
        landAreas.add(LandArea.NORWAY);
        return landAreas;
    }
}
