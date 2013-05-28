package nu.danielsundberg.yakutia.games;



import nu.danielsundberg.yakutia.base.NavbarPage;
import nu.danielsundberg.yakutia.application.service.exceptions.NoPlayerFoundException;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.session.MySession;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;

@AuthorizeInstantiation("USER")
public class CreateGamePage extends NavbarPage {

    public String message = "Nada";

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
                Label status = new Label("status", "Invited!");
                item.add(new Check<Player>("checkbox", item.getModel()));
                item.add(status);
            }
        };
//        final CheckGroup group=new CheckGroup("group",listview.getModel());
        final CheckGroup group=new CheckGroup("group",listview.getModel());
        listview.setReuseItems(true);
        listview.setOutputMarkupId(true);
        group.add(listview);
        group.setOutputMarkupId(true);

        final Label test = new Label("msg", message);
        test.setOutputMarkupId(true);

        Form form = new Form("form1");
        Button button = new Button("submit1") {

            @Override
            public void onSubmit() {
                List<Player> players = (ArrayList<Player>)group.getDefaultModelObject();
                System.out.println(players.get(0).getName());

                try {
                    InitialContext ctx = new InitialContext();
//                    GameEngineInterface gameBean = (GameEngineInterface) ctx.lookup("kickass");
                    PreGameInterface preGame = (PreGameInterface) ctx.lookup("preGameBean");

                    // TODO Switch session stuff when fixed player id to session
                    MySession session = (MySession) getSession();
                    long gameId = preGame.createNewGame(preGame.getPlayerByName(session.getPlayerName()).getPlayerId());
                    for (Player p : players) {
                        preGame.invitePlayerToGame(p.getPlayerId(),gameId);
                    }

                } catch (NamingException e) {
                    e.printStackTrace();
                } catch (NoPlayerFoundException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        };
        form.add(button);
        form.add(group);
        add(form);
        add(test);

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
