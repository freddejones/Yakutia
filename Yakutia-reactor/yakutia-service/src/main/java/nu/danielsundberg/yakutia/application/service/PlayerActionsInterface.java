package nu.danielsundberg.yakutia.application.service;

import java.util.Set;

import javassist.NotFoundException;
import nu.danielsundberg.yakutia.entity.LandArea;

import javax.ejb.Remote;
import javax.naming.NamingException;

@Remote
public interface PlayerActionsInterface {

	public void attackLandArea(long playerId, int landAreaIdFrom, int landAreaIdTo);

    // move units between land areas
    public void moveUnits();

}
