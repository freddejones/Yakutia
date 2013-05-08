package nu.danielsundberg.yakutia.games;

import junit.framework.Assert;
import nu.danielsundberg.yakutia.NavbarPage;
import nu.danielsundberg.yakutia.SignIn;
import nu.danielsundberg.yakutia.WicketApplication;
import nu.danielsundberg.yakutia.harness.Authorizer;
import nu.danielsundberg.yakutia.harness.MyMockApplication;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.authroles.authorization.strategies.role.IRoleCheckingStrategy;
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import javax.naming.Context;
import java.io.Serializable;

/**
 * User: Fredde
 * Date: 5/6/13 11:13 PM
 */
public class GamesPageTest {

    private WicketTester tester;

    @Before
    public void setUp() {

        tester = new WicketTester(new MyMockApplication());
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
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));

        tester.startPage(GamesPage.class);

        Assert.assertEquals("No games for you", tester.getTagById("msg").getValue());

        tester.assertVisible("msg");
        tester.assertInvisible("msg2");
    }


}
