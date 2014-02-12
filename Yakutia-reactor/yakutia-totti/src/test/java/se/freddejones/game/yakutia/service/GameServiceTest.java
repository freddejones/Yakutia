package se.freddejones.game.yakutia.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.freddejones.game.yakutia.dao.GameDao;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.dao.UnitDao;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.exception.NotEnoughPlayersException;
import se.freddejones.game.yakutia.exception.NotEnoughUnitsException;
import se.freddejones.game.yakutia.exception.TerritoryNotConnectedException;
import se.freddejones.game.yakutia.model.LandArea;
import se.freddejones.game.yakutia.model.TerritoryDTO;
import se.freddejones.game.yakutia.model.dto.AttackActionUpdate;
import se.freddejones.game.yakutia.model.dto.GameDTO;
import se.freddejones.game.yakutia.model.dto.GameStateModelDTO;
import se.freddejones.game.yakutia.model.dto.PlaceUnitUpdate;
import se.freddejones.game.yakutia.model.statuses.ActionStatus;
import se.freddejones.game.yakutia.model.statuses.GameStatus;
import se.freddejones.game.yakutia.service.impl.GameServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {

    @Mock private GamePlayerDao gamePlayerDaoMock;
    @Mock private UnitDao unitDaoMock;
    @Mock private GameDao gameDaoMock;
    @InjectMocks private GameServiceImpl gameService;

    private GamePlayer gamePlayerMock;
    private Game gameMock;
    @Before
    public void setup() {
        gamePlayerMock = mock(GamePlayer.class);
        gameMock = mock(Game.class);
    }

    @Test
    public void testPlaceUnitStatusIsReturnedInDTO() throws Exception {
        // Given gameplayer object with place unit status
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong())).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getActionStatus()).thenReturn(ActionStatus.PLACE_UNITS);

        // When: get the state model
        GameStateModelDTO gameStateModelDTO = gameService.getGameStateModel(1L,1L);

        // Then: correct action status i returned
        assertThat(gameStateModelDTO.getState()).isEqualTo(ActionStatus.PLACE_UNITS.toString());
    }

    @Test
    public void testAttackStatusIsReturnedInDTO() throws Exception {
        // Given gameplayer object with attack status
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong())).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getActionStatus()).thenReturn(ActionStatus.ATTACK);

        // When: get the state model
        GameStateModelDTO gameStateModelDTO = gameService.getGameStateModel(1L,1L);

        // Then: correct action status i returned
        assertThat(gameStateModelDTO.getState()).isEqualTo(ActionStatus.ATTACK.toString());
    }

    @Test
    public void testMoveUnitsStatusIsReturnedInDTO() throws Exception {
        // Given gameplayer object with move unit status
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong())).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getActionStatus()).thenReturn(ActionStatus.MOVE);

        // When: get the state model
        GameStateModelDTO gameStateModelDTO = gameService.getGameStateModel(1L,1L);

        // Then: correct action status i returned
        assertThat(gameStateModelDTO.getState()).isEqualTo(ActionStatus.MOVE.toString());

    }

    @Test
    public void testGetZeroGamesForPlayer() throws Exception {
        List<GameDTO> games = gameService.getGamesForPlayerById(1L);
        assertThat(games).isEmpty();
    }

    @Test
    public void testGetOneGameForPlayer() throws Exception {
        // Given a player has one game created
        setupGetGamesForPlayerDefaultMockSettings();

        List<GameDTO> games = gameService.getGamesForPlayerById(1L);

        assertThat(games.size()).isEqualTo(1);
        assertThat(games.get(0).getStatus()).isEqualTo(GameStatus.CREATED.toString());
    }

    @Test
    public void testIfGameCreatorCanStartGame() throws Exception {
        // Given a player has one game created
        setupGetGamesForPlayerDefaultMockSettings();
        when(gameMock.getGameCreatorPlayerId()).thenReturn(1L);

        List<GameDTO> games = gameService.getGamesForPlayerById(1L);

        assertThat(games.get(0).isCanStartGame()).isEqualTo(true);
    }

    @Test
    public void testIfNonGameCreatorCanStartGame() throws Exception {
        // Given a player has one game created
        setupGetGamesForPlayerDefaultMockSettings();
        when(gameMock.getGameCreatorPlayerId()).thenReturn(2L);

        // When: getting games for player
        List<GameDTO> games = gameService.getGamesForPlayerById(1L);

        assertThat(games.get(0).isCanStartGame()).isEqualTo(false);
    }

    @Test
    public void testGetTerritoryInformationReturnsEmpty() throws Exception {
        List<TerritoryDTO> territoryDTOList = gameService.getTerritoryInformationForActiveGame(1L,1L);
        assertThat(territoryDTOList).isEmpty();
    }

    @Test
    public void testGetTerritoryInformationReturnsUnitsOnAGamePlayer() throws Exception {
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(10L, 12L)).thenReturn(gamePlayerMock);
        List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>();
        setupUnitsForGamePlayerMock();
        gamePlayers.add(gamePlayerMock);
        GamePlayer counterPartGamePlayer = mock(GamePlayer.class);
        gamePlayers.add(counterPartGamePlayer);
        when(gamePlayerDaoMock.getGamePlayersByGameId(12L)).thenReturn(gamePlayers);

        List<TerritoryDTO> territoryDTOList = gameService.getTerritoryInformationForActiveGame(10L,12L);

        assertThat(territoryDTOList.size()).isEqualTo(2);
        assertThat(territoryDTOList.get(0).getLandName()).isEqualTo(LandArea.SWEDEN.toString());
        assertThat(territoryDTOList.get(1).getLandName()).isEqualTo(LandArea.UNASSIGNEDLAND.toString());
    }

    @Test
    public void testThatGetTerritoryInformationBelongsToPlayer() throws Exception {
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(10L, 12L)).thenReturn(gamePlayerMock);
        List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>();
        setupUnitsForGamePlayerMock();
        gamePlayers.add(gamePlayerMock);
        GamePlayer counterPartGamePlayer = mock(GamePlayer.class);
        gamePlayers.add(counterPartGamePlayer);
        when(gamePlayerDaoMock.getGamePlayersByGameId(12L)).thenReturn(gamePlayers);

        List<TerritoryDTO> territoryDTOList = gameService.getTerritoryInformationForActiveGame(10L,12L);

        assertThat(territoryDTOList.get(0).isOwnedByPlayer()).isTrue();
        assertThat(territoryDTOList.get(1).isOwnedByPlayer()).isTrue();
    }

    @Test
    public void testThatGetTerritoryInformationCanReturnUnitsNotBeloningToPlayer() throws Exception {
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(10L, 12L)).thenReturn(gamePlayerMock);
        List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>();
        setupUnitsForGamePlayerMock();
        gamePlayers.add(gamePlayerMock);
        GamePlayer counterPartGamePlayer = mock(GamePlayer.class);
        when(counterPartGamePlayer.getGamePlayerId()).thenReturn(45L);
        Unit unit = new Unit();
        unit.setLandArea(LandArea.FINLAND);
        List<Unit> units = new ArrayList<Unit>();
        units.add(unit);
        when(counterPartGamePlayer.getUnits()).thenReturn(units);
        gamePlayers.add(counterPartGamePlayer);
        when(gamePlayerDaoMock.getGamePlayersByGameId(12L)).thenReturn(gamePlayers);

        List<TerritoryDTO> territoryDTOList = gameService.getTerritoryInformationForActiveGame(10L,12L);
        assertThat(territoryDTOList.get(2).isOwnedByPlayer()).isFalse();
    }

    @Test(expected = NotEnoughPlayersException.class)
    public void testNotEnoughPlayerToStartGame() throws Exception {
        // Given: No players received from gamePlayerDao
        when(gamePlayerDaoMock.getGamePlayersByGameId(1L)).thenReturn(new ArrayList<GamePlayer>());

        // When: setting game to started
        gameService.setGameToStarted(1L);

        // Then: exception is thrown
    }

    @Test
    @Ignore
    public void testPlaceUnitUpdateTransitionToAttack() throws Exception {
/*
        // Given: 2 players left in unassigned land
        Unit unitMock = new Unit();
        unitMock.setStrength(2);
        GameStateModelDTO gameStateModelDTO = getGameStateModelDTOWithPlaceUnitUpdateObject();
        gameStateModelDTO.getPlaceUnitUpdate().setNumberOfUnits(2);
        setupUnitsForGamePlayerMock();

        when(gamePlayerDaoMock
                .getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong()))
                .thenReturn(gamePlayerMock);
        when(gamePlayerDaoMock.getUnassignedLand(anyLong())).thenReturn(unitMock);

        // When: updateStateModel service method is called upon
        GameStateModelDTO returnObject = gameService.updateStateModel(gameStateModelDTO);

        // Then: next state for the model should be set
        assertThat(returnObject.getState()).isEqualTo(ActionStatus.ATTACK.toString());
        */
    }

    @Test
    @Ignore
    public void testMatchingLandAreaShouldCallDAOtoUpdateUnits() throws Exception {
        /*
        // Given: a gamestateModel incoming object with placeUnitUpdate object
        GameStateModelDTO gameStateModelDTO = getGameStateModelDTOWithPlaceUnitUpdateObject();
        Unit unitMock = new Unit();
        unitMock.setStrength(2);
        when(gamePlayerDaoMock
                .getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong())).thenReturn(gamePlayerMock);
        when(gamePlayerDaoMock.getUnassignedLand(anyLong())).thenReturn(unitMock);
        setupUnitsForGamePlayerMock();

        // When: gameService updateStateModel service method i called upon
        gameService.updateStateModel(gameStateModelDTO);

        // Then: three methods from gamePlayerDao is called
        verify(gamePlayerDaoMock).getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong());
        verify(gamePlayerDaoMock).getUnassignedLand(anyLong());
        verify(gamePlayerDaoMock, atLeast(2)).setUnitsToGamePlayer(anyLong(), (Unit) anyObject());
        */
    }

    @Test
    @Ignore
    public void testNameAValidUpdateShouldReturnAValidReturnObject() throws Exception {
        /*
        // Given: a gamestateModel incoming object with placeUnitUpdate object
        Unit unitMock = new Unit();
        unitMock.setStrength(2);
        GameStateModelDTO gameStateModelDTO = getGameStateModelDTOWithPlaceUnitUpdateObject();
        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong()))
                .thenReturn(gamePlayerMock);
        when(gamePlayerDaoMock.getUnassignedLand(anyLong())).thenReturn(unitMock);


        // When: gameService updateStateModel service method is called upon
        GameStateModelDTO returnObject = gameService.updateStateModel(gameStateModelDTO);

        // Then: returning object should have values set
        assertThat(returnObject).isNotNull();
        assertThat(returnObject.getGameId()).isNotNull().isEqualTo(1L);
        assertThat(returnObject.getPlayerId()).isNotNull().isEqualTo(1L);
        */
    }

    @Ignore
    @Test(expected = NotEnoughUnitsException.class)
    public void testNotEnoughUnitExceptionIsThrown() throws Exception {
//        // Given: no unassigned units left
//        Unit unitMock = mock(Unit.class);
//        unitMock.setStrength(0);
//        GameStateModelDTO gameStateModelDTO = getGameStateModelDTOWithPlaceUnitUpdateObject();
//        gameStateModelDTO.getPlaceUnitUpdate().setNumberOfUnits(1);
//        setupUnitsForGamePlayerMock();
//        when(gamePlayerDaoMock
//                .getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong()))
//                .thenReturn(gamePlayerMock);
//        when(gamePlayerDaoMock.getUnassignedLand(anyLong())).thenReturn(unitMock);
//
//        // When: gameService updateStateModel service method is called upon
//        gameService.updateStateModel(gameStateModelDTO);
//
//        // Then: Exception
    }

    @Test(expected = TerritoryNotConnectedException.class)
    @Ignore
    public void testAttackingTerritoryNotAllowed() throws Exception {
//        // Given: a update dto with two not connected territories
//        GameStateModelDTO gameStateModelDTO = getGameStateModelDTOWithPlaceUnitUpdateObject();
//        gameStateModelDTO.setState(ActionStatus.ATTACK.toString());
//        AttackActionUpdate attackActionUpdate = getAttackActionUpdateObject();
//        attackActionUpdate.setTerritoryAttackSrc(LandArea.UKRAINA.toString());
//        gameStateModelDTO.setAttackActionUpdate(attackActionUpdate);
//
//        // When: updating state model
//        gameService.updateStateModel(gameStateModelDTO);
//
//        // Then: Exception expected
    }


    @Test
    @Ignore
    public void testUpdateStateModelWhenAttackingTerritory() throws Exception {
//        // Given:
//        GameStateModelDTO gameStateModelDTO = getGameStateModelDTOWithPlaceUnitUpdateObject();
//        gameStateModelDTO.setState(ActionStatus.ATTACK.toString());
//        gameStateModelDTO.setAttackActionUpdate(getAttackActionUpdateObject());
//
//        when(gamePlayerDaoMock.getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong())).thenReturn(gamePlayerMock);
//        when(gamePlayerDaoMock.getGamePlayerByGamePlayerId(anyLong())).thenReturn(gamePlayerMock);
//        setupUnitsForGamePlayerMock();
//        when(unitDaoMock.getGamePlayerIdByLandAreaAndGameId(anyLong(), (LandArea) anyObject())).thenReturn(1L);
//
//        // When:
//        gameService.updateStateModel(gameStateModelDTO);
//
//        // Then: dao methods have been called
//        verify(gamePlayerDaoMock, atLeast(2)).setUnitsToGamePlayer(anyLong(), (Unit) anyObject());
    }

    private void setupGetGamesForPlayerDefaultMockSettings() {
        List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>();
        gamePlayers.add(gamePlayerMock);
        when(gamePlayerDaoMock.getGamePlayersByPlayerId(anyLong())).thenReturn(gamePlayers);
        when(gamePlayerMock.getGameId()).thenReturn(666L);
        when(gameDaoMock.getGameByGameId(666L)).thenReturn(gameMock);
        when(gameMock.getCreationTime()).thenReturn(new Date());
        when(gameMock.getGameStatus()).thenReturn(GameStatus.CREATED);
    }

    private void setupUnitsForGamePlayerMock() {
        List<Unit> unitsForGamePlayer = new ArrayList<Unit>();
        Unit unit = new Unit();
        unit.setLandArea(LandArea.SWEDEN);
        unitsForGamePlayer.add(unit);

        Unit unitUnassigned = new Unit();
        unitUnassigned.setLandArea(LandArea.UNASSIGNEDLAND);
        unitsForGamePlayer.add(unitUnassigned);
        when(gamePlayerMock.getUnits()).thenReturn(unitsForGamePlayer);
    }

    private AttackActionUpdate getAttackActionUpdateObject() {
        AttackActionUpdate attackActionUpdate = new AttackActionUpdate();
        attackActionUpdate.setAttackingNumberOfUnits(5);
        attackActionUpdate.setTerritoryAttackDest(LandArea.SWEDEN.toString());
        attackActionUpdate.setTerritoryAttackSrc(LandArea.NORWAY.toString());
        return attackActionUpdate;
    }

//    private GameStateModelDTO getGameStateModelDTOWithPlaceUnitUpdateObject() {
//        GameStateModelDTO gameStateModelDTO = setupBasicGameStateModel();
//        PlaceUnitUpdate placeUnitUpdate = getPlaceUnitUpdateSimpleSetup();
//        gameStateModelDTO.setPlaceUnitUpdate(placeUnitUpdate);
//        return gameStateModelDTO;
//    }

    private PlaceUnitUpdate getPlaceUnitUpdateSimpleSetup() {
        PlaceUnitUpdate placeUnitUpdate = new PlaceUnitUpdate();
        placeUnitUpdate.setLandArea(LandArea.SWEDEN.toString());
        placeUnitUpdate.setNumberOfUnits(2);
        return placeUnitUpdate;
    }

    private GameStateModelDTO setupBasicGameStateModel() {
        GameStateModelDTO gameStateModelDTO = new GameStateModelDTO();
        gameStateModelDTO.setState(ActionStatus.PLACE_UNITS.toString());
        gameStateModelDTO.setPlayerId(1L);
        gameStateModelDTO.setGameId(1L);
        return gameStateModelDTO;
    }
}
