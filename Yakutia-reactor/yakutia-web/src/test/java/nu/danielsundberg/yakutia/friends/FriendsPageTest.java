package nu.danielsundberg.yakutia.friends;

import nu.danielsundberg.yakutia.WicketApplication;
import nu.danielsundberg.yakutia.application.service.exceptions.NoPlayerFoundException;
import nu.danielsundberg.yakutia.application.service.iface.FriendManagerInterface;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.auth.SignIn;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.games.GamesPage;
import nu.danielsundberg.yakutia.harness.Authorizer;
import nu.danielsundberg.yakutia.harness.OauthMockHarness;
import nu.danielsundberg.yakutia.harness.preGameBeanMock.MyMockApplication;
import nu.danielsundberg.yakutia.session.MySession;
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.Result;
import org.apache.wicket.util.tester.TagTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Fredde
 * Date: 5/21/13 11:02 PM
 */
public class FriendsPageTest extends OauthMockHarness {

    private FriendManagerInterface friendManagerMock;
    private PreGameInterface preGameInterfaceMock;
    private Player playerMock;
    private WicketTester tester;

    @Before
    public void setup() throws NoPlayerFoundException {
        friendManagerMock = mock(FriendManagerInterface.class);
        preGameInterfaceMock = mock(PreGameInterface.class);

        playerMock = mock(Player.class);
        when(playerMock.getName()).thenReturn("any");
        when(preGameInterfaceMock.getPlayerByName(any(String.class))).thenReturn(playerMock);

        tester = new WicketTester(
                new MyMockApplication(new Object[]{friendManagerMock, preGameInterfaceMock}));
    }

