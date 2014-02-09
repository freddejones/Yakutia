package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.GameDao;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.dao.UnitDao;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.exception.NoGameFoundException;
import se.freddejones.game.yakutia.exception.NotEnoughUnitsException;
import se.freddejones.game.yakutia.exception.TerritoryNotConnectedException;
import se.freddejones.game.yakutia.model.*;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.model.dto.GameDTO;
import se.freddejones.game.yakutia.model.dto.GameStateModelDTO;
import se.freddejones.game.yakutia.model.statuses.ActionStatus;
import se.freddejones.game.yakutia.service.GameService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static se.freddejones.game.yakutia.model.GameManager.getLandAreas;

@Service("gameservice")
@Transactional(readOnly = true)
public class GameServiceImpl implements GameService {

    private Logger log = Logger.getLogger(GameServiceImpl.class.getName());

    @Autowired
    protected GameDao gameDao;
    @Autowired
    protected GamePlayerDao gamePlayerDao;
    @Autowired
    protected UnitDao unitDao;


    @Override
    @Transactional(readOnly = false)
    public Long createNewGame(CreateGameDTO createGameDTO) {
        return gameDao.createNewGame(createGameDTO);
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
        List<GamePlayer> gamePlayers = gamePlayerDao.getGamePlayersByGameId(gameId);
        for (GamePlayer gamePlayer : gamePlayers) {
            if (gp.getGamePlayerId() == gamePlayer.getGamePlayerId()) {
                for (Unit unit : gamePlayer.getUnits()) {
                    yakutiaModels.add(new YakutiaModel(unit.getLandArea().toString(), unit.getStrength(), true));
                }
            } else {
                for (Unit unit : gamePlayer.getUnits()) {
                    if (unit.getLandArea() != LandArea.UNASSIGNEDLAND) {
                        yakutiaModels.add(new YakutiaModel(unit.getLandArea().toString(), unit.getStrength(), false));
                    }
                }
            }
        }

        return yakutiaModels;
    }

    @Override
    @Transactional(readOnly = false)
    public void setGameToStarted(Long gameId) throws Exception {

        List<GamePlayer> gamePlayers = gamePlayerDao.getGamePlayersByGameId(gameId);

        // TODO throw exception
        if (gamePlayers.isEmpty() || gamePlayers.size() <= 1) {
            throw new Exception("Fix this exception too");
        }
        // TODO Maximum number of players
        if (gamePlayers.size() > getLandAreas().size()) {
            throw new Exception("Fix this exception");
        }

        // TODO check for minimum accepted players


        // TODO extract proper code to elsewhere
        ArrayList<GameSetupStuff> gamePlayersSetup = new ArrayList<GameSetupStuff>();

        for (GamePlayer gp : gamePlayers) {
            GameSetupStuff gss = new GameSetupStuff();
            gss.setGp(gp);
            gss.setUnits(new ArrayList<Unit>());
            gss.setTotalNumberOfUnits(10);
            gamePlayersSetup.add(gss);
        }

        List<LandArea> landAreas = getLandAreas();
        int gamePlayerCounter = 0;
        for(LandArea landArea : landAreas) {
            Unit u = new Unit();
            u.setLandArea(landArea);
            u.setStrength(0);
            u.setTypeOfUnit(UnitType.TANK);
            gamePlayersSetup.get(gamePlayerCounter).getUnits().add(u);
            gamePlayerCounter++;
            if (gamePlayerCounter == gamePlayers.size()) {
                gamePlayerCounter = 0;
            }
        }

        for (GameSetupStuff gss : gamePlayersSetup) {

            while(gss.getTotalNumberOfUnits() != 0) {
                for (Unit unit : gss.getUnits()) {
                    if (gss.getTotalNumberOfUnits() < 5) {
                        int currentStrength = unit.getStrength();
                        int newStrength = currentStrength + gss.getTotalNumberOfUnits();
                        unit.setStrength(newStrength);
                        gss.setTotalNumberOfUnits(gss.getTotalNumberOfUnits()-newStrength);
                    } else {
                        unit.setStrength(unit.getStrength()+5);
                        gss.setTotalNumberOfUnits(gss.getTotalNumberOfUnits()-5);
                    }
                }
            }

            for (Unit u : gss.getUnits()) {
                gamePlayerDao.setUnitsToGamePlayer(gss.getGp().getGamePlayerId(), u);
            }

            Unit reinforcementUnit = new Unit();
            reinforcementUnit.setStrength(3);
            reinforcementUnit.setTypeOfUnit(UnitType.TANK);
            reinforcementUnit.setLandArea(LandArea.UNASSIGNEDLAND);
            gamePlayerDao.setUnitsToGamePlayer(gss.getGp().getGamePlayerId(), reinforcementUnit);
        }
        gameDao.startGame(gameId);
    }

