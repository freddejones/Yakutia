package nu.danielsundberg.yakutia.entity;

import junit.framework.Assert;
import org.junit.Test;

/**
 * User: Fredde
 * Date: 5/9/13 10:53 AM
 */
public class UnitTypeTest {

    @Test
    public void testReturnString() {
        String actual = UnitType.TANK.toString();
        Assert.assertEquals("TANK", actual);
    }

}
