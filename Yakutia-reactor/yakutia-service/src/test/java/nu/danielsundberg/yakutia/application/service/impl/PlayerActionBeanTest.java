package nu.danielsundberg.yakutia.application.service.impl;

import nu.danielsundberg.yakutia.application.service.exceptions.LandIsNotNeighbourException;
import nu.danielsundberg.yakutia.application.service.landAreas.LandArea;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

public class PlayerActionBeanTest {

    @Test(expected = LandIsNotNeighbourException.class)
    public void attackLandAreasNotConnected() throws LandIsNotNeighbourException {
        PlayerActionsBean playerActionsBean = new PlayerActionsBean();
        playerActionsBean.attackLandArea(LandArea.FINLAND, LandArea.NORGE);
    }

    @Test
    public void attackingLandAreaWins() throws LandIsNotNeighbourException {
//        PlayerActionsBean playerActionsBean = new PlayerActionsBean();
//        playerActionsBean.em = mock(EntityManager.class);
//
//        List<Unit> units = new ArrayList<Unit>();
//        Unit u1 = new Unit();
//        u1.setLandArea(LandArea.SVERIGE);
//        u1.setStrength(1);
//        u1.setTypeOfUnit(UnitType.TANK);
//        units.add(u1);
//
//        Query queryMockResultList = mock(Query.class);
//        Query queryMockQuery = mock(Query.class);
//
//        when(queryMockResultList.getResultList()).thenReturn(units);
//        when(queryMockQuery.setParameter(anyString(), anyString()))
//                .thenReturn(queryMockResultList);
//        when(playerActionsBean.em.createNamedQuery(anyString()))
//                .thenReturn(queryMockQuery);
//
//        when(playerActionsBean.em.persist(any()).;)
//
//        assertTrue("Not a win. should be a win..",
//                playerActionsBean.attackLandArea(LandArea.SVERIGE, LandArea.FINLAND));
    }



}
