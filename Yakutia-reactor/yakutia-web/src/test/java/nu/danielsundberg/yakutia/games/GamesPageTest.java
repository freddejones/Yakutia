package nu.danielsundberg.yakutia.games;

import junit.framework.Assert;
import nu.danielsundberg.yakutia.SignIn;
import nu.danielsundberg.yakutia.entity.GamePlayer;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.harness.Authorizer;
import nu.danielsundberg.yakutia.harness.preGameBeanMock.MockFactory;
import nu.danielsundberg.yakutia.harness.preGameBeanMock.MyMockApplication;
import nu.danielsundberg.yakutia.harness.preGameBeanMock.PreGameBeanMock;
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

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
        Assert.assertEquals("No games for you", tester.getTagById("msg").getValue());
        tester.assertVisible("msg");
        tester.assertInvisible("msg2");
    }

    @Test
    public void checkPageWhenGamesExist() {
        // Given: user is authorized with 1 games
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));
        when(preGameBeanMock.getPlayerByName(any(String.class))).thenReturn(playerMock);
        Set<GamePlayer> setMock = new HashSet<GamePlayer>() {
            @Override
            public int size() {
                return 3;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

        };
        when(playerMock.getGames()).thenReturn(setMock);

        // When: user hits gamepage
        tester.startPage(GamesPage.class);

        // Then: number of games are shown
        System.out.println(tester.getTagById("msg2"));
        Assert.assertEquals("3", tester.getTagById("msg2").getValue());
        tester.assertInvisible("msg");
        tester.assertVisible("msg2");
    }


}
