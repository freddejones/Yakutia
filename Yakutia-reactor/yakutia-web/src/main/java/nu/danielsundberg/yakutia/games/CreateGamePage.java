package nu.danielsundberg.yakutia.games;



import nu.danielsundberg.yakutia.base.NavbarPage;
import nu.danielsundberg.yakutia.application.service.exceptions.NoPlayerFoundException;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.entity.PlayerFriend;
import nu.danielsundberg.yakutia.session.MySession;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.ejb.EJB;
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

    // TODO refactor the code to nice
    public CreateGamePage(PageParameters parameters) throws NamingException {
        super(parameters);

        MySession session = (MySession) getSession();
        final long playerId = session.getPlayerId();

        final List<Player> addedPlayers = new ArrayList<Player>();
        final List<Player> friends = new ArrayList<Player>();

        // Gather friends
        final List<Player> friendsTmp = new ArrayList<Player>();
        try {
             Player p = preGameInterface.getPlayerById(session.getPlayerId());
             for (PlayerFriend playerFriend : p.getFriends()) {
                 Player tmpPlayer = playerFriend.getFriend();
                 friendsTmp.add(tmpPlayer);
             }
        } catch (NoPlayerFoundException e) {
            e.printStackTrace();
        }

        final List<Player> players = new ArrayList<Player>();
        List<Player> listTmp = preGameInterface.getPlayers();
        for (Player p : listTmp) {

            if (isMatchedPlayerFriend(p.getPlayerId(), friendsTmp)) {
                friends.add(p);
            } else if (!p.getName().equals(session.getPlayerName())) {
                players.add(p);
            } else {
                addedPlayers.add(p);
            }
        }

        final WebMarkupContainer wmcMessage = new WebMarkupContainer("wmc-message");
        wmcMessage.setOutputMarkupId(true);
        add(wmcMessage);

        final TextField gameNameTextField = new TextField("gamename", new PropertyModel(this,"gameName"));
        gameNameTextField.setOutputMarkupId(true);
        gameNameTextField.add(new AttributeModifier("value", new Model("Enter game name")));
        setGameName("Enter game name");

        final Form form = new Form("form1");
        form.add(gameNameTextField);

        final WebMarkupContainer wmcTheGame = new WebMarkupContainer("wmc");
        wmcTheGame.setOutputMarkupId(true);
        form.add(wmcTheGame);

        final WebMarkupContainer wmcFriends = new WebMarkupContainer("wmc-friends");
        wmcFriends.setOutputMarkupId(true);
        form.add(wmcFriends);

        final WebMarkupContainer wmc = new WebMarkupContainer("webmarkupcontainer");
        wmc.setOutputMarkupId(true);
        form.add(wmc);

        final Label messageLabel = new Label("message","empty message");
        messageLabel.setOutputMarkupId(true);
        messageLabel.setVisible(false);
        wmcMessage.add(messageLabel);

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

                        if (selected.getPlayerId() == playerId) {
                            return;
                        }

                        addedPlayers.remove(selected);

                        lv.removeAll();
                        if (isMatchedPlayerFriend(selected.getPlayerId(), friendsTmp)) {
                            friends.add(selected);
                            if (target != null) {
                                target.add(wmcTheGame);
                                target.add(wmcFriends);
                            }
                        } else {
                            players.add(selected);
                            if (target != null) {
                                target.add(wmcTheGame);
                                target.add(wmc);
                            }
                        }



                    }
                };

                item.add(button);
            }
        };
        addedPlayersListview.setReuseItems(true);
        addedPlayersListview.setOutputMarkupId(true);
        wmcTheGame.add(addedPlayersListview);


        final ListView<Player> friendsListView = new ListView<Player>("friends", friends)
        {
            final ListView lv = this;
            public void populateItem(final ListItem<Player> item)
            {
                final Player player = item.getModelObject();
                final Label playerName = new Label("friendname", player.getName());
                playerName.setOutputMarkupId(true);
                item.add(playerName);

                AjaxFallbackButton button = new AjaxFallbackButton("addFriendPlayer", form) {

                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                        wmcMessage.replace(messageLabel);

                        Player selected = item.getModelObject();
                        friends.remove(selected);
                        addedPlayers.add(selected);
                        lv.removeAll();

                        if (target != null) {
                            target.add(wmcMessage);
                            target.add(wmcTheGame);
                            target.add(wmcFriends);
                        }
                    }
                };

                item.add(button);
            }
        };
        friendsListView.setReuseItems(true);
        friendsListView.setOutputMarkupId(true);
        wmcFriends.add(friendsListView);

        /* Handling non friend players */
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


                        wmcMessage.replace(messageLabel);

                        Player selected = item.getModelObject();
                        players.remove(selected);
                        addedPlayers.add(selected);
                        lv.removeAll();

                        if (target != null) {
                            target.add(wmcMessage);
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


                if (getErrorMessages(players, getGameName()).size() > 0) {
                    wmcMessage.removeAll();
                    String errorMessage = "";
                    for (String error : getErrorMessages(players, getGameName())) {
                        errorMessage += error + "\n";
                    }
                    wmcMessage.add(new MultiLineLabel("message",errorMessage));
                    return;
                }

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

    private List<String> getErrorMessages(List<Player> players, String gameName) {
        List<String> errors = new ArrayList<String>();

        if (players.size() < 2) {
            errors.add("you need to be at least 2 players to create a game");
        }

        if ("Enter game name".equals(getGameName())
                        || "".equals(getGameName())
                        || getGameName() == null) {
            errors.add("you need to specify a game name");
        }
        return errors;
    }

    private boolean isMatchedPlayerFriend(long playerId, List<Player> friends) {

        for (Player p : friends) {

            if (p.getPlayerId() == playerId)
                return true;
        }

        return false;
    }
}
