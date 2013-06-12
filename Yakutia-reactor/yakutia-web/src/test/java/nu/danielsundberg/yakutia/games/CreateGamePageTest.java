package nu.danielsundberg.yakutia.games;

import nu.danielsundberg.yakutia.application.service.exceptions.NoPlayerFoundException;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.entity.GamePlayer;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.harness.Authorizer;
import nu.danielsundberg.yakutia.harness.preGameBeanMock.MyMockApplication;
import nu.danielsundberg.yakutia.session.MySession;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;
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
        tester = new WicketTester(new MyMockApplication(preGameBeanMock));
    }

    @Test
    public void checkCorrectRole() {
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));
        tester.startPage(CreateGamePage.class);
        tester.assertRenderedPage(CreateGamePage.class);
    }

    // TODO change to freinds when that exists
    @Test
    public void createAGameAndCheckCorrectCallbackPage() throws NoPlayerFoundException {
        // Given: authorized user, created a game
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));
        tester.startPage(CreateGamePage.class);

        ((MySession)tester.getSession()).setPlayerId(1L);
        when(preGameBeanMock.getPlayers()).thenReturn(getPlayerList());
        when(preGameBeanMock.createNewGame(anyLong(),anyString())).thenReturn(666L);
        FormTester formTester = tester.newFormTester("form1");
        formTester.setValue("gamename","GAMENAME!!");

        // Mock the gamesPage page:
        ((MySession)tester.getSession()).setPlayerName("PLAYER");
        when(preGameBeanMock.getPlayerByName(anyString())).thenReturn(playerMock);
        when(playerMock.getGames()).thenReturn(new HashSet<GamePlayer>());

        // When: clicking the button
        formTester.submit("submit1");

        // Then: user get redirected back to Games page
        tester.assertRenderedPage(GamesPage.class);
    }

    // TODO change to friends when that exists
    @Test
    public void noPlayersExists() {

    }

    @Test
    public void clickSelectionBox() throws NoPlayerFoundException {
        // Given: authorized user, created a game
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));


        // Mock the gamesPage page:
        ((MySession)tester.getSession()).setPlayerName("PLAYER");
        when(preGameBeanMock.getPlayerByName(anyString())).thenReturn(playerMock);
        when(playerMock.getGames()).thenReturn(new HashSet<GamePlayer>());

        ((MySession)tester.getSession()).setPlayerId(1L);
        when(playerMock.getName()).thenReturn("PLAYER");
        List<Player> players = getPlayerList();
        players.add(playerMock);
        System.out.println(players.size());
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

        List<Player> actualAddedPlayersAfter = (List<Player>)tester.getComponentFromLastRenderedPage("form1:wmc:playersAdded").getDefaultModelObject();
        Assert.assertEquals(2, actualAddedPlayersAfter.size());
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
