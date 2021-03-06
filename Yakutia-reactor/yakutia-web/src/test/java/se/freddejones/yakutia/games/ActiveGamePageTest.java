package se.freddejones.yakutia.games;

import org.apache.wicket.util.tester.TagTester;
import org.junit.Ignore;
import se.freddejones.yakutia.application.service.exceptions.NoPlayerFoundException;
import se.freddejones.yakutia.application.service.iface.PlayerActionsInterface;
import se.freddejones.yakutia.application.service.iface.PreGameInterface;
import se.freddejones.yakutia.auth.SignIn;
import se.freddejones.yakutia.base.ErrorPage;
import se.freddejones.yakutia.entity.Game;
import se.freddejones.yakutia.entity.GamePlayer;
import se.freddejones.yakutia.entity.Player;
import se.freddejones.yakutia.harness.Authorizer;
import se.freddejones.yakutia.harness.OauthMockHarness;
import se.freddejones.yakutia.harness.beanMocker.MyMockApplication;
import se.freddejones.yakutia.session.MySession;
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Fredde
 * Date: 7/21/13 12:13 AM
 */
public class ActiveGamePageTest extends OauthMockHarness {

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
        when(preGameBeanMock.getPlayerByName(any(String.class))).thenReturn(playerMock);

        tester = new WicketTester(new MyMockApplication(
                new Object[]{preGameBeanMock, playerActionsInterfaceMock}));
    }

    @Test
    public void playerNotAllowedToPlayNonConnectedGame() {

        // Given: page authorized player, not part of game
        authorize();
        PageParameters params = new PageParameters();
        params.add("gameId","1");
        Game gameMock = mock(Game.class);
        GamePlayer gamePlayerMock = mock(GamePlayer.class);
        Set<GamePlayer> gpSet = new HashSet<GamePlayer>();
        gpSet.add(gamePlayerMock);

        when(preGameBeanMock.getGameById(anyLong())).thenReturn(gameMock);
        when(gameMock.getPlayers()).thenReturn(gpSet);
        when(gamePlayerMock.getPlayerId()).thenReturn(2L);

        // When: hitting active game page
        tester.startPage(ActiveGamePage.class,params);

        // Then: Error page is shown
        tester.assertRenderedPage(ErrorPage.class);
    }

    @Test
    public void notCurrentPlayersTurn() {

        // Given: authorized player, but not current players turn
        authorize();
        ((MySession)tester.getSession()).setPlayerId(1L);
        PageParameters params = new PageParameters();
        params.add("gameId","1");
        Game gameMock = mock(Game.class);
        GamePlayer gamePlayerMock = mock(GamePlayer.class);
        GamePlayer gamePlayerMockOtherPlayer = mock(GamePlayer.class);
        Set<GamePlayer> gpSet = new HashSet<GamePlayer>();
        gpSet.add(gamePlayerMock);
        gpSet.add(gamePlayerMockOtherPlayer);

        when(preGameBeanMock.getGameById(anyLong())).thenReturn(gameMock);
        when(gameMock.getPlayers()).thenReturn(gpSet);
        when(gamePlayerMock.getPlayerId()).thenReturn(1L);

        when(playerActionsInterfaceMock.isPlayerTurn(anyLong(),anyLong())).thenReturn(false);

        // When: hitting the active game page
        tester.startPage(ActiveGamePage.class, params);

        // Then: message is shown that it is not your turn
        tester.assertLabel("message", "Not your turn");

        // And Then:
        tester.assertInvisible("wmc-actionform");
    }

    @Test
    public void notAuthorizationTest() {

        // Given: unauthorized user
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("FOO")));

        // When: hitting active game page
        tester.startPage(ActiveGamePage.class);

        // Then: Sign in page is shown
        tester.assertRenderedPage(SignIn.class);
    }

    @Test
    @Ignore
    public void checkThatNonPlayerAreasAreInRedColor() {

//        // Given: authorized user and a started game
//        authorize();
//        ((MySession)tester.getSession()).setPlayerId(1L);
//        PageParameters params = new PageParameters();
//        params.add("gameId","1");
//        Game gameMock = mock(Game.class);
//        GamePlayer gamePlayerMock = mock(GamePlayer.class);
//        GamePlayer gamePlayerMockOtherPlayer = mock(GamePlayer.class);
//        Set<GamePlayer> gpSet = new HashSet<GamePlayer>();
//        gpSet.add(gamePlayerMock);
//        gpSet.add(gamePlayerMockOtherPlayer);
//
//        when(preGameBeanMock.getGameById(anyLong())).thenReturn(gameMock);
//        when(gameMock.getPlayers()).thenReturn(gpSet);
//        when(gamePlayerMock.getPlayerId()).thenReturn(1L);
//
//        when(playerActionsInterfaceMock.isPlayerTurn(anyLong(),anyLong())).thenReturn(true);
//
//        // When: hitting active game page
//        tester.startPage(ActiveGamePage.class, params);
//
//        // Then:
//        System.out.println(tester.getLastResponseAsString());
//
    }

    @Test
    @Ignore
    public void checkCountryDivsArePopulatedWithCorrectUnitInformation() {

        // Given:

        // When:

        // Then:
    }

    private void authorize() {
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));
    }
}
