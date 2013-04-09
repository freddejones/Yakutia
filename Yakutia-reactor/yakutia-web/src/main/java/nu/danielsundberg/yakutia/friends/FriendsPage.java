package nu.danielsundberg.yakutia.friends;



import nu.danielsundberg.yakutia.NavbarPage;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.naming.NamingException;

@AuthorizeInstantiation("USER")
public class FriendsPage extends NavbarPage {

    public FriendsPage(PageParameters parameters) throws NamingException {
        super(parameters);

    }
}
