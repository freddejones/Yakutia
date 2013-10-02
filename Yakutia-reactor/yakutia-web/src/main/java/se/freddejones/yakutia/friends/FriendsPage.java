package se.freddejones.yakutia.friends;



import se.freddejones.yakutia.application.service.exceptions.NoPlayerFoundException;
import se.freddejones.yakutia.application.service.iface.FriendManagerInterface;
import se.freddejones.yakutia.application.service.iface.PreGameInterface;
import se.freddejones.yakutia.base.ErrorPage;
import se.freddejones.yakutia.base.NavbarPage;
import se.freddejones.yakutia.entity.Player;
import se.freddejones.yakutia.session.MySession;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.ejb.EJB;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;

@AuthorizeInstantiation("USER")
public class FriendsPage extends NavbarPage {

    @EJB(name = "friendManagerBean")
    private FriendManagerInterface friendManager;
    @EJB(name = "preGameBean")
    private PreGameInterface preGame;

    private Player p = null;

    public FriendsPage(PageParameters parameters) throws NamingException {
        super(parameters);

        MySession session = (MySession) getSession();

        try {
            p = preGame.getPlayerById(session.getPlayerId());
        } catch (NoPlayerFoundException e) {
            PageParameters pgp = new PageParameters();
            pgp.add("message","could not determine your player id");
            setResponsePage(ErrorPage.class, pgp);
        }

        final Form superFriendForm = new Form("super-friend-form");
        final Form containerForm = new Form("container-form");
        final WebMarkupContainer listPlayersContainer = new WebMarkupContainer("all-players");
        final WebMarkupContainer invitesContainer = new WebMarkupContainer("invites-container");
        listPlayersContainer.setOutputMarkupId(true);
        invitesContainer.setOutputMarkupId(true);

        final ListView<Player> friendsListView = friendsListView(p);
        final ListView<Player> allPlayersListView = allPlayersListView(p, containerForm, listPlayersContainer);
        final ListView<Player> invitesListView = allInvitesListView(p, containerForm, invitesContainer);

        final WebMarkupContainer friendsContainer = new WebMarkupContainer("friends-container");
        friendsContainer.add(friendsListView);
        friendsContainer.setVisible(false);
        containerForm.add(friendsContainer);

        listPlayersContainer.add(allPlayersListView);
        listPlayersContainer.setVisible(false);
        containerForm.add(listPlayersContainer);

        invitesContainer.add(invitesListView);
        invitesContainer.setVisible(false);
        containerForm.add(invitesContainer);

        final List<WebMarkupContainer> containers = new ArrayList<WebMarkupContainer>();
        containers.add(friendsContainer);
        containers.add(invitesContainer);
        containers.add(listPlayersContainer);

        final List<Button> buttons = new ArrayList<Button>();

        Button invitesButton;
        invitesButton = new Button("list-all-invites-button") {
            @Override
            public void onSubmit() {
                invitesContainer.removeAll();
                invitesContainer.add(allInvitesListView(p, containerForm, invitesContainer));
                if (invitesContainer.isVisible()) {
                    invitesContainer.setVisible(false);
                    this.add(new AttributeModifier("class", new Model("btn")));
                } else {
                    unmarkButtons(buttons);
                    hideContainers(containers);
                    invitesContainer.setVisible(true);
                    this.add(new AttributeModifier("class", new Model("btn btn-primary")));
                }
            }
        };
        invitesButton.setOutputMarkupId(true);
        superFriendForm.add(invitesButton);

        Button listAllPlayerButton = new Button("list-all-button"){

            @Override
            public void onSubmit() {
                listPlayersContainer.removeAll();
                listPlayersContainer.add(allPlayersListView(p, containerForm, listPlayersContainer));
                if (listPlayersContainer.isVisible()) {
                    listPlayersContainer.setVisible(false);
                    this.removeAll();
                    this.add(new AttributeModifier("class", new Model("btn")));
                } else {
                    unmarkButtons(buttons);
                    hideContainers(containers);
                    listPlayersContainer.setVisible(true);
                    this.removeAll();
                    this.add(new AttributeModifier("class", new Model("btn btn-primary")));
                }
            }
        };
        listAllPlayerButton.setOutputMarkupId(true);

        superFriendForm.add(listAllPlayerButton);


        Button listAllFriendsButton = new Button("list-all-friends-button") {

            @Override
            public void onSubmit() {
                friendsContainer.removeAll();
                friendsContainer.add(friendsListView(p));
                if (friendsContainer.isVisible()) {
                    friendsContainer.setVisible(false);
                    this.removeAll();
                    this.add(new AttributeModifier("class", new Model("btn")));
                } else {
                    unmarkButtons(buttons);
                    hideContainers(containers);
                    friendsContainer.setVisible(true);
                    this.removeAll();
                    this.add(new AttributeModifier("class", new Model("btn btn-primary")));
                }
            }
        };
        listAllFriendsButton.setOutputMarkupId(true);
        superFriendForm.add(listAllFriendsButton);

        add(superFriendForm);
        add(containerForm);

        // Add buttons to list
        buttons.add(listAllPlayerButton);
        buttons.add(listAllFriendsButton);
        buttons.add(invitesButton);
    }

