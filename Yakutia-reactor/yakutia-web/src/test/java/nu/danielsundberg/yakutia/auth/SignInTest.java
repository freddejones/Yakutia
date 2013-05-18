package nu.danielsundberg.yakutia.auth;

import junit.framework.Assert;
import nu.danielsundberg.yakutia.WicketApplication;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import javax.naming.NamingException;

/**
 * User: Fredde
 * Date: 5/18/13 1:37 AM
 */
public class SignInTest {

    private WicketTester tester;

    @Before
    public void setup() {
        tester = new WicketTester(new WicketApplication());
    }

    @Test
    public void getOAuthService() throws NamingException {
        SignIn signIn = new SignIn(null);
        Assert.assertNotNull(signIn.getOAuthService());
    }

    @Test
    public void signInWithOAuth() {
        // Given: a user

        // When: a user hits sign in and clicks sign in with google
        tester.startPage(SignIn.class);
        FormTester formTester = tester.newFormTester("google-form");
        tester.setFollowRedirects(false);
        formTester.submit("google");

        // Then: page gets redirected
        Assert.assertTrue(tester.getLastResponse().isRedirect());
    }

}
