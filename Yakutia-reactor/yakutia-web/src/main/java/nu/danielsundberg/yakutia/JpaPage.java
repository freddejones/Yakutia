package nu.danielsundberg.yakutia;



import nu.danielsundberg.yakutia.GameEngineInterface;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;

public class JpaPage extends HomePage {

    public JpaPage(PageParameters parameters) throws NamingException {
        super(parameters);

        InitialContext ctx = new InitialContext();
        GameEngineInterface test = (GameEngineInterface) ctx.lookup("kickass");


        List<String> players = test.getPlayers();
        add(new ListView<String>("rows", players)
        {
            public void populateItem(final ListItem<String> item)
            {
                final String player = item.getModelObject();
                item.add(new Label("name", player));
            }
        });

    }
}
