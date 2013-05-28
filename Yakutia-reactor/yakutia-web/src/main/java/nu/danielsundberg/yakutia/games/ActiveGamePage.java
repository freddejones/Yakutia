package nu.danielsundberg.yakutia.games;

import nu.danielsundberg.yakutia.base.NavbarPage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.naming.NamingException;

/**
 * User: Fredde
 * Date: 5/14/13 11:41 PM
 */
@AuthorizeInstantiation("USER")
public class ActiveGamePage extends NavbarPage {

    public ActiveGamePage(PageParameters parameters) throws NamingException {
        super(parameters);

        String gameId = String.valueOf(parameters.get("gameId"));

        Label gameMsgId = new Label("gameIdMsg","Game Id: " + gameId);
        add(gameMsgId);
    }
}
