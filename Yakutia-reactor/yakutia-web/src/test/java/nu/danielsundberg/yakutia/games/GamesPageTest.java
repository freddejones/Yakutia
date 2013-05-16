package nu.danielsundberg.yakutia.games;

import junit.framework.Assert;
import nu.danielsundberg.yakutia.SignIn;
import nu.danielsundberg.yakutia.entity.Game;
import nu.danielsundberg.yakutia.entity.GamePlayer;
import nu.danielsundberg.yakutia.entity.GameStatus;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.harness.Authorizer;
import nu.danielsundberg.yakutia.harness.preGameBeanMock.MockFactory;
import nu.danielsundberg.yakutia.harness.preGameBeanMock.MyMockApplication;
import nu.danielsundberg.yakutia.harness.preGameBeanMock.PreGameBeanMock;
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import static org.mockito.Mockito.*;

/**
 * User: Fredde
 * Date: 5/6/13 11:13 PM
 */
public class GamesPageTest {

    private WicketTester tester;
    private PreGameBeanMock preGameBeanMock;
    private Player playerMock;

    @Before
    public void setUp() {
        preGameBeanMock = mock(PreGameBeanMock.class);
        playerMock = mock(Player.class);
        when(playerMock.getName()).thenReturn("any");
        when(preGameBeanMock.getPlayerByName(any(String.class))).thenReturn(playerMock);

        tester = new WicketTester(new MyMockApplication(preGameBeanMock));
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
    public void checkPageWhenNoGamesExists() {
        // Given: user is authorized with 0 games
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));

        // When: user hits gamepage
        tester.startPage(GamesPage.class);

        // Then: no games are shown
        System.out.println(tester.getTagById("msg").getValue());
        Assert.assertEquals("No games for you", tester.getTagById("msg").getValue());
        tester.assertVisible("msg");
    }

    @Test
    public void checkPageWhenGamesExist() {
        // Given: user is authorized with 1 games
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));
        when(preGameBeanMock.getPlayerByName(any(String.class))).thenReturn(playerMock);

        Iterator<GamePlayer> gamePlayersIteratorMock = mock(Iterator.class);
        Set<GamePlayer> gamePlayersSetMock = mock(Set.class);
        GamePlayer gamePlayerMock = mock(GamePlayer.class);
        Game gameMock = mock(Game.class);

        when(gamePlayersSetMock.isEmpty()).thenReturn(false);
        when(gamePlayersSetMock.size()).thenReturn(1);
        when(gamePlayersSetMock.iterator()).thenReturn(gamePlayersIteratorMock);
        when(gamePlayersIteratorMock.hasNext()).thenReturn(true).thenReturn(false);
        when(gamePlayersIteratorMock.next()).thenReturn(gamePlayerMock);
        when(gamePlayerMock.getGameId()).thenReturn(12L);
        when(gamePlayerMock.getGame()).thenReturn(gameMock);
        when(gameMock.getGameStatus()).thenReturn(GameStatus.ONGOING);


//        Set<GamePlayer> setMock = new HashSet<GamePlayer>() {
//
//            private Stack<Boolean> booleans;
//
//
//            @Override
//            public int size() {
//                booleans = new Stack<Boolean>();
//                booleans.push(false);
//                booleans.push(true);
//                return 1;
//            }
//
//            @Override
//            public boolean isEmpty() {
//                return false;
//            }
//
//            @Override
//            public Iterator iterator() {
//                return gamePlayersIteratorMock;
//            }
//
//        };


        when(playerMock.getGames()).thenReturn(gamePlayersSetMock);

        // When: user hits gamepage
        tester.startPage(GamesPage.class);

        // Then: number of games are shown
//        System.out.println(tester.getTagByWicketId("rows:gameid").getValue());
//        System.out.println(tester.getTagsByWicketId("gameid").get(0).getValue());
//        System.out.println(tester.getTagById("rows:gameid").getValue());
        tester.assertInvisible("msg");
    }

    @Test
    public void testCreateGameButtonClick() {
        // Given: authorized user
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));

        // When: clicking create game button
        tester.startPage(GamesPage.class);
        FormTester formTester = tester.newFormTester("CreateGame");
        formTester.submit("createGameButton");

        // Then: click went ok
        tester.assertNoErrorMessage();
        tester.assertNoInfoMessage();
        tester.assertRenderedPage(CreateGamePage.class);
    }


}
