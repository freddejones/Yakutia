package nu.danielsundberg.yakutia.games;



import nu.danielsundberg.yakutia.*;
//import nu.danielsundberg.yakutia.entity.Game;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.ejb.EJB;
import javax.naming.NamingException;

@AuthorizeInstantiation("USER")
public class GamesPage extends NavbarPage {

    @EJB(name = "pregamebean")
    private PreGameInterface pgi;



    public GamesPage(PageParameters parameters) throws NamingException {
        super(parameters);

        Form form = new Form("CreateGame");
        Button button = new Button("createGameButton") {

            @Override
            public void onSubmit() {
                setResponsePage(CreateGamePage.class);
            }
        };
        form.add(button);
        add(form);



        Label l = new Label("msg","No games for you");
        add(l);

        Label l2 = new Label("msg2", "games found");
        add(l2);

//        if (ctx == null)
//            setInitialContext(new InitialContext());

//        PreGameInterface test = (PreGameInterface) ctx.lookup("preGameBean");
//        GameEngineInterface engine = (GameEngineInterface) ctx.lookup("kickass");

//        List<PlayerApi> players = test.getPlayers();
//
        if (pgi.getPlayers().size() > 0) {
            l.setVisible(false);
        } else {
            l2.setVisible(false);
        }
//        if (players.size() == 0) {
//            l2.setVisible(false);
//        }
//        add(new ListView<PlayerApi>("rows", players)
//        {
//            public void populateItem(final ListItem<PlayerApi> item)
//            {
//                final PlayerApi player = item.getModelObject();
//                item.add(new Label("name", player.getPlayerName()));
//                item.add(new Label("id", player.getPlayerId().toString()));
//            }
//        });
        // TODO Show invites

        // TODO Show current games?
    }

//    protected void setInitialContext(InitialContext ctx) {
//        this.ctx = ctx;
//    }
}
