package nu.danielsundberg.yakutia.application.service.impl;

import junit.framework.Assert;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.entity.PlayerFriend;
import nu.danielsundberg.yakutia.entity.statuses.FriendStatus;
import nu.danielsundberg.yakutia.testcore.JpaTestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * User: Fredde
 * Date: 7/7/13 8:59 PM
 */
public class FriendManagerBeanTest extends JpaTestCase {

    private FriendManagerBean friendManager;
    private Player player;

    @Before
    public void setup() {
        friendManager = new FriendManagerBean();
        friendManager.em = entityManager;
    }

    @Test
    public void getFriendsButNotExists() {

        // Given: a player with no friends
        player = setupBasicPlayer("PLAYER");
        entityManager.persist(player);

        // When: getting friends for the player
        Set<Player> friends = friendManager.getFriends(player);

        // Then: no friends (e.g. players) should be received
        Assert.assertEquals(0, friends.size());
    }

    @Test
    public void getFriendsWhenTwoExists() {

        // Given: a players with two friends (one direction relationship)
        player = setupBasicPlayer("PLAYER_WITH_FRIENDS");
        Player friendOne, friendTwo;
        friendOne = setupBasicPlayer("FRIEND_ONE");
        friendTwo = setupBasicPlayer("FRIEND_TWO");
        entityManager.persist(player);
        entityManager.persist(friendOne);
        entityManager.persist(friendTwo);

        entityManager.refresh(player);
        entityManager.refresh(friendOne);
        entityManager.refresh(friendTwo);
        PlayerFriend pf1 = new PlayerFriend();
        pf1.setPlayer(player);
        pf1.setFriend(friendOne);
        pf1.setFriendStatus(FriendStatus.ACCEPTED);
        entityManager.persist(pf1);

        PlayerFriend pf2 = new PlayerFriend();
        pf2.setPlayer(player);
        pf2.setFriend(friendTwo);
        pf2.setFriendStatus(FriendStatus.ACCEPTED);
        entityManager.persist(pf2);

        // When: getting friends for player
        Set<Player> friendsReceived = friendManager.getFriends(player);

        // Then: two friens should be received
        Assert.assertEquals(2, friendsReceived.size());
    }

    @Test
    public void getAllNonFriendPlayersTest() {

        // Given: three players in database with no relations
        Player p = setupBasicPlayer("PLAYER_ONE");
        entityManager.persist(p);
        entityManager.persist(setupBasicPlayer("PLAYER_TWO"));
        entityManager.persist(setupBasicPlayer("PLAYER_THREE"));

        // When: getting all players from one players pov
        Set<Player> players = friendManager.getAllNonFriendPlayers(p);

        // Then: 2 players should be received
        Assert.assertEquals(2, players.size());
    }

    @Test
    public void getAllNonFriendPlayersWhenPlayerAreInvited() {

        // Given: one player with one invite sent
        Player player = setupBasicPlayer("PLAYER_ONE");
        Player invitedPlayer = setupBasicPlayer("PLAYER_TWO");
        entityManager.persist(player);
        entityManager.persist(invitedPlayer);
        entityManager.persist(setupBasicPlayer("PLAYER_THREE"));

        entityManager.refresh(player);
        entityManager.refresh(invitedPlayer);
        PlayerFriend pf = new PlayerFriend();
        pf.setPlayer(player);
        pf.setFriendStatus(FriendStatus.INVITED);
        pf.setFriend(invitedPlayer);
        entityManager.persist(pf);

        // When: getting all players no friend
        Set<Player> playerNo = friendManager.getAllNonFriendPlayers(player);

        // Then: one player is received
        Assert.assertEquals(1, playerNo.size());
    }

    @Test
    public void getAllNonFriendPlayersWithOneRelationTest() {

        // Given: one player with friend relation, two others
        Player player = setupBasicPlayer("PLAYER_ONE");
        Player friend = setupBasicPlayer("PLAYER_TWO");
        entityManager.persist(player);
        entityManager.persist(friend);
        entityManager.persist(setupBasicPlayer("PLAYER_THREE"));

        entityManager.refresh(player);
        entityManager.refresh(friend);
        PlayerFriend pf = new PlayerFriend();
        pf.setPlayer(player);
        pf.setFriendStatus(FriendStatus.ACCEPTED);
        pf.setFriend(friend);
        entityManager.persist(pf);

        // When: getting all players no friend
        Set<Player> playerNo = friendManager.getAllNonFriendPlayers(player);

        // Then: one player is received
        Assert.assertEquals(1, playerNo.size());
    }