    @Test
    public void userNotAuthorizedFriendPage() {
        // Given: user not authorized
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("FOO")));

        // When: requesting friend page
        tester.startPage(FriendsPage.class);

        // Then: redirected to sign in page
        tester.assertRenderedPage(SignIn.class);
    }

    @Test
    public void userAuthorizedWhenRequestingFriendPage() throws NoPlayerFoundException {

        // Given: a signed in player
        authorize();
        Player p = new Player();
        p.setName("APAN");
        ((MySession)tester.getSession()).setPlayerId(1L);
        when(preGameInterfaceMock.getPlayerById(anyLong())).thenReturn(p);

        // When: requesting friend page
        tester.startPage(FriendsPage.class);

        // Then: no errors are thrown and no listviews are shown
        tester.assertContainsNot("invites-container:invites-listview:0:invited-playerName");
        tester.assertContainsNot("all-players:all-players-listview:0:playername");
        tester.assertContainsNot("friends-container:friends-listview:0:friend-playerName");
    }

    @Test
    public void clickShowAllNonFriendPlayers() throws NoPlayerFoundException {
        // Given: authorized player on friends page
        authorize();
        ((MySession)tester.getSession()).setPlayerId(1L);
        when(preGameInterfaceMock.getPlayerById(anyLong())).thenReturn(playerMock);
        when(playerMock.getPlayerId()).thenReturn(1L);
        Set<Player> players = new HashSet<Player>();
        Player p = new Player();
        p.setName("PLAYER");
        players.add(p);
        when(friendManagerMock.getAllNonFriendPlayers(any(Player.class))).thenReturn(players);
        when(friendManagerMock.getAllInvites(any(Player.class))).thenReturn(players);
        when(friendManagerMock.getFriends(any(Player.class))).thenReturn(players);
        tester.startPage(FriendsPage.class);

        // When: clicking all non friend player button
        FormTester formtester = tester.newFormTester("super-friend-form");
        formtester.submit("list-all-button");

        // Then: a list of players are shown (non friends)
        tester.assertVisible("container-form:all-players:all-players-listview:0:playername");
        tester.assertContainsNot("container-form:invites-container:invites-listview:0:invited-playerName");
        tester.assertContainsNot("container-form:friends-container:friends-listview:0:friend-playerName");
    }

    @Test
    public void clickShowAndHideNonFriendPlayers() throws NoPlayerFoundException {
        // Given: authorized player on friends page
        authorize();
        ((MySession)tester.getSession()).setPlayerId(1L);
        when(preGameInterfaceMock.getPlayerById(anyLong())).thenReturn(playerMock);
        when(playerMock.getPlayerId()).thenReturn(1L);
        Set<Player> players = new HashSet<Player>();
        Player p = new Player();
        p.setName("PLAYER");
        players.add(p);
        when(friendManagerMock.getAllNonFriendPlayers(any(Player.class))).thenReturn(players);
        when(friendManagerMock.getAllInvites(any(Player.class))).thenReturn(players);
        when(friendManagerMock.getFriends(any(Player.class))).thenReturn(players);
        tester.startPage(FriendsPage.class);

        // When: clicking all non friend player button
        FormTester formtester = tester.newFormTester("super-friend-form");
        formtester.submit("list-all-button");

        // Then: a list of players are shown (non friends) + (not the other lists)
        tester.assertVisible("container-form:all-players:all-players-listview:0:playername");
        tester.assertContainsNot("container-form:invites-container:invites-listview:0:invited-playerName");
        tester.assertContainsNot("container-form:friends-container:friends-listview:0:friend-playerName");

        // When: clicking all non friend player button again
        formtester = tester.newFormTester("super-friend-form");
        formtester.submit("list-all-button");

        // Then: the list of players are not shown (not the other lists)
        tester.assertContainsNot("container-form:all-players:all-players-listview:0:playername");
        tester.assertContainsNot("container-form:invites-container:invites-listview:0:invited-playerName");
        tester.assertContainsNot("container-form:friends-container:friends-listview:0:friend-playerName");
    }

    @Test
    public void clickInviteNonFriendPlayer() throws NoPlayerFoundException {
        // Given: authorized player on friends page
        authorize();
        ((MySession)tester.getSession()).setPlayerId(1L);
        when(preGameInterfaceMock.getPlayerById(anyLong())).thenReturn(playerMock);
        when(playerMock.getPlayerId()).thenReturn(1L);
        Set<Player> players = new HashSet<Player>();
        Player p = new Player();
        p.setName("PLAYER");
        players.add(p);
        when(friendManagerMock.getAllNonFriendPlayers(any(Player.class))).thenReturn(players);
        when(friendManagerMock.getAllInvites(any(Player.class))).thenReturn(players);
        when(friendManagerMock.getFriends(any(Player.class))).thenReturn(players);
        tester.startPage(FriendsPage.class);

        // When: clicking all non friend player button
        FormTester formtester = tester.newFormTester("super-friend-form");
        formtester.submit("list-all-button");

        // Then: a list of players are shown (non friends) + (not the other lists)
        tester.assertVisible("container-form:all-players:all-players-listview:0:playername");
        List<Player> listOfAllNonfriendsBefore = (List<Player>)tester.getComponentFromLastRenderedPage("container-form:all-players:all-players-listview").getDefaultModelObject();
        Assert.assertEquals(1, listOfAllNonfriendsBefore.size());

        // When: clicking all non friend player button again
        formtester = tester.newFormTester("container-form");
        formtester.submit("all-players:all-players-listview:0:invite-button");

        // Then: the list of players are not shown (not the other lists)
        List<Player> listOfAllNonfriendsAfter = (List<Player>)tester.getComponentFromLastRenderedPage("container-form:all-players:all-players-listview").getDefaultModelObject();
        Assert.assertEquals(0, listOfAllNonfriendsAfter.size());
    }

    @Test
    public void clickShowNewInvites() throws NoPlayerFoundException {
        // Given: authorized player on friends page
        authorize();
        ((MySession)tester.getSession()).setPlayerId(1L);
        when(preGameInterfaceMock.getPlayerById(anyLong())).thenReturn(playerMock);
        when(playerMock.getPlayerId()).thenReturn(1L);
        Set<Player> players = new HashSet<Player>();
        Player p = new Player();
        p.setName("PLAYER_THAT_INVITED_YOU");
        players.add(p);
        when(friendManagerMock.getAllNonFriendPlayers(any(Player.class))).thenReturn(players);
        when(friendManagerMock.getAllInvites(any(Player.class))).thenReturn(players);
        when(friendManagerMock.getFriends(any(Player.class))).thenReturn(players);
        tester.startPage(FriendsPage.class);

        // When: clicking show invites
        FormTester formtester = tester.newFormTester("super-friend-form");
        formtester.submit("list-all-invites-button");

        // Then: a list of players that has invited + (not the other lists)
        tester.assertVisible("container-form:invites-container:invites-listview:0:invited-playerName");
        tester.assertContainsNot("container-form:all-players:all-players-listview:0:playername");
        tester.assertContainsNot("container-form:friends-container:friends-listview:0:friend-playerName");
    }

    @Test
    public void clickShowAndHideNewInvites() throws NoPlayerFoundException {
        // Given: authorized player on friends page
        authorize();
        ((MySession)tester.getSession()).setPlayerId(1L);
        when(preGameInterfaceMock.getPlayerById(anyLong())).thenReturn(playerMock);
        when(playerMock.getPlayerId()).thenReturn(1L);
        Set<Player> players = new HashSet<Player>();
        Player p = new Player();
        p.setName("PLAYER_THAT_INVITED_YOU");
        players.add(p);
        when(friendManagerMock.getAllNonFriendPlayers(any(Player.class))).thenReturn(players);
        when(friendManagerMock.getAllInvites(any(Player.class))).thenReturn(players);
        when(friendManagerMock.getFriends(any(Player.class))).thenReturn(players);
        tester.startPage(FriendsPage.class);

        // When: clicking show invites
        FormTester formtester = tester.newFormTester("super-friend-form");
        formtester.submit("list-all-invites-button");

        // Then: a list of players that has invited + (not the other lists)
        tester.assertVisible("container-form:invites-container:invites-listview:0:invited-playerName");
        tester.assertContainsNot("container-form:all-players:all-players-listview:0:playername");
        tester.assertContainsNot("container-form:friends-container:friends-listview:0:friend-playerName");

        // When: clicking show invites to close view
        formtester = tester.newFormTester("super-friend-form");
        formtester.submit("list-all-invites-button");

        // Then: a list of players that has invited + (not the other lists)
        tester.assertContainsNot("container-form:invites-container:invites-listview:0:invited-playerName");
        tester.assertContainsNot("container-form:all-players:all-players-listview:0:playername");
        tester.assertContainsNot("container-form:friends-container:friends-listview:0:friend-playerName");
    }

    @Test
    public void clickShowInvitesAndAcceptInvite() throws NoPlayerFoundException {
        // Given: authorized player on friends page
        authorize();
        ((MySession)tester.getSession()).setPlayerId(1L);
        when(preGameInterfaceMock.getPlayerById(anyLong())).thenReturn(playerMock);
        when(playerMock.getPlayerId()).thenReturn(1L);
        Set<Player> players = new HashSet<Player>();
        Player p = new Player();
        p.setName("PLAYER_THAT_INVITED_YOU");
        players.add(p);
        when(friendManagerMock.getAllNonFriendPlayers(any(Player.class))).thenReturn(players);
        when(friendManagerMock.getAllInvites(any(Player.class))).thenReturn(players);
        when(friendManagerMock.getFriends(any(Player.class))).thenReturn(players);
        tester.startPage(FriendsPage.class);

        // When: clicking show invites
        FormTester formtester = tester.newFormTester("super-friend-form");
        formtester.submit("list-all-invites-button");

        // Then: a player is shown and a accept button exists
        Result acceptInviteButtonVisible = tester.isVisible("container-form:invites-container:invites-listview:0:accept-invite");
        Assert.assertFalse(acceptInviteButtonVisible.wasFailed());

        List<Player> listOfAllInvitesBefore = (List<Player>)tester.getComponentFromLastRenderedPage("container-form:invites-container:invites-listview").getDefaultModelObject();
        Assert.assertEquals(1, listOfAllInvitesBefore.size());

        // When: clicking accept button
        formtester = tester.newFormTester("container-form");
        formtester.submit("invites-container:invites-listview:0:accept-invite");

        // Then: no player is shown and the accept button is gone
        List<Player> listOfAllInvitesAfter = (List<Player>)tester.getComponentFromLastRenderedPage("container-form:invites-container:invites-listview").getDefaultModelObject();
        Assert.assertEquals(0, listOfAllInvitesAfter.size());
    }

    @Test
    public void clickShowInvitesAndDeclineInvite() throws NoPlayerFoundException {
        // Given: authorized player on friends page
        authorize();
        ((MySession)tester.getSession()).setPlayerId(1L);
        when(preGameInterfaceMock.getPlayerById(anyLong())).thenReturn(playerMock);
        when(playerMock.getPlayerId()).thenReturn(1L);
        Set<Player> players = new HashSet<Player>();
        Player p = new Player();
        p.setName("PLAYER_THAT_INVITED_YOU");
        players.add(p);
        when(friendManagerMock.getAllNonFriendPlayers(any(Player.class))).thenReturn(players);
        when(friendManagerMock.getAllInvites(any(Player.class))).thenReturn(players);
        when(friendManagerMock.getFriends(any(Player.class))).thenReturn(players);
        tester.startPage(FriendsPage.class);

        // When: clicking show invites
        FormTester formtester = tester.newFormTester("super-friend-form");
        formtester.submit("list-all-invites-button");

        // Then: a player is shown and a decline button exists
        List<Player> listOfAllInvitesBefore = (List<Player>)tester.getComponentFromLastRenderedPage("container-form:invites-container:invites-listview").getDefaultModelObject();
        Assert.assertEquals(1, listOfAllInvitesBefore.size());
        Result acceptInviteButtonVisible = tester.isVisible("container-form:invites-container:invites-listview:0:decline-invite");
        Assert.assertFalse(acceptInviteButtonVisible.wasFailed());

        // When: clicking decline button
        formtester = tester.newFormTester("container-form");
        formtester.submit("invites-container:invites-listview:0:decline-invite");

        // Then: no player is shown and the accept button is gone
        List<Player> listOfAllInvitesAfter = (List<Player>)tester.getComponentFromLastRenderedPage("container-form:invites-container:invites-listview").getDefaultModelObject();
        Assert.assertEquals(0, listOfAllInvitesAfter.size());
    }

    @Test
    public void clickShowFriends() throws NoPlayerFoundException {
        // Given: authorized player on friends page
        authorize();
        ((MySession)tester.getSession()).setPlayerId(1L);
        when(preGameInterfaceMock.getPlayerById(anyLong())).thenReturn(playerMock);
        when(playerMock.getPlayerId()).thenReturn(1L);
        Set<Player> players = new HashSet<Player>();
        Player p = new Player();
        p.setName("PLAYER_THAT_IS_A_FRIEND");
        players.add(p);
        when(friendManagerMock.getAllNonFriendPlayers(any(Player.class))).thenReturn(players);
        when(friendManagerMock.getAllInvites(any(Player.class))).thenReturn(players);
        when(friendManagerMock.getFriends(any(Player.class))).thenReturn(players);
        tester.startPage(FriendsPage.class);

        // When: clicking show invites
        FormTester formtester = tester.newFormTester("super-friend-form");
        formtester.submit("list-all-friends-button");

        // Then: a list of players that has invited + (not the other lists)
        tester.assertVisible("container-form:friends-container:friends-listview:0:friend-playerName");
        tester.assertContainsNot("container-form:invites-container:invites-listview:0:invited-playerName");
        tester.assertContainsNot("container-form:all-players:all-players-listview:0:playername");
    }

    @Test
    public void clickShowAndThenHideFriends() throws NoPlayerFoundException {

        // Given: authorized player on friends page
        authorize();
        ((MySession)tester.getSession()).setPlayerId(1L);
        when(preGameInterfaceMock.getPlayerById(anyLong())).thenReturn(playerMock);
        when(playerMock.getPlayerId()).thenReturn(1L);
        Set<Player> players = new HashSet<Player>();
        Player p = new Player();
        p.setName("PLAYER_THAT_IS_A_FRIEND");
        players.add(p);
        when(friendManagerMock.getAllNonFriendPlayers(any(Player.class))).thenReturn(players);
        when(friendManagerMock.getAllInvites(any(Player.class))).thenReturn(players);
        when(friendManagerMock.getFriends(any(Player.class))).thenReturn(players);
        tester.startPage(FriendsPage.class);

        // When: clicking show invites
        FormTester formtester = tester.newFormTester("super-friend-form");
        formtester.submit("list-all-friends-button");

        // Then: a list of players that has invited + (not the other lists)
        tester.assertVisible("container-form:friends-container:friends-listview:0:friend-playerName");
        tester.assertContainsNot("container-form:invites-container:invites-listview:0:invited-playerName");
        tester.assertContainsNot("container-form:all-players:all-players-listview:0:playername");

        // When: clicking show invites to close view
        formtester = tester.newFormTester("super-friend-form");
        formtester.submit("list-all-friends-button");

        // Then: a list of players that has invited + (not the other lists)
        tester.assertContainsNot("container-form:friends-container:friends-listview:0:friend-playerName");
        tester.assertContainsNot("container-form:invites-container:invites-listview:0:invited-playerName");
        tester.assertContainsNot("container-form:all-players:all-players-listview:0:playername");
    }

    private void authorize() {
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));
    }
}
