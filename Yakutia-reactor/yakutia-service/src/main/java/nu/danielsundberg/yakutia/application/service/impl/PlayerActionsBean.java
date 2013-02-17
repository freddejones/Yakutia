package nu.danielsundberg.yakutia.application.service.impl;

import nu.danielsundberg.yakutia.PlayerActionsInterface;
import nu.danielsundberg.yakutia.entity.LandArea;
import nu.danielsundberg.yakutia.entity.Unit;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PlayerActionsBean implements PlayerActionsInterface {

    @PersistenceContext(unitName = "yakutiaPU")
    private EntityManager em;

//    @Override
//    public boolean attackLandArea(LandArea fromArea, LandArea toArea) {
//
//        // måste veta antalet units
//        // samt vilken typ
//
//       Unit attacking = (Unit) em.createNamedQuery("Unit.getUnitsByLandArea")
//               .setParameter("laName",fromArea.toString())
//               .getSingleResult();
//
//       Unit underAttack = (Unit) em.createNamedQuery("Unit.getUnitsByLandArea")
//               .setParameter("laName",toArea.toString())
//               .getSingleResult();
//
//       if (attacking.getStrength() > underAttack.getStrength()) {
//           return true;
//       }
//       return false;
//
//    }

    @Override
    public boolean attackLandArea() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void moveUnits() {

    }

    @Override
    public void placeUnits() {

    }


}