    @Override
    public GameStateModelDTO getGameStateModel(Long gameId, Long playerId) {
        GamePlayer gamePlayer = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(playerId, gameId);

        GameStateModelDTO gameStateModelDTO = new GameStateModelDTO();
        gameStateModelDTO.setGameId(gameId);
        gameStateModelDTO.setPlayerId(playerId);

        if (gamePlayer.getActionStatus() == null ||
                gamePlayer.getActionStatus() == ActionStatus.PLACE_UNITS) {
            gameStateModelDTO.setState(ActionStatus.PLACE_UNITS.toString());
        } else if (gamePlayer.getActionStatus() == ActionStatus.ATTACK) {
            gameStateModelDTO.setState(ActionStatus.ATTACK.toString());
        }

        return gameStateModelDTO;
    }

    @Override
    @Transactional(readOnly = false)
    public GameStateModelDTO updateStateModel(GameStateModelDTO gameStateModelDTO)
            throws NotEnoughUnitsException, TerritoryNotConnectedException {
        GamePlayer gamePlayer = gamePlayerDao.getGamePlayerByGameIdAndPlayerId(
                gameStateModelDTO.getPlayerId(), gameStateModelDTO.getGameId());

        if (ActionStatus.PLACE_UNITS.toString().equals(gameStateModelDTO.getState())) {
            placeUnitUpdate(gameStateModelDTO, gamePlayer);
        } else if (ActionStatus.ATTACK.toString().equals(gameStateModelDTO.getState())) {
            attackTerritory(gameStateModelDTO, gamePlayer);
        }

        return gameStateModelDTO;
    }

    private void attackTerritory(GameStateModelDTO gameStateModelDTO, GamePlayer gamePlayer) throws TerritoryNotConnectedException {
        gameStateModelDTO.getAttackActionUpdate();
        LandArea attackingTerritory = LandArea.translateLandArea(gameStateModelDTO.getAttackActionUpdate().getTerritoryAttackSrc());
        LandArea defendingTerritory = LandArea.translateLandArea(gameStateModelDTO.getAttackActionUpdate().getTerritoryAttackDest());

        if (!GameManager.isTerritoriesConnected(attackingTerritory, defendingTerritory)) {
            throw new TerritoryNotConnectedException(String.format("[%s] not connected to [%s]",
                    attackingTerritory.toString(), defendingTerritory.toString()));
        }

        Long defendingGamePlayerId = unitDao.getGamePlayerIdByLandAreaAndGameId(
                gamePlayer.getGameId(), defendingTerritory);

        GamePlayer defendingGamePlayer = gamePlayerDao.getGamePlayerByGamePlayerId(defendingGamePlayerId);
        for (Unit u : defendingGamePlayer.getUnits()) {
            if (defendingTerritory.equals(u.getLandArea())) {
                int remainingUnit = u.getStrength() - gameStateModelDTO.getAttackActionUpdate().getAttackingNumberOfUnits();
                u.setStrength(remainingUnit);
                gamePlayerDao.setUnitsToGamePlayer(defendingGamePlayerId, u);

                if (remainingUnit <= 0) {
                    // Change owner of territory
                    u.setGamePlayer(gamePlayer);
                    gamePlayerDao.setUnitsToGamePlayer(gamePlayer.getGamePlayerId(), u);
                }

            }
        }
    }

    private void placeUnitUpdate(GameStateModelDTO gameStateModelDTO, GamePlayer gamePlayer) throws NotEnoughUnitsException {
        Unit unassignedLandUnit = gamePlayerDao.getUnassignedLand(gamePlayer.getGamePlayerId());
        LandArea landAreaInRequest = LandArea.translateLandArea(
                gameStateModelDTO.getPlaceUnitUpdate().getLandArea());

        if (unassignedLandUnit.getStrength() - gameStateModelDTO.getPlaceUnitUpdate().getNumberOfUnits() < 0) {
            throw new NotEnoughUnitsException("Insufficient units for doing PLACE UNIT operation");
        }

        for (Unit unit : gamePlayer.getUnits()) {
            if (unit.getLandArea() == landAreaInRequest) {
                int numberOfUnits = gameStateModelDTO.getPlaceUnitUpdate().getNumberOfUnits();
                unit.setStrength(unit.getStrength() + numberOfUnits);
                gamePlayerDao.setUnitsToGamePlayer(gamePlayer.getGamePlayerId(), unit);
                unassignedLandUnit.setStrength(unassignedLandUnit.getStrength()-numberOfUnits);
                gamePlayerDao.setUnitsToGamePlayer(gamePlayer.getGamePlayerId(), unassignedLandUnit);
                if (unassignedLandUnit.getStrength() == 0) {
                    gameStateModelDTO.setState(ActionStatus.ATTACK.toString());
                    gamePlayerDao.setActionStatus(gamePlayer.getGamePlayerId(), ActionStatus.ATTACK);
                }
            }
        }
    }
}
