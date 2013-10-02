package se.freddejones.yakutia.entity.statuses;

import junit.framework.Assert;
import org.junit.Test;

/**
 * User: Fredde
 * Date: 9/14/13 5:07 PM
 */
public class StatusesTest {

    @Test
    public void testGetStringForGamePlayerStatus() {
        Assert.assertEquals("INVITED",GamePlayerStatus.INVITED.toString());
        Assert.assertEquals("DECLINED",GamePlayerStatus.DECLINED.toString());
        Assert.assertEquals("DEAD",GamePlayerStatus.DEAD.toString());
        Assert.assertEquals("ALIVE",GamePlayerStatus.ALIVE.toString());
        Assert.assertEquals("ACCEPTED",GamePlayerStatus.ACCEPTED.toString());
    }

    @Test
    public void testGetStringForGameStatus() {
        Assert.assertEquals("CREATED",GameStatus.CREATED.toString());
        Assert.assertEquals("ONGOING",GameStatus.ONGOING.toString());
        Assert.assertEquals("FINISHED",GameStatus.FINISHED.toString());
    }

    @Test
    public void testGetStringForFriendStatus() {
        Assert.assertEquals("ACCEPTED",FriendStatus.ACCEPTED.toString());
        Assert.assertEquals("INVITED",FriendStatus.INVITED.toString());
        Assert.assertEquals("DECLINED",FriendStatus.DECLINED.toString());
    }
}
