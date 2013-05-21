package nu.danielsundberg.yakutia.friends;

import nu.danielsundberg.yakutia.WicketApplication;
import nu.danielsundberg.yakutia.games.GamesPage;
import nu.danielsundberg.yakutia.harness.Authorizer;
import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

/**
 * User: Fredde
 * Date: 5/21/13 11:02 PM
 */
public class FriendsPageTest {


    @Test
    public void testPage() {
        WicketTester tester = new WicketTester(new WicketApplication());
        tester.getApplication().getSecuritySettings().setAuthorizationStrategy(
                new RoleAuthorizationStrategy(new Authorizer("USER")));
        tester.startPage(FriendsPage.class);
    }

}
