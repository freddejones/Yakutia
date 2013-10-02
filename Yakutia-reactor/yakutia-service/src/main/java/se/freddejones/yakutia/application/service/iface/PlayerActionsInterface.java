package se.freddejones.yakutia.application.service.iface;

import se.freddejones.yakutia.application.service.exceptions.NotSufficientUnitException;
import se.freddejones.yakutia.application.service.exceptions.PlayerDoNotOwnLandAreaException;
import se.freddejones.yakutia.application.service.landAreas.LandArea;
import se.freddejones.yakutia.application.service.exceptions.LandIsNotNeighbourException;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface PlayerActionsInterface {

    boolean attackLandArea(LandArea from, LandArea to, long gameId) throws LandIsNotNeighbourException;

    List<LandArea> getPlayersLandAreas(long playerId, long gameId);

    void moveUnits(long playerId, long gameId);

    void placeUnits(long playerId, long gameId, LandArea landArea, int extraUnits) throws NotSufficientUnitException, PlayerDoNotOwnLandAreaException;

    boolean endTurn(long playerId, long gameId);

    boolean isPlayerTurn(long playerId, long gameId);
}
