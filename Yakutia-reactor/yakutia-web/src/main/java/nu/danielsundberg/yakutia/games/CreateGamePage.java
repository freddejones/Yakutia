package nu.danielsundberg.yakutia.games;



import nu.danielsundberg.yakutia.base.NavbarPage;
import nu.danielsundberg.yakutia.application.service.exceptions.NoPlayerFoundException;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.session.MySession;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.CollectionModel;
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

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    private String gameName;

    // TODO write unit test

    // TODO refactor the code to nice

    public CreateGamePage(PageParameters parameters) throws NamingException {
        super(parameters);

        final List<Player> addedPlayers = new ArrayList<Player>();

        // TODO CHange this to use friends here instead
        final List<Player> players = new ArrayList<Player>();
        List<Player> listTmp = preGameInterface.getPlayers();
        for (Player p : listTmp) {
            MySession session = (MySession) getSession();
            if (!p.getName().equals(session.getPlayerName())) {
                players.add(p);
            } else {
                addedPlayers.add(p);
            }
        }


        TextField gameNameTextField = new TextField("gamename", new PropertyModel(this,"gameName"));
        gameNameTextField.add(new AttributeAppender("value", new Model("Enter game name")));

        final Form form = new Form("form1");
        form.add(gameNameTextField);

        final WebMarkupContainer wmcTheGame = new WebMarkupContainer("wmc");
        wmcTheGame.setOutputMarkupId(true);
        form.add(wmcTheGame);

        final WebMarkupContainer wmc = new WebMarkupContainer("webmarkupcontainer");
        wmc.setOutputMarkupId(true);
        form.add(wmc);


        final ListView<Player> addedPlayersListview = new ListView<Player>("playersAdded", addedPlayers)
        {
            final ListView lv = this;
            public void populateItem(final ListItem<Player> item)
            {
                final Player player = item.getModelObject();
                final Label playerName = new Label("playernameAdded", player.getName());
                playerName.setOutputMarkupId(true);
                item.add(playerName);

                AjaxFallbackButton button = new AjaxFallbackButton("delPlayer", form) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        Player selected = item.getModelObject();
                        addedPlayers.remove(selected);
                        players.add(selected);
                        lv.removeAll();

                        if (target != null) {
                            target.add(wmc);
                            target.add(wmcTheGame);
                        }
                    }
                };

                item.add(button);
            }
        };

        addedPlayersListview.setReuseItems(true);
        addedPlayersListview.setOutputMarkupId(true);
        wmcTheGame.add(addedPlayersListview);


        final ListView<Player> listview = new ListView<Player>("players", players)
        {
            final ListView lv = this;
            public void populateItem(final ListItem<Player> item)
            {
                final Player player = item.getModelObject();
                final Label playerName = new Label("playername", player.getName());
                playerName.setOutputMarkupId(true);
                item.add(playerName);

                AjaxFallbackButton button = new AjaxFallbackButton("addPlayer", form) {

                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                        Player selected = item.getModelObject();
                        players.remove(selected);
                        addedPlayers.add(selected);
                        lv.removeAll();

                        if (target != null) {
                            target.add(wmcTheGame);
                            target.add(wmc);
                        }
                    }
                };

                item.add(button);
            }
        };


        listview.setReuseItems(true);
        listview.setOutputMarkupId(true);
        wmc.add(listview);



        Button button = new Button("submit1")
        {
            @Override
            public void onSubmit() {
                List<Player> players = (ArrayList<Player>)addedPlayersListview.getDefaultModelObject();
                MySession session = (MySession) getSession();
                long gameId = preGameInterface.createNewGame(session.getPlayerId(),getGameName());
                for (Player p : players) {
                    if (p.getPlayerId() != session.getPlayerId()) {
                        preGameInterface.invitePlayerToGame(p.getPlayerId(),gameId);
                    }
                }
                setResponsePage(GamesPage.class);
            }
        };
        form.add(button);
        add(form);

    }
}
