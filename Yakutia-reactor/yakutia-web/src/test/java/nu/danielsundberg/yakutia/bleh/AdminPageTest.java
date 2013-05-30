package nu.danielsundberg.yakutia.bleh;

import nu.danielsundberg.yakutia.WicketApplication;
import nu.danielsundberg.yakutia.application.service.exceptions.NoPlayerFoundException;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.games.CreateGamePage;
import nu.danielsundberg.yakutia.harness.Authorizer;
import nu.danielsundberg.yakutia.harness.preGameBeanMock.MyMockApplication;
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * User: Fredde
 * Date: 5/30/13 12:39 AM
 */
public class AdminPageTest {

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
    public void fireUpPageTest() {
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("ADMIN")));
        tester.startPage(ApiPage.class);
        tester.assertRenderedPage(ApiPage.class);
    }
}
