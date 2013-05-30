package nu.danielsundberg.yakutia.games;



import nu.danielsundberg.yakutia.base.NavbarPage;
import nu.danielsundberg.yakutia.application.service.exceptions.NoPlayerFoundException;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.session.MySession;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;

@AuthorizeInstantiation("USER")
public class CreateGamePage extends NavbarPage {

    @EJB(name = "pregamebean")
    private PreGameInterface preGameInterface;


    public CreateGamePage(PageParameters parameters) throws NamingException {
        super(parameters);

        List<Player> list = new ArrayList<Player>();


        List<Player> listTmp = preGameInterface.getPlayers();
        for (Player p : listTmp) {
            MySession session = (MySession) getSession();
            if (!p.getName().equals(session.getPlayerName())) {
                list.add(p);
            }
        }

        List<Player> players = list;
        final ListView<Player> listview = new ListView<Player>("players", players)
        {
            public void populateItem(final ListItem<Player> item)
            {
                final Player player = item.getModelObject();
                final Label playerName = new Label("playername", player.getName());
                playerName.setOutputMarkupId(true);

                item.add(playerName);
                Label status = new Label("status", "Will be Invited!");
                item.add(new Check<Player>("checkbox", item.getModel()));
                item.add(status);
            }
        };

        final CheckGroup group=new CheckGroup("group",listview.getModel());
        listview.setReuseItems(true);
        listview.setOutputMarkupId(true);
        group.add(listview);
        group.setOutputMarkupId(true);

        Form form = new Form("form1");
        final TextField gameNameTextField = new TextField("gamename");
        gameNameTextField.add(new AttributeAppender("value", new Model("Enter game name")));
        form.add(gameNameTextField);
        Button button = new Button("submit1") {

            @Override
            public void onSubmit() {
                List<Player> players = (ArrayList<Player>)group.getDefaultModelObject();
                MySession session = (MySession) getSession();
                long gameId = preGameInterface.createNewGame(session.getPlayerId(),gameNameTextField.getInput());
                for (Player p : players) {
                    preGameInterface.invitePlayerToGame(p.getPlayerId(),gameId);
                }
                setResponsePage(GamesPage.class);
            }
        }.setDefaultFormProcessing(false);
        form.add(button);
        form.add(group);
        add(form);

    }
}
