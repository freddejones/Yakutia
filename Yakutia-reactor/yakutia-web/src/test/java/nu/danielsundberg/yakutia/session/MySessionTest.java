package nu.danielsundberg.yakutia.session;

import junit.framework.Assert;
import nu.danielsundberg.yakutia.WicketApplication;
import nu.danielsundberg.yakutia.application.service.exceptions.NoPlayerFoundException;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.harness.preGameBeanMock.MyMockApplication;
import org.apache.wicket.mock.MockWebRequest;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Locale;

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
        Assert.assertFalse(session.authenticate("noEmailExists@blaha.com","passwd"));
    }

}
