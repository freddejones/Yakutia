package se.freddejones.yakutia.bleh;

import junit.framework.Assert;
import se.freddejones.yakutia.application.service.landAreas.LandArea;
import se.freddejones.yakutia.application.service.landAreas.LandAreaConnections;
import org.junit.Test;

/**
 * User: Fredde
 * Date: 5/26/13 2:29 AM
 */
public class LandAreaConnectionTest {

    @Test
    public void landAreaConnectionTest() {
        Assert.assertTrue(LandAreaConnections.isConnected(LandArea.SVERIGE, LandArea.FINLAND));
        Assert.assertTrue(LandAreaConnections.isConnected(LandArea.SVERIGE, LandArea.NORGE));
        Assert.assertFalse(LandAreaConnections.isConnected(LandArea.FINLAND, LandArea.NORGE));
    }

}
