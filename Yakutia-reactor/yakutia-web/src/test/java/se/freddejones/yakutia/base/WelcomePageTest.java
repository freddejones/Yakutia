package se.freddejones.yakutia.base;

import se.freddejones.yakutia.application.service.exceptions.NoPlayerFoundException;
import se.freddejones.yakutia.application.service.iface.PreGameInterface;
import se.freddejones.yakutia.entity.Player;
import se.freddejones.yakutia.harness.beanMocker.MyMockApplication;
import se.freddejones.yakutia.session.MySession;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Fredde
 * Date: 5/20/13 10:08 PM
 */
public class WelcomePageTest {

    private PreGameInterface preGameInterfaceMock;
    private WicketTester tester;

    @Before
    public void setup() {
        preGameInterfaceMock = mock(PreGameInterface.class);
        tester = new WicketTester(new MyMockApplication(new Object[]{preGameInterfaceMock}));
    }

    @Test
    public void checkUserIsStatedWelcomePage() throws NoPlayerFoundException {

        // Given: a player named myname
        MySession session = (MySession) tester.getSession();
        when(preGameInterfaceMock.playerExists(any(String.class))).thenReturn(true);
        Player p = mock(Player.class);
        when(preGameInterfaceMock.getPlayerByEmail(anyString())).thenReturn(p);
        when(p.getName()).thenReturn("myname");
        when(p.getPlayerId()).thenReturn(10L);

        // When: signing in and going to welcomepage
        session.signIn("anything", "stuff");
        tester.startPage(WelcomePage.class);

        // Then: correct welcome page is shown
        tester.assertLabel("welcomeMsg", "Welcome to Yakutia, myname");
    }
}
