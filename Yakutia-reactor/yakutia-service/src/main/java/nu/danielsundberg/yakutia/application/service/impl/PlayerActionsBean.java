package nu.danielsundberg.yakutia.application.service.impl;

import nu.danielsundberg.yakutia.PlayerActionsInterface;
import nu.danielsundberg.yakutia.entity.GamePlayer;
import nu.danielsundberg.yakutia.exceptions.LandIsNotNeighbourException;
import nu.danielsundberg.yakutia.landAreas.LandArea;
import nu.danielsundberg.yakutia.entity.Unit;
import nu.danielsundberg.yakutia.exceptions.TurnCannotBeEndedException;
import nu.danielsundberg.yakutia.landAreas.LandAreaConnections;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Stateless(mappedName = "playerActionsBean")
public class PlayerActionsBean implements PlayerActionsInterface {

    private Logger log = Logger.getLogger(PlayerActionsBean.class.getName());


    @PersistenceContext(unitName = "yakutiaPU")
    protected EntityManager em;

    @Override
    public boolean attackLandArea(LandArea attacking, LandArea defending) throws LandIsNotNeighbourException {

        if (!LandAreaConnections.isConnected(attacking, defending)) {
            log.info("Not connected");
            throw new LandIsNotNeighbourException("LandAreas not connected");
        }

        List<Unit> attackingUnits = getUnits(attacking);
        List<Unit> defendingUnits = getUnits(defending);

        // TODO need to get how many units to attack with
        // maybe just put in a list of Units instead?
        // Hmm something needs to go in the api in that case..


        // TODO get a nicer way to get gamePlayer
        GamePlayer attackingPlayer = attackingUnits.get(0).getGamePlayer();

        // Cheat for fun:
        for (Unit defeatedUnit : defendingUnits) {
            em.refresh(defeatedUnit);
            defeatedUnit.setGamePlayer(attackingPlayer);
            defeatedUnit.setStrength(15);
            em.persist(defeatedUnit);
        }

        return true;
    }

    @Override
    public List<LandArea> getPlayersLandAreas(long playerId, long gameId) {
        GamePlayer gp = (GamePlayer) em.createNamedQuery("GamePlayer.getGamePlayer")
                .setParameter("gameId",gameId)
                .setParameter("playerId", playerId)
                .getSingleResult();

        em.refresh(gp);

        List<LandArea> landAreas = new ArrayList<LandArea>();
        for (Unit u : gp.getUnits()) {
            landAreas.add(u.getLandArea());
        }
        return landAreas;
    }

    // TODO how to fix this nice???
    @SuppressWarnings (value="unchecked")
    private List<Unit> getUnits(LandArea landArea) {
        return em.createNamedQuery("Unit.getUnitsByLandArea")
                .setParameter("laName",landArea)
                .getResultList();
    }

    @Override
    public void moveUnits(long playerId, long gameId) {

    }

    @Override
    public void placeUnits(long playerId, long gameId, LandArea landArea, int unitStrength) {
        GamePlayer gamePlayer = (GamePlayer) em.createNamedQuery("GamePlayer.getGamePlayer")
                .setParameter("gameId",gameId)
                .setParameter("playerId",playerId)
                .getSingleResult();

        List<Unit> units = gamePlayer.getUnits();
        // TODO check that unitsStrength can be done

        for (Unit unit : units) {
            log.info(unit.getLandArea().toString());
            if (unit.getLandArea() == LandArea.UNASSIGNEDLAND) {
                if (unitStrength > unit.getStrength()) {
                    //TODO Throw new exception
                    log.info("Throw exception");
                    continue; //TODO REMOVE after fixed exception
                }
                em.refresh(unit);
                unit.setStrength(unit.getStrength()-unitStrength);
                if (unit.getStrength() == 0) {
                    em.remove(unit);
                } else {
                    em.merge(unit);
                }
            }
        }

        for (Unit unit : units) {
            if (unit.getLandArea() == landArea) {
                em.refresh(unit);
                unit.setStrength(unit.getStrength()+unitStrength);
                em.merge(unit);
            }
        }

    }

    @Override
    public void endTurn(long playerId, long gameId) throws TurnCannotBeEndedException {
        // TODO end Turn

        // Check that no empty units exists

    }
}
