package se.freddejones.yakutia.games;

import se.freddejones.yakutia.application.service.exceptions.NoPlayerFoundException;
import se.freddejones.yakutia.application.service.iface.PreGameInterface;
import se.freddejones.yakutia.entity.GamePlayer;
import se.freddejones.yakutia.entity.Player;
import se.freddejones.yakutia.entity.PlayerFriend;
import se.freddejones.yakutia.harness.Authorizer;
import se.freddejones.yakutia.harness.beanMocker.MyMockApplication;
import se.freddejones.yakutia.session.MySession;
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Fredde
 * Date: 5/23/13 12:13 AM
 */
public class CreateGamePageTest {

    private WicketTester tester;
    private PreGameInterface preGameBeanMock;
    private Player playerMock;

    @Before
    public void setUp() throws NoPlayerFoundException {
        preGameBeanMock = mock(PreGameInterface.class);
        playerMock = mock(Player.class);
        tester = new WicketTester(new MyMockApplication(new Object[]{preGameBeanMock}));
    }

    @Test
    public void checkCorrectRole() throws NoPlayerFoundException {
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));
        setupNoFriendsMock();
        tester.startPage(CreateGamePage.class);
        tester.assertRenderedPage(CreateGamePage.class);
    }

    @Test
    public void createAGameAndCheckCorrectCallbackPage() throws NoPlayerFoundException {
        // Given: authorized user, going to create game page
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));

        setupNoFriendsMock();
        ((MySession)tester.getSession()).setPlayerId(1L);
        ((MySession)tester.getSession()).setPlayerName("PLAYER");
        List<Player> players = getPlayerList();
        Player p = new Player();
        p.setName("PLAYER");
        p.setPlayerId(1L);
        players.add(p);
        when(preGameBeanMock.getPlayers()).thenReturn(players);
        when(preGameBeanMock.createNewGame(anyLong(),anyString())).thenReturn(666L);

        tester.startPage(CreateGamePage.class);
        FormTester formTester = tester.newFormTester("form1");
        formTester.setValue("gamename","GAMENAME!!");
        formTester.submit();

        // Mock the gamesPage page:
        when(preGameBeanMock.getPlayerByName(anyString())).thenReturn(playerMock);
        when(playerMock.getGames()).thenReturn(new HashSet<GamePlayer>());

        FormTester formtester = tester.newFormTester("form1");
        formtester.submit("webmarkupcontainer:players:0:addPlayer");

        // When: clicking the button
        formtester = tester.newFormTester("form1");
        formtester.submit("submit1");

        // Then: user get redirected back to Games page
        tester.assertRenderedPage(GamesPage.class);
    }

    @Test
    public void checkFriendsOnTopInList() throws NoPlayerFoundException {
        // Given: authorized user, with friends
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));

        ((MySession)tester.getSession()).setPlayerName("PLAYER");

        when(preGameBeanMock.getPlayerById(anyLong())).thenReturn(playerMock);

        PlayerFriend playerFriendMock = mock(PlayerFriend.class);
        Set<PlayerFriend> friendsSetMock = new HashSet<PlayerFriend>();
        friendsSetMock.add(playerFriendMock);
        when(playerMock.getFriends()).thenReturn(friendsSetMock);
        Player p = new Player();
        p.setPlayerId(3L);
        p.setName("PLAYER_FRIEND");
        when(playerFriendMock.getFriend()).thenReturn(p);

        List<Player> allPlayers = getPlayerList();
        allPlayers.add(p);
        when(preGameBeanMock.getPlayers()).thenReturn(allPlayers);

        // When: hitting create game page
        tester.startPage(CreateGamePage.class);

        // Then: friends are listed in friend table
        tester.assertModelValue("form1:wmc-friends:friends:0:friendname","PLAYER_FRIEND");
    }

    @Test
    public void addFriendToGameList() throws NoPlayerFoundException {

        // Given: a player with one friend connection
        checkFriendsOnTopInList();

        List<Player> actualAddedPlayersBefore = (List<Player>)tester.getComponentFromLastRenderedPage("form1:wmc:playersAdded").getDefaultModelObject();
        Assert.assertEquals(0, actualAddedPlayersBefore.size());

        // When: clicking the add friend button
        FormTester formtester = tester.newFormTester("form1");
        formtester.submit("wmc-friends:friends:0:addFriendPlayer");

        // Then: a friend is added to added players list
        List<Player> actualAddedPlayersAfter = (List<Player>)tester.getComponentFromLastRenderedPage("form1:wmc:playersAdded").getDefaultModelObject();
        Assert.assertEquals(1, actualAddedPlayersAfter.size());
    }

    @Test
    public void removeAddedFriendFromGameList() throws NoPlayerFoundException {

        // Given: a friend is added to list with added players
        addFriendToGameList();

        List<Player> actualFriendsBefore =
                (List<Player>)tester.getComponentFromLastRenderedPage("form1:wmc-friends:friends").getDefaultModelObject();
        Assert.assertEquals(0, actualFriendsBefore.size());

        // When: deleting a friend from added players table
        FormTester formtester = tester.newFormTester("form1");
        formtester.submit("wmc:playersAdded:0:delPlayer");

        // Then: friend is back to friends list
        List<Player> actualFriendsAfter =
                (List<Player>)tester.getComponentFromLastRenderedPage("form1:wmc-friends:friends").getDefaultModelObject();
        Assert.assertEquals(1, actualFriendsAfter.size());
    }

    @Test
    public void cannotCreateGameWithOnlyOnePlayer() throws NoPlayerFoundException {
        // Given: going to create game page, only one player added to game
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));

        setupNoFriendsMock();
        ((MySession)tester.getSession()).setPlayerName("PLAYER");
        Player p = new Player();
        p.setName("PLAYER");
        p.setPlayerId(1L);
        List<Player> players = getPlayerList();
        players.add(p);
        when(preGameBeanMock.getPlayers()).thenReturn(players);
        tester.startPage(CreateGamePage.class);

        // When: creating game
        FormTester formTester = tester.newFormTester("form1");
        formTester.setValue("gamename","a game name");
        formTester.submit("submit1");

        // Then: a help message is shown
        MultiLineLabel label = (MultiLineLabel)tester.getComponentFromLastRenderedPage("wmc-message:message");
        Assert.assertEquals("you need to be at least 2 players to create a game\n",label.getDefaultModelObjectAsString());
        tester.assertVisible("wmc-message:message");
    }

    @Test
    public void notPossibleToRemoveYourselfFromActivePlayerList() throws NoPlayerFoundException {
        // Given: going to create game page, only the user is added as player
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));

        setupNoFriendsMock();
        ((MySession)tester.getSession()).setPlayerName("PLAYER");
        ((MySession)tester.getSession()).setPlayerId(1L);
        Player p = new Player();
        p.setPlayerId(1L);
        p.setName("PLAYER");
        List<Player> players = new ArrayList<Player>();
        players.add(p);
        when(preGameBeanMock.getPlayers()).thenReturn(players);
        tester.startPage(CreateGamePage.class);

        List<Player> actualAddedPlayersBefore = (List<Player>)tester.getComponentFromLastRenderedPage("form1:wmc:playersAdded").getDefaultModelObject();
        Assert.assertEquals(1, actualAddedPlayersBefore.size());

        // When: removing user player from active player list
        FormTester formtester = tester.newFormTester("form1");
        formtester.submit("wmc:playersAdded:0:delPlayer");

        // Then: player is still in the list
        List<Player> actualAddedPlayersAfter = (List<Player>)tester.getComponentFromLastRenderedPage("form1:wmc:playersAdded").getDefaultModelObject();
        Assert.assertEquals(1, actualAddedPlayersAfter.size());

    }

    @Test
    public void checkMandatoryGameNameOnCreateGameSubmit() throws NoPlayerFoundException {
        // Given: authorized user
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));

        setupNoFriendsMock();
        ((MySession)tester.getSession()).setPlayerId(1L);
        ((MySession)tester.getSession()).setPlayerName("PLAYER");
        List<Player> players = getPlayerList();
        Player p = new Player();
        p.setName("PLAYER");
        p.setPlayerId(1L);
        players.add(p);
        when(preGameBeanMock.getPlayers()).thenReturn(players);
        when(preGameBeanMock.createNewGame(anyLong(),anyString())).thenReturn(666L);

        tester.startPage(CreateGamePage.class);

        // Mock the gamesPage page:
        ((MySession)tester.getSession()).setPlayerName("PLAYER");
        when(preGameBeanMock.getPlayerByName(anyString())).thenReturn(playerMock);
        when(playerMock.getGames()).thenReturn(new HashSet<GamePlayer>());

        FormTester formtester = tester.newFormTester("form1");
        formtester.submit("webmarkupcontainer:players:0:addPlayer");
        tester.assertInvisible("wmc-message:message");

        // When: creating a game (submit) without specifying any game name
        formtester = tester.newFormTester("form1");
        formtester.submit("submit1");

        // Then: a information message is shown
        MultiLineLabel label = (MultiLineLabel)tester.getComponentFromLastRenderedPage("wmc-message:message");
        Assert.assertEquals("you need to specify a game name\n",label.getDefaultModelObjectAsString());
        tester.assertVisible("wmc-message:message");
    }

    @Test
    public void addAPlayerFromNonFriendList() throws NoPlayerFoundException {
        // Given: authorized user, created a game
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));

        setupNoFriendsMock();

        // Mock the gamesPage page:
        ((MySession)tester.getSession()).setPlayerName("PLAYER");
        when(preGameBeanMock.getPlayerByName(anyString())).thenReturn(playerMock);
        when(playerMock.getGames()).thenReturn(new HashSet<GamePlayer>());

        ((MySession)tester.getSession()).setPlayerId(1L);
        when(playerMock.getName()).thenReturn("PLAYER");
        List<Player> players = getPlayerList();
        players.add(playerMock);
        when(preGameBeanMock.getPlayers()).thenReturn(players);
        when(preGameBeanMock.createNewGame(anyLong(),anyString())).thenReturn(666L);

        tester.startPage(CreateGamePage.class);
        FormTester formTester = tester.newFormTester("form1");
        formTester.setValue("gamename","GAMENAME!!");

        List<Player> actualAddedPlayersBefore = (List<Player>)tester.getComponentFromLastRenderedPage("form1:wmc:playersAdded").getDefaultModelObject();
        Assert.assertEquals(1, actualAddedPlayersBefore.size());

        // When: clicking the add player button
        FormTester formtester = tester.newFormTester("form1");
        formtester.submit("webmarkupcontainer:players:0:addPlayer");

        // Then: player is added to active player list
        List<Player> actualAddedPlayersAfter = (List<Player>)tester.getComponentFromLastRenderedPage("form1:wmc:playersAdded").getDefaultModelObject();
        Assert.assertEquals(2, actualAddedPlayersAfter.size());
    }

    @Test
    public void checkThatMessageIsRemovedWhenAddingAPlayer() throws NoPlayerFoundException {
        // Given: a error message has been triggered
        cannotCreateGameWithOnlyOnePlayer();

        // When: correcting the error
        FormTester formtester = tester.newFormTester("form1");
        formtester.submit("webmarkupcontainer:players:0:addPlayer");

        // Then: the message label should not be visible
        tester.assertInvisible("wmc-message:message");
    }

    @Test
    public void checkThatMessageIsRemovedWhenAddingAFriend() throws NoPlayerFoundException {
        // Given: a error message has been triggered
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));

        ((MySession)tester.getSession()).setPlayerName("PLAYER");

        when(preGameBeanMock.getPlayerById(anyLong())).thenReturn(playerMock);

        PlayerFriend playerFriendMock = mock(PlayerFriend.class);
        Set<PlayerFriend> friendsSetMock = new HashSet<PlayerFriend>();
        friendsSetMock.add(playerFriendMock);
        when(playerMock.getFriends()).thenReturn(friendsSetMock);
        Player p = new Player();
        p.setPlayerId(3L);
        p.setName("PLAYER_FRIEND");
        when(playerFriendMock.getFriend()).thenReturn(p);

        List<Player> allPlayers = getPlayerList();
        allPlayers.add(p);
        when(preGameBeanMock.getPlayers()).thenReturn(allPlayers);
        tester.startPage(CreateGamePage.class);

        FormTester formTester = tester.newFormTester("form1");
        formTester.setValue("gamename","a game name");
        formTester.submit("submit1");

        // When: correcting the error
        FormTester formtester = tester.newFormTester("form1");
        formtester.submit("wmc-friends:friends:0:addFriendPlayer");

        // Then: the message label should not be visible
        tester.assertInvisible("wmc-message:message");
    }

    @Test
    public void checkThatTwoMessagesCanBeDisplayedAfterEachOtherBugFix() throws NoPlayerFoundException {
        // Given: a message has been triggered
        cannotCreateGameWithOnlyOnePlayer();

        // When: triggerering a new message
        FormTester formTester = tester.newFormTester("form1");
        formTester.setValue("gamename","");
        formTester.submit("submit1");

        // Then: message containing two error messages will be shown
        tester.assertVisible("wmc-message:message");
        MultiLineLabel label = (MultiLineLabel)tester.getComponentFromLastRenderedPage("wmc-message:message");
        Assert.assertEquals("you need to be at least 2 players to create a game\n" +
                "you need to specify a game name\n",label.getDefaultModelObjectAsString());
    }


    private void setupNoFriendsMock() throws NoPlayerFoundException {
        when(preGameBeanMock.getPlayerById(anyLong())).thenReturn(playerMock);
        Set<PlayerFriend> friendsSetMock = new HashSet<PlayerFriend>();
        when(playerMock.getFriends()).thenReturn(friendsSetMock);
    }

    private List<Player> getPlayerList() {
        Player p = new Player();
        p.setPlayerId(2L);
        p.setName("PLAYER_X");
        List<Player> players = new ArrayList<Player>();
        players.add(p);
        return players;
    }
}
