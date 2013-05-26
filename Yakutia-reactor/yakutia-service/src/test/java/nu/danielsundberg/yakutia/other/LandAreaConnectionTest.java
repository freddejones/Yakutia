package nu.danielsundberg.yakutia.other;

import junit.framework.Assert;
import nu.danielsundberg.yakutia.application.service.landAreas.LandArea;
import nu.danielsundberg.yakutia.application.service.landAreas.LandAreaConnections;
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
