package nu.danielsundberg.yakutia;

import nu.danielsundberg.yakutia.exceptions.LandIsNotNeighbourException;
import nu.danielsundberg.yakutia.exceptions.TurnCannotBeEndedException;
import nu.danielsundberg.yakutia.landAreas.LandArea;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface PlayerActionsInterface {

	public boolean attackLandArea(LandArea from, LandArea to) throws LandIsNotNeighbourException;

    public List<LandArea> getPlayersLandAreas(long PlayerId, long gameId);

    public void moveUnits();

    public void placeUnits();

    public void endTurn() throws TurnCannotBeEndedException;

}
