package nu.danielsundberg.yakutia.application.service.iface;

import nu.danielsundberg.yakutia.application.service.exceptions.LandIsNotNeighbourException;
import nu.danielsundberg.yakutia.application.service.exceptions.NotSufficientUnitException;
import nu.danielsundberg.yakutia.application.service.exceptions.PlayerDoNotOwnLandAreaException;
import nu.danielsundberg.yakutia.application.service.landAreas.LandArea;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface PlayerActionsInterface {

	public boolean attackLandArea(LandArea from, LandArea to, long gameId) throws LandIsNotNeighbourException;

    public List<LandArea> getPlayersLandAreas(long playerId, long gameId);

    public void moveUnits(long playerId, long gameId);

    public void placeUnits(long playerId, long gameId, LandArea landArea, int extraUnits) throws NotSufficientUnitException, PlayerDoNotOwnLandAreaException;

    public boolean endTurn(long playerId, long gameId);

    public boolean isPlayerTurn(long playerId, long gameId);

}
