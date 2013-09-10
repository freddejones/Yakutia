package nu.danielsundberg.yakutia.games;

import junit.framework.Assert;
import nu.danielsundberg.yakutia.application.service.iface.PlayerActionsInterface;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.auth.SignIn;
import nu.danielsundberg.yakutia.application.service.exceptions.NoPlayerFoundException;
import nu.danielsundberg.yakutia.entity.Game;
import nu.danielsundberg.yakutia.entity.GamePlayer;
import nu.danielsundberg.yakutia.entity.PlayerFriend;
import nu.danielsundberg.yakutia.entity.statuses.GamePlayerStatus;
import nu.danielsundberg.yakutia.entity.statuses.GameStatus;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.harness.Authorizer;
import nu.danielsundberg.yakutia.harness.OauthMockHarness;
import nu.danielsundberg.yakutia.harness.preGameBeanMock.MyMockApplication;
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.naming.NamingException;
import java.util.*;

import static org.mockito.Mockito.*;

/**
 * User: Fredde
 * Date: 5/6/13 11:13 PM
 */
public class GamesPageTest extends OauthMockHarness {

    private WicketTester tester;
    private PreGameInterface preGameBeanMock;
    private PlayerActionsInterface playerActionsInterfaceMock;
    private Player playerMock;

    @Before
    public void setUp() throws NoPlayerFoundException {
        preGameBeanMock = mock(PreGameInterface.class);
        playerActionsInterfaceMock = mock(PlayerActionsInterface.class);
        playerMock = mock(Player.class);
        when(playerMock.getName()).thenReturn("any");
        when(playerMock.getPlayerId()).thenReturn(1L);
        when(preGameBeanMock.getPlayerByName(any(String.class))).thenReturn(playerMock);
        when(preGameBeanMock.getPlayerById(anyLong())).thenReturn(playerMock);

        tester = new WicketTester(new MyMockApplication(
                new Object[]{preGameBeanMock, playerActionsInterfaceMock}));
    }

