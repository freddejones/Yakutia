package nu.danielsundberg.yakutia.auth;

import junit.framework.Assert;
import nu.danielsundberg.yakutia.WelcomePage;
import nu.danielsundberg.yakutia.application.service.exceptions.NoPlayerFoundException;
import nu.danielsundberg.yakutia.application.service.exceptions.PlayerAlreadyExists;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.harness.preGameBeanMock.MyMockApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Fredde
 * Date: 5/21/13 11:47 PM
 */
public class CreateAccountPageTest {

    private WicketTester tester;
    private PreGameInterface preGameInterfaceMock;

    @Before
    public void setup() {
        preGameInterfaceMock = mock(PreGameInterface.class);
        tester = new WicketTester(new MyMockApplication(preGameInterfaceMock));
    }

    @Test
    public void testEmailProvidedInPageParametersIsDisplayed() {
        // Given: page parameter provides email
        PageParameters pg = new PageParameters();
        pg.add("email","fidde@email.com");

        // When: hitting createaccountpage
        tester.startPage(CreateAccountPage.class, pg);

        // Then: email is prefilled in form
        FormTester formTester = tester.newFormTester("form");
        Assert.assertEquals("fidde@email.com", formTester.getTextComponentValue("email"));
    }


    @Test
    public void testCreateANewUser() throws PlayerAlreadyExists, NoPlayerFoundException {
        // Given: user has provided a new player name that does not exist already
        PageParameters pg = new PageParameters();
        pg.add("email","fidde@email.com");
        tester.startPage(CreateAccountPage.class, pg);

        FormTester formTester = tester.newFormTester("form");
        formTester.setValue("playername","fidde");

        when(preGameInterfaceMock.createNewPlayer(isA(String.class),isA(String.class))).thenReturn(1L);
        when(preGameInterfaceMock.playerExists(notNull(String.class))).thenReturn(true);
        Player p = mock(Player.class);
        when(preGameInterfaceMock.getPlayerByEmail(notNull(String.class))).thenReturn(p);
        when(p.getName()).thenReturn("fidde");
        when(p.getPlayerId()).thenReturn(1L);

        // When: clicking the create player button
        formTester.submit("createButton");

        // Then: user is directly passed to welcome page
        tester.assertRenderedPage(WelcomePage.class);
    }
}
