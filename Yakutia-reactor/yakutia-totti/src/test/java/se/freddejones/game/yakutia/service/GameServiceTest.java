package se.freddejones.game.yakutia.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Unit;
import se.freddejones.game.yakutia.exception.NotEnoughUnitsException;
import se.freddejones.game.yakutia.model.LandArea;
import se.freddejones.game.yakutia.model.dto.GameStateModelDTO;
import se.freddejones.game.yakutia.model.dto.PlaceUnitUpdate;
import se.freddejones.game.yakutia.model.statuses.ActionStatus;
import se.freddejones.game.yakutia.service.impl.GameServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {

    @Mock private GamePlayerDao gamePlayerDaoMock;
    @InjectMocks private GameServiceImpl gameService;

    private GamePlayer gamePlayerMock;
    @Before
    public void setup() {
        gamePlayerMock = mock(GamePlayer.class);
    }

    @Test
    public void testPlaceUnitUpdateTransitionToAttack() throws Exception {

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
    }

    @Test
    public void testMatchingLandAreaShouldCallDAOtoUpdateUnits() throws NotEnoughUnitsException {
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
    }

    @Test
    public void testNameAValidUpdateShouldReturnAValidReturnObject() throws Exception {
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
    }

    @Test(expected = NotEnoughUnitsException.class)
    public void testNotEnoughUnitExceptionIsThrown() throws Exception {
        // Given: no unassigned units left
        Unit unitMock = mock(Unit.class);
        unitMock.setStrength(0);
        GameStateModelDTO gameStateModelDTO = getGameStateModelDTOWithPlaceUnitUpdateObject();
        gameStateModelDTO.getPlaceUnitUpdate().setNumberOfUnits(1);
        setupUnitsForGamePlayerMock();
        when(gamePlayerDaoMock
                .getGamePlayerByGameIdAndPlayerId(anyLong(), anyLong()))
                .thenReturn(gamePlayerMock);
        when(gamePlayerDaoMock.getUnassignedLand(anyLong())).thenReturn(unitMock);

        // When: gameService updateStateModel service method is called upon
        gameService.updateStateModel(gameStateModelDTO);
    }

    private void setupUnitsForGamePlayerMock() {
        List<Unit> unitsForGamePlayer = new ArrayList<Unit>();
        Unit unit = new Unit();
        unit.setLandArea(LandArea.SWEDEN);
        unitsForGamePlayer.add(unit);
        when(gamePlayerMock.getUnits()).thenReturn(unitsForGamePlayer);
    }

    private GameStateModelDTO getGameStateModelDTOWithPlaceUnitUpdateObject() {
        GameStateModelDTO gameStateModelDTO = setupBasicGameStateModel();
        PlaceUnitUpdate placeUnitUpdate = getPlaceUnitUpdateSimpleSetup();
        gameStateModelDTO.setPlaceUnitUpdate(placeUnitUpdate);
        return gameStateModelDTO;
    }

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
