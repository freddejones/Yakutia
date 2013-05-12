package nu.danielsundberg.yakutia.application.service.impl;

import nu.danielsundberg.yakutia.application.service.exceptions.NotSufficientUnitException;
import nu.danielsundberg.yakutia.application.service.exceptions.PlayerDoNotOwnLandAreaException;
import nu.danielsundberg.yakutia.application.service.iface.PlayerActionsInterface;
import nu.danielsundberg.yakutia.entity.GamePlayer;
import nu.danielsundberg.yakutia.entity.GamePlayerStatus;
import nu.danielsundberg.yakutia.application.service.exceptions.LandIsNotNeighbourException;
import nu.danielsundberg.yakutia.application.service.landAreas.LandArea;
import nu.danielsundberg.yakutia.entity.Unit;
import nu.danielsundberg.yakutia.application.service.landAreas.LandAreaConnections;

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
    public boolean attackLandArea(LandArea attacking, LandArea defending, long gameId) throws LandIsNotNeighbourException {

        if (!LandAreaConnections.isConnected(attacking, defending)) {
            log.info("Not connected");
            throw new LandIsNotNeighbourException("LandAreas not connected");
        }

        List<Unit> attackingUnits = getUnitsByLandArea(attacking, gameId);
        List<Unit> defendingUnits = getUnitsByLandArea(defending, gameId);

        GamePlayer attackingPlayer = attackingUnits.get(0).getGamePlayer();
        GamePlayer defendingGamePlayer = defendingUnits.get(0).getGamePlayer();

        // TODO need to get how many units to attack with
        // maybe just put in a list of Units instead?
        // Hmm something needs to go in the api in that case..


        // Cheat for fun:
        for (Unit defeatedUnit : defendingUnits) {
            em.refresh(defeatedUnit);
            defeatedUnit.setGamePlayer(attackingPlayer);
            defeatedUnit.setStrength(15);
            em.persist(defeatedUnit);
        }

        em.flush();

        em.refresh(defendingGamePlayer);
        if (defendingGamePlayer.getUnits().size() == 0) {
            defendingGamePlayer.setGamePlayerStatus(GamePlayerStatus.DEAD);
            em.merge(defendingGamePlayer);
            log.info(defendingGamePlayer.getPlayer().getName() + " DIED!");
        }

        em.flush();
        return true;
    }

    @Override
    public List<LandArea> getPlayersLandAreas(long playerId, long gameId) {
        GamePlayer gp = (GamePlayer) em.createNamedQuery("GamePlayer.getGamePlayer")
                .setParameter("gameId", gameId)
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
    protected List<Unit> getUnitsByLandArea(LandArea landArea, long gameId) {
        List<Unit> sortedUnits = new ArrayList<Unit>();
        List<Unit> unSortedUnits = em.createNamedQuery("Unit.getUnitsByLandArea")
                .setParameter("laName",landArea)
                .getResultList();

        for (Unit unit : unSortedUnits) {
            if (unit.isSameGameId(gameId)) {
                sortedUnits.add(unit);
            }
        }
        return sortedUnits;
    }

    @Override
    public void moveUnits(long playerId, long gameId) {

    }

    @Override
    public void placeUnits(long playerId, long gameId, LandArea landArea, int unitStrength) throws NotSufficientUnitException, PlayerDoNotOwnLandAreaException {
        GamePlayer gamePlayer = (GamePlayer) em.createNamedQuery("GamePlayer.getGamePlayer")
                .setParameter("gameId",gameId)
                .setParameter("playerId",playerId)
                .getSingleResult();

        List<Unit> units = gamePlayer.getUnits();
        boolean check = false;
        for (Unit u : units) {
            if (u.getLandArea() == landArea) {
                check = true;
            }
        }

        if (!check) {
            throw new PlayerDoNotOwnLandAreaException("Landarea "
                    + landArea.toString() + " not connected to gameplayer "
                    + gamePlayer.getGameId());
        }

        for (Unit unit : units) {
            if (unit.getLandArea() == LandArea.UNASSIGNEDLAND) {
                if (unitStrength > unit.getStrength()) {
                    throw new NotSufficientUnitException("Not enough units");
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

        em.flush();

        for (Unit unit : units) {
            if (unit.getLandArea() == landArea) {
                em.refresh(unit);
                unit.setStrength(unit.getStrength()+unitStrength);
                em.merge(unit);
            }
        }

        em.flush();

        em.refresh(gamePlayer);
    }

    @Override
    public boolean endTurn(long playerId, long gameId)  {
        GamePlayer gamePlayer = (GamePlayer) em.createNamedQuery("GamePlayer.getGamePlayer")
                .setParameter("gameId",gameId)
                .setParameter("playerId",playerId)
                .getSingleResult();

        for (Unit unit : gamePlayer.getUnits()) {
            log.info(gamePlayer.getPlayerId() + " " + unit.getLandArea().toString());
            if (unit.getLandArea() == LandArea.UNASSIGNEDLAND) {
                return false;
            }
        }
        return true;
    }
}
