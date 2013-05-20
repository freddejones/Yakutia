package nu.danielsundberg.yakutia.auth;

import junit.framework.Assert;
import nu.danielsundberg.yakutia.WelcomePage;
import nu.danielsundberg.yakutia.application.service.exceptions.NoPlayerFoundException;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.harness.preGameBeanMock.MyMockApplication;
import nu.danielsundberg.yakutia.session.MySession;
import nu.danielsundberg.yakutia.session.SignOut;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Fredde
 * Date: 5/20/13 10:26 PM
 */
public class SignOutTest {

    private PreGameInterface preGameInterfaceMock;
    private WicketTester tester;

    @Before
    public void setup() throws NoPlayerFoundException {
        preGameInterfaceMock = mock(PreGameInterface.class);
        tester = new WicketTester(new MyMockApplication(preGameInterfaceMock));
        when(preGameInterfaceMock.playerExists(any(String.class))).thenReturn(true);
        Player p = mock(Player.class);
        when(preGameInterfaceMock.getPlayerByEmail(any(String.class))).thenReturn(p);
        when(p.getName()).thenReturn("myname");
        when(p.getPlayerId()).thenReturn(10L);
        MySession session = (MySession) tester.getSession();
        session.signIn("anything", "stuff");
    }

    @Test
    public void checkInvalidatedSessionWhenLogout() throws NoPlayerFoundException {

        // Given: a player named signedIn at WelcomePage
        tester.startPage(WelcomePage.class);

        // When: clicking logout
        tester.startPage(SignOut.class); // Fake click a navbar button

        // Then: Session is invalidated
        MySession session = (MySession) tester.getSession();
        Assert.assertFalse(session.isSignedIn());
        Assert.assertTrue(session.isSessionInvalidated());
    }



    @Test
    public void testHomeLink() {
        // Given: user is logged out
        tester.startPage(SignOut.class);

        // When: clicking home link
        tester.clickLink("homelink");

        // Then: goes to sign in page
        tester.assertRenderedPage(SignIn.class);
    }

}
