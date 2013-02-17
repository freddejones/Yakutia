package nu.danielsundberg.yakutia;

import javax.ejb.Remote;

@Remote
public interface PlayerActionsInterface {

	public boolean attackLandArea();

    public void moveUnits();

    public void placeUnits();

}
