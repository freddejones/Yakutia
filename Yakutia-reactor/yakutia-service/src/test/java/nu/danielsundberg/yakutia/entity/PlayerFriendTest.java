package nu.danielsundberg.yakutia.entity;

import junit.framework.Assert;
import nu.danielsundberg.yakutia.entity.statuses.FriendStatus;
import nu.danielsundberg.yakutia.testcore.JpaTestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

/**
 * User: Fredde
 * Date: 7/6/13 12:50 AM
 */
public class PlayerFriendTest extends JpaTestCase {

    Player playerWithFriends;
    Player friend_1;

    @Before
    public void setup() {
        playerWithFriends = new Player();
        playerWithFriends.setName("PLAYER_WITH_FRIENDS");
        friend_1 = new Player();
        friend_1.setName("FRIEND_1");
        entityManager.persist(playerWithFriends);
        entityManager.persist(friend_1);
    }

    @Test
    public void persistPlayerFriendEntity_1() {
        PlayerFriend pf = new PlayerFriend();
        pf.setFriendStatus(FriendStatus.DECLINED);
        entityManager.persist(pf);
        Assert.assertNotNull(pf.getPlayerFriendId());
    }

    @Test
    public void persistPlayerFriendEntity() {

        // Given: two players (one relationship direction)
        entityManager.refresh(playerWithFriends);
        entityManager.refresh(friend_1);
        PlayerFriend pf = new PlayerFriend();
        pf.setPlayer(playerWithFriends);
        pf.setFriend(friend_1);
        pf.setFriendStatus(FriendStatus.INVITED);

        // When: persisting relationship
        entityManager.persist(pf);

        // Then: a relationship is created, and can be retrieved from Player object
        Assert.assertNotNull(pf.getPlayerFriendId());
        PlayerFriend playerFriendVerify = entityManager.find(PlayerFriend.class, pf.getPlayerFriendId());
        entityManager.refresh(playerWithFriends);
        Player playerVerify = entityManager.find(Player.class, playerWithFriends.getPlayerId());
        Assert.assertNotNull(playerFriendVerify.getPlayer());
        Assert.assertEquals(1, playerVerify.getFriends().size());
    }
}
