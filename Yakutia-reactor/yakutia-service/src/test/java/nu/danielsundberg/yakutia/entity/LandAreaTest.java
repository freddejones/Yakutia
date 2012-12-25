package nu.danielsundberg.yakutia.entity;

import nu.danielsundberg.yakutia.testcore.JpaTestCase;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class LandAreaTest extends JpaTestCase{

    @Test
    public void testPersistLandArea() {
        LandArea landArea = new LandArea();
        entityManager.persist(landArea);
        assertNotNull(landArea);
    }

    @Test
    public void testConnectedLandAreas() {
        LandArea landArea = new LandArea();
        LandArea connectedLandArea = new LandArea();
        LandArea notConnectedLandArea = new LandArea();

        Set<LandArea> connected = new HashSet<LandArea>();
        connected.add(connectedLandArea);
        landArea.setNeighbours(connected);

        entityManager.persist(notConnectedLandArea);
        entityManager.persist(connectedLandArea);
        entityManager.persist(landArea);

        assertEquals(1, landArea.getNeighbours().size());
        assertEquals(connectedLandArea.getId(),
                landArea.getNeighbours().iterator().next().getId());
    }
}
