package nu.danielsundberg.yakutia.session;

import junit.framework.Assert;
import nu.danielsundberg.yakutia.application.service.exceptions.NoPlayerFoundException;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.harness.preGameBeanMock.MyMockApplication;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Fredde
 * Date: 5/19/13 1:49 AM
 */
public class MySessionTest {

    private PreGameInterface preGameInterfaceMock;
    private WicketTester tester;

    @Before
    public void setup() {
        preGameInterfaceMock = mock(PreGameInterface.class);
        tester = new WicketTester(new MyMockApplication(preGameInterfaceMock));
    }

    @Test
    public void noPlayerExists() throws NoPlayerFoundException {

        // Given: a session from applicaton
        MySession session = (MySession) tester.getSession();

        // When: a email that does not exists retunr
        when(preGameInterfaceMock.playerExists(any(String.class))).thenReturn(false);

        // Then: false is returned
        Assert.assertFalse(session.authenticate("noEmailExists@blaha.com", "passwd"));
    }

    @Test
    public void getPlayerByEmail() throws NoPlayerFoundException {
        // Given: a session from applicaton
        MySession session = (MySession) tester.getSession();

        // When: a player is found in database
        when(preGameInterfaceMock.playerExists(any(String.class))).thenReturn(true);
        Player p = mock(Player.class);
        when(preGameInterfaceMock.getPlayerByEmail(any(String.class))).thenReturn(p);
        when(p.getName()).thenReturn("myname");
        when(p.getPlayerId()).thenReturn(10L);

        // Then: playername is set to session + playerid and session is granted
        Assert.assertTrue(session.authenticate("emailExists@blaha.com", "passwd"));
        Assert.assertEquals("myname",session.getPlayerName());
        Assert.assertEquals(10L, (long)session.getPlayerId());
    }

    @Test
    public void getRolesTestWhenAuthenticated() throws NoPlayerFoundException {
        // Given: a session from applicaton
        MySession session = (MySession) tester.getSession();

        // When: a player is found in database and signing in
        when(preGameInterfaceMock.playerExists(any(String.class))).thenReturn(true);
        Player p = mock(Player.class);
        when(preGameInterfaceMock.getPlayerByEmail(any(String.class))).thenReturn(p);
        when(p.getName()).thenReturn("myname");
        when(p.getPlayerId()).thenReturn(10L);
        session.signIn("anything", "stuff");

        // Then: session has roles set to USER
        session = (MySession) tester.getSession();
        Roles roles = session.getRoles();
        Assert.assertEquals("USER", roles.toString());
    }

    @Test
    public void getRolesTestWhenNotAuthenticated() throws NoPlayerFoundException {
        // Given: existing player in database

        // When: a player is found in database and signing in
        when(preGameInterfaceMock.playerExists(any(String.class))).thenReturn(true);
        Player p = mock(Player.class);
        when(preGameInterfaceMock.getPlayerByEmail(any(String.class))).thenReturn(p);
        when(p.getName()).thenReturn("myname");
        when(p.getPlayerId()).thenReturn(10L);

        // Then: session has roles set to USER
        MySession session = (MySession) tester.getSession();
        Roles roles = session.getRoles();
        Assert.assertEquals(true, roles.isEmpty());
    }

}