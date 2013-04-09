package nu.danielsundberg.yakutia;



import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;

@AuthorizeInstantiation("USER")
public class JpaPage extends NavbarPage {

    public JpaPage(PageParameters parameters) throws NamingException {
        super(parameters);

        InitialContext ctx = new InitialContext();
        PreGameInterface test = (PreGameInterface) ctx.lookup("preGameBean");


        List<PlayerApi> players = test.getPlayers();
        add(new ListView<PlayerApi>("rows", players)
        {
            public void populateItem(final ListItem<PlayerApi> item)
            {
                final PlayerApi player = item.getModelObject();
                item.add(new Label("name", player.getPlayerName()));
                item.add(new Label("id", player.getPlayerId().toString()));
            }
        });

    }
}