    @Test
    public void checkCorrectRole() {
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));
        tester.startPage(GamesPage.class);

        tester.assertRenderedPage(GamesPage.class);
    }

    @Test
    public void checkInCorrectRole() {
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("FOO")));
        tester.startPage(GamesPage.class);

        tester.assertRenderedPage(SignIn.class);
    }

    @Test
    public void checkPageWhenNoGamesExists() throws NoPlayerFoundException {
        // Given: user is authorized with 0 games
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));
        Set<GamePlayer> gamePlayersSetMock = mock(Set.class);
        when(preGameBeanMock.getPlayerByName(any(String.class))).thenReturn(playerMock);
        when(playerMock.getGames()).thenReturn(gamePlayersSetMock);
        when(gamePlayersSetMock.isEmpty()).thenReturn(true);


        // When: user hits gamepage
        tester.startPage(GamesPage.class);

        // Then: no games are shown
        Assert.assertEquals("No games for you", tester.getTagById("msg").getValue());
        tester.assertVisible("CreateGame:msg");
    }

    @Test
    public void checkPageWhenGamesExist() throws NoPlayerFoundException {
        // Given: user is authorized with 1 games
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));
        when(preGameBeanMock.getPlayerByName(any(String.class))).thenReturn(playerMock);

        Iterator<GamePlayer> gamePlayersIteratorMock = mock(Iterator.class);
        Set<GamePlayer> gamePlayersSetMock = mock(Set.class);
        GamePlayer gamePlayerMock = mock(GamePlayer.class);
        Game gameMock = mock(Game.class);
        when(gamePlayerMock.getGamePlayerStatus()).thenReturn(GamePlayerStatus.ACCEPTED);
        when(gamePlayersSetMock.isEmpty()).thenReturn(false);
        when(gamePlayersSetMock.size()).thenReturn(1);
        when(gamePlayersSetMock.iterator()).thenReturn(gamePlayersIteratorMock);
        when(gamePlayersIteratorMock.hasNext()).thenReturn(true).thenReturn(false);
        when(gamePlayersIteratorMock.next()).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getGameId()).thenReturn(12L);
        when(gamePlayerMock.getGame()).thenReturn(gameMock);
        when(gameMock.getGameStatus()).thenReturn(GameStatus.ONGOING);
        when(playerMock.getGames()).thenReturn(gamePlayersSetMock);

        when(preGameBeanMock.getGameById(anyLong())).thenReturn(gameMock);

        // When: user hits gamepage
        tester.startPage(GamesPage.class);

        // Then: number of games are shown + correct gameId
        ListView<GamePlayer> list = (ListView<GamePlayer>)tester.getLastRenderedPage().get("CreateGame:wmc-current-games:rows");
        ListItem listItem = (ListItem) list.get(0);
        GamePlayer gp = (GamePlayer) listItem.getModelObject();
        Assert.assertEquals(1,list.size());
        Assert.assertEquals(12L,gp.getGameId());
        tester.assertInvisible("CreateGame:msg");
    }

    @Test
    public void clickButtonForCreatedGame() throws NoPlayerFoundException {
        // Given: user is authorized with 1 game
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));
        when(preGameBeanMock.getPlayerByName(any(String.class))).thenReturn(playerMock);

        Iterator<GamePlayer> gamePlayersIteratorMock = mock(Iterator.class);
        Set<GamePlayer> gamePlayersSetMock = mock(Set.class);
        GamePlayer gamePlayerMock = mock(GamePlayer.class);
        Game gameMock = mock(Game.class);
        when(gamePlayerMock.getGamePlayerStatus()).thenReturn(GamePlayerStatus.ACCEPTED);
        when(gamePlayersSetMock.isEmpty()).thenReturn(false);
        when(gamePlayersSetMock.size()).thenReturn(1);
        when(gamePlayersSetMock.iterator()).thenReturn(gamePlayersIteratorMock);
        when(gamePlayersIteratorMock.hasNext()).thenReturn(true).thenReturn(false);
        when(gamePlayersIteratorMock.next()).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getGameId()).thenReturn(12L);
        when(gamePlayerMock.getGame()).thenReturn(gameMock);
        when(gameMock.getGameStatus()).thenReturn(GameStatus.ONGOING);
        when(playerMock.getGames()).thenReturn(gamePlayersSetMock);

        // mock active game page
        Game game2 = mock(Game.class);
        Set<GamePlayer> gamePlayers = new HashSet<GamePlayer>();
        gamePlayers.add(gamePlayerMock);
        when(game2.getPlayers()).thenReturn(gamePlayers);
        when(preGameBeanMock.getGameById(anyLong())).thenReturn(gameMock).thenReturn(game2);

        when(playerActionsInterfaceMock.isPlayerTurn(anyLong(),anyLong())).thenReturn(true);

        // When: user clicks the game button
        tester.startPage(GamesPage.class);
        FormTester formtester = tester.newFormTester("CreateGame");
        formtester.submit("wmc-current-games:rows:0:button");

        // Then: user reaches active game page
        tester.assertRenderedPage(ActiveGamePage.class);
    }

    @Test
    public void noPlayerFoundException() throws NoPlayerFoundException {
        // Given: user is authorized
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));

        // When: user hits gamePage and do not exist in database
        doThrow(NoPlayerFoundException.class).when(preGameBeanMock).getPlayerById(anyLong());
        tester.startPage(GamesPage.class);

        // Then: infomessage is displayed
        Assert.assertEquals("You seem to not exist as a player, wich is weird",
                tester.getTagById("msg").getValue());
        tester.assertVisible("CreateGame:msg");
    }

    @Test
    public void testCreateGameButtonClick() throws NoPlayerFoundException {
        // Given: authorized user
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));

        when(preGameBeanMock.getPlayerById(anyLong())).thenReturn(playerMock);
        Set<PlayerFriend> friendsSetMock = new HashSet<PlayerFriend>();
        when(playerMock.getFriends()).thenReturn(friendsSetMock);

        // When: clicking create game button
        tester.startPage(GamesPage.class);
        FormTester formTester = tester.newFormTester("CreateGame");
        formTester.submit("createGameButton");

        // Then: click went ok
        tester.assertNoErrorMessage();
        tester.assertNoInfoMessage();
        tester.assertRenderedPage(CreateGamePage.class);
    }

    @Test
    public void checkInvitedGamesArePopulated() throws NoPlayerFoundException {
        // Given: authorized user that has a invited game
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));

        setupInviteMock();

        // When: hitting GamesPage
        tester.startPage(GamesPage.class);

        // Then: Table is populated correctly
        tester.assertLabel("CreateGame:wmc-invited-games:inviteRows:0:invitedGameId","12");
        tester.assertLabel("CreateGame:wmc-invited-games:inviteRows:0:invitedGameName","INVITED GAME NAME");
        tester.assertVisible("CreateGame:wmc-invited-games:inviteRows:0:invitedGameId");
    }

    @Test
    public void acceptGameInvite() throws NoPlayerFoundException {
        // Given: authorized user that has a invited game
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));

        setupInviteMock();

        // add mock when accepting invite
        when(preGameBeanMock.acceptInvite(anyLong(),anyLong())).thenReturn(true);

        tester.startPage(GamesPage.class);

        ListView<GamePlayer> invitedGamesBefore = (ListView<GamePlayer>)
                tester.getComponentFromLastRenderedPage("CreateGame:wmc-invited-games:inviteRows");
        ListView<GamePlayer> currentGamesBefore = (ListView<GamePlayer>)
                tester.getComponentFromLastRenderedPage("CreateGame:wmc-current-games:rows");
        Assert.assertEquals(0,currentGamesBefore.size());
        Assert.assertEquals(1,invitedGamesBefore.size());

        // When: clicks invite button
        FormTester formtester = tester.newFormTester("CreateGame");
        formtester.submit("wmc-invited-games:inviteRows:0:acceptbutton");

        // Then: invite row is removed and active game increased with one
        tester.debugComponentTrees();
        ListView<GamePlayer> invitedGamesAfter = (ListView<GamePlayer>)
                tester.getComponentFromLastRenderedPage("CreateGame:wmc-invited-games:inviteRows");
        ListView<GamePlayer> currentGamesAfter = (ListView<GamePlayer>)
                tester.getComponentFromLastRenderedPage("CreateGame:wmc-current-games:rows");

        Assert.assertEquals(0,invitedGamesAfter.size());
        Assert.assertEquals(1,currentGamesAfter.size());
    }

    @Test
    public void declineGameInvite() throws NoPlayerFoundException {
        // Given: authorized user that has a invited game
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));

        setupInviteMock();

        // add mock when declining invite
        when(preGameBeanMock.declineInvite(anyLong(), anyLong())).thenReturn(true);

        tester.startPage(GamesPage.class);

        ListView<GamePlayer> invitedGamesBefore = (ListView<GamePlayer>)
                tester.getComponentFromLastRenderedPage("CreateGame:wmc-invited-games:inviteRows");
        ListView<GamePlayer> currentGamesBefore = (ListView<GamePlayer>)
                tester.getComponentFromLastRenderedPage("CreateGame:wmc-current-games:rows");
        Assert.assertEquals(0,currentGamesBefore.size());
        Assert.assertEquals(1,invitedGamesBefore.size());

        // When: clicks decline button
        FormTester formtester = tester.newFormTester("CreateGame");
        formtester.submit("wmc-invited-games:inviteRows:0:declinebutton");

        // Then: decline row is removed and current game does not get added
        ListView<GamePlayer> invitedGamesAfter = (ListView<GamePlayer>)
                tester.getComponentFromLastRenderedPage("CreateGame:wmc-invited-games:inviteRows");
        ListView<GamePlayer> currentGamesAfter = (ListView<GamePlayer>)
                tester.getComponentFromLastRenderedPage("CreateGame:wmc-current-games:rows");

        Assert.assertEquals(0,invitedGamesAfter.size());
        Assert.assertEquals(0,currentGamesAfter.size());
    }

    @Test
    @Ignore
    public void checkDisabledGoButtonForNotStartedGame() {

    }

    @Test
    @Ignore
    public void testTotalCount() {

    }

    @Test
    @Ignore
    public void testAcceptCount() {

    }

    private void setupInviteMock() throws NoPlayerFoundException {
        when(preGameBeanMock.getPlayerByName(any(String.class))).thenReturn(playerMock);
        Iterator<GamePlayer> gamePlayersIteratorMock = mock(Iterator.class);
        Set<GamePlayer> gamePlayersSetMock = mock(Set.class);
        GamePlayer gamePlayerMock = mock(GamePlayer.class);
        Game gameMock = mock(Game.class);
        when(gameMock.getName()).thenReturn("INVITED GAME NAME");
        when(gamePlayersSetMock.isEmpty()).thenReturn(false);
        when(gamePlayersSetMock.size()).thenReturn(1);
        when(gamePlayersSetMock.iterator()).thenReturn(gamePlayersIteratorMock);
        when(gamePlayersIteratorMock.hasNext()).thenReturn(true).thenReturn(false);
        when(gamePlayersIteratorMock.next()).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getGameId()).thenReturn(12L);
        when(gamePlayerMock.getGame()).thenReturn(gameMock);
        when(gamePlayerMock.getGamePlayerStatus()).thenReturn(GamePlayerStatus.INVITED);
        when(gameMock.getGameStatus()).thenReturn(GameStatus.CREATED);
        when(playerMock.getGames()).thenReturn(gamePlayersSetMock);
        when(preGameBeanMock.getGameById(anyLong())).thenReturn(gameMock);
    }

}