    private ListView<Player> friendsListView(Player p) {
        List<Player> friendsToPlayer = (List) new ArrayList<Player>(friendManager.getFriends(p));
        return new ListView<Player>("friends-listview", friendsToPlayer)
        {
            public void populateItem(final ListItem<Player> item)
            {
                Player p = item.getModelObject();
                item.add(new Label("friend-playerName",p.getName()));
            }
        };
    }

    private ListView<Player> allPlayersListView(final Player p, final Form form, final WebMarkupContainer wmc) {
        final List<Player> nonFriendPlayers = (List) new ArrayList<Player>(friendManager.getAllNonFriendPlayers(p));
        return new ListView<Player>("all-players-listview", nonFriendPlayers)
        {
            final ListView lv = this;
            public void populateItem(final ListItem<Player> item)
            {
                Player nonFriendPlayer = item.getModelObject();
                item.add(new Label("playername",nonFriendPlayer.getName()));

                AjaxFallbackButton button = new AjaxFallbackButton("invite-button", form) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        Player playerToBeInvited = item.getModelObject();
                        nonFriendPlayers.remove(playerToBeInvited);
                        friendManager.sendInvite(p, playerToBeInvited);
                        lv.removeAll();

                        if (target != null) {
                            target.add(wmc);
                        }
                    }
                };

                item.add(button);
            }
        }.setReuseItems(true);
    }

    private ListView<Player> allInvitesListView(final Player player, final Form form, final WebMarkupContainer wmc) {
        final List<Player> invitesFromPlayers = (List) new ArrayList<Player>(friendManager.getAllInvites(player));
        return new ListView<Player>("invites-listview", invitesFromPlayers)
        {
            final ListView lv = this;
            public void populateItem(final ListItem<Player> item)
            {
                Player playerThatInvites = item.getModelObject();
                item.add(new Label("invited-playerName",playerThatInvites.getName()));

                AjaxFallbackButton acceptButton = new AjaxFallbackButton("accept-invite", form) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        Player playerToBeAccepted = item.getModelObject();
                        invitesFromPlayers.remove(playerToBeAccepted);
                        friendManager.acceptInvite(player, playerToBeAccepted);
                        lv.removeAll();

                        if (target != null) {
                            target.add(wmc);
                        }
                    }
                };
                item.add(acceptButton);

                AjaxFallbackButton declineButton = new AjaxFallbackButton("decline-invite", form) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        Player playerToBeDeclined = item.getModelObject();
                        invitesFromPlayers.remove(playerToBeDeclined);
                        friendManager.declineInvite(player, playerToBeDeclined);
                        lv.removeAll();

                        if (target != null) {
                            target.add(wmc);
                        }
                    }
                };
                item.add(declineButton);
            }
        }.setReuseItems(true);
    }

    private void unmarkButtons(List<Button> buttons) {
        for (Button button : buttons) {
            button.removeAll();
            button.add(new AttributeModifier("class", new Model("btn")));
        }
    }

    private void hideContainers(List<WebMarkupContainer> containers) {
        for (WebMarkupContainer container : containers) {
            container.setVisible(false);
        }
    }
}
