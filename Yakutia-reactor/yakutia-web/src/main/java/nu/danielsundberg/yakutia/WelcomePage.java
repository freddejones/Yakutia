package nu.danielsundberg.yakutia;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.*;

import javax.naming.NamingException;

/**
 * User: Fredde
 * Date: 3/26/13 11:22 PM
 */
@AuthorizeInstantiation("USER")
public class WelcomePage extends NavbarPage {

    public WelcomePage(PageParameters parameters) throws NamingException {
        super(parameters);

        StringValue playerName = parameters.get("playername");

        add(new Label("welcomeMsg", "Welcome to Yakutia, " + playerName));

    }
}