    @Test
    public void getAllInvitesForAPlayerTest() {

        // Given: one player has invited another player to be friends
        Player player = setupBasicPlayer("PLAYER_ONE");
        Player playerThatsInvited = setupBasicPlayer("PLAYER_INVITED");
        entityManager.persist(player);
        entityManager.persist(playerThatsInvited);
        entityManager.persist(setupBasicPlayer("PLAYER_THREE"));

        entityManager.refresh(player);
        entityManager.refresh(playerThatsInvited);
        PlayerFriend pf = new PlayerFriend();
        pf.setPlayer(player);
        pf.setFriendStatus(FriendStatus.INVITED);
        pf.setFriend(playerThatsInvited);
        entityManager.persist(pf);

        // When: requesting to get all current invites for the invited player
        Set<Player> playersWithInvitesToYou = friendManager.getAllInvites(playerThatsInvited);

        // Then: one player is in the result
        Assert.assertEquals(1, playersWithInvitesToYou.size());
    }

    @Test
    public void sendInviteTest() {

        // Given: two non friendship players
        Player p = setupBasicPlayer("PLAYER_ONE");
        Player friend = setupBasicPlayer("PLAYER_TO_INVITE");
        entityManager.persist(p);
        entityManager.persist(friend);

        // When: player invites a player to be friends
        friendManager.sendInvite(p,friend);

        // Then: the friend player has an invite
        entityManager.refresh(friend);
        List<PlayerFriend> relationships = new ArrayList<PlayerFriend>(friend.getFriendsReqested());
        Assert.assertEquals(1, relationships.size());
        Assert.assertEquals(FriendStatus.INVITED, relationships.get(0).getFriendStatus());
    }

    @Test
    public void acceptInviteAndCheckFriendshipDirectionsTest() {

        // Given: a player p1 has invited another player p2 to be a friend
        Player p1 = setupBasicPlayer("PLAYER_ONE");
        Player p2 = setupBasicPlayer("PLAYER_THAT_ACCEPTS_INVITE");
        entityManager.persist(p1);
        entityManager.persist(p2);
        PlayerFriend pf = new PlayerFriend();
        pf.setFriend(p2);
        pf.setPlayer(p1);
        pf.setFriendStatus(FriendStatus.INVITED);
        entityManager.persist(pf);

        // When: player p2 accepts the invite
        friendManager.acceptInvite(p2, p1);

        // Then: both players has each other as friends (and ACCEPTED)
        entityManager.refresh(p1);
        entityManager.refresh(p2);

        List<PlayerFriend> toVerifyP1DirectionOne = new ArrayList<PlayerFriend>(p1.getFriends());
        Assert.assertEquals(1, toVerifyP1DirectionOne.size());
        Assert.assertEquals(FriendStatus.ACCEPTED, toVerifyP1DirectionOne.get(0).getFriendStatus());
        List<PlayerFriend> toVerifyP1DirectionTwo = new ArrayList<PlayerFriend>(p1.getFriendsReqested());
        Assert.assertEquals(1, toVerifyP1DirectionTwo.size());
        Assert.assertEquals(FriendStatus.ACCEPTED, toVerifyP1DirectionTwo.get(0).getFriendStatus());
        List<PlayerFriend> toVerifyP2DirectionOne = new ArrayList<PlayerFriend>(p2.getFriends());
        Assert.assertEquals(1, toVerifyP2DirectionOne.size());
        Assert.assertEquals(FriendStatus.ACCEPTED, toVerifyP2DirectionOne.get(0).getFriendStatus());
        List<PlayerFriend> toVerifyP2DirectionTwo = new ArrayList<PlayerFriend>(p2.getFriendsReqested());
        Assert.assertEquals(1, toVerifyP2DirectionTwo.size());
        Assert.assertEquals(FriendStatus.ACCEPTED, toVerifyP2DirectionTwo.get(0).getFriendStatus());
    }

    @Test
    public void friendManagerInviteScenarioTest() {

        // Given: two players in database, no relation
        Player p1 = setupBasicPlayer("PLAYER_ONE");
        Player p2 = setupBasicPlayer("PLAYER_TWO");
        entityManager.persist(p1);
        entityManager.persist(p2);

        Assert.assertEquals(0, friendManager.getFriends(p1).size());
        Assert.assertEquals(0, friendManager.getFriends(p1).size());

        // When: p1 send invite to p2
        friendManager.sendInvite(p1, p2);

        // Then: p2 gets an invite
        Assert.assertEquals(1, friendManager.getAllInvites(p2).size());

        // When: p2 accepts invite from p1
        friendManager.acceptInvite(p2, p1);

        // Then: no players are left in db for p1 to friend or for p2
        Assert.assertEquals(0, friendManager.getAllNonFriendPlayers(p1).size());
        Assert.assertEquals(0, friendManager.getAllNonFriendPlayers(p2).size());

        // Then: p1 and p2 got friended
        Assert.assertEquals(1, friendManager.getFriends(p1).size());
        Assert.assertEquals(1, friendManager.getFriends(p2).size());

    }

    private Player setupBasicPlayer(String name) {
        Player p = new Player();
        p.setName(name);
        p.setEmail("some@email.com");
        return p;
    }
}
