package nu.danielsundberg.yakutia.application.service.impl;

import nu.danielsundberg.yakutia.application.service.PlayerActionsInterface;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PlayerActionsBean implements PlayerActionsInterface {

    @PersistenceContext(unitName = "yakutiaPU")
    private EntityManager em;

    @Override
    public void attackLandArea(long playerId, int landAreaIdFrom, int landAreaIdTo) {

        // TODO fix the domain model before implementing anything here

    }

    @Override
    public void moveUnits() {
        // TODO implement this stuff
    }


}
