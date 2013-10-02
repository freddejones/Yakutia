package se.freddejones.yakutia.games;

import se.freddejones.yakutia.application.service.exceptions.NoPlayerFoundException;
import se.freddejones.yakutia.application.service.exceptions.NotEnoughPlayers;
import se.freddejones.yakutia.application.service.iface.GameEngineInterface;
import se.freddejones.yakutia.base.ErrorPage;
import se.freddejones.yakutia.base.NavbarPage;
import se.freddejones.yakutia.application.service.iface.PreGameInterface;
import se.freddejones.yakutia.entity.Game;
import se.freddejones.yakutia.entity.GamePlayer;
import se.freddejones.yakutia.entity.statuses.GamePlayerStatus;
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
import java.util.*;

@AuthorizeInstantiation("USER")
public class GamesPage extends NavbarPage {

    @EJB(name = "pregamebean")
    private PreGameInterface preGameInterface;

    @EJB(name = "kickass")
    private GameEngineInterface gameEngineInterface;

    public GamesPage(PageParameters parameters) throws NamingException {
        super(parameters);

        List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>();
        List<GamePlayer> invitedGamePlayers = new ArrayList<GamePlayer>();


        final Form form = new Form("CreateGame");
        Button button = new Button("createGameButton") {

            @Override
            public void onSubmit() {
                setResponsePage(CreateGamePage.class);
            }
        };
        form.add(button);

        Label infoMessage = new Label("msg","No games for you");
        form.add(infoMessage);


        MySession session = (MySession) getSession();
        try {
            Player player = preGameInterface.getPlayerById(session.getPlayerId());

            if (!player.getGames().isEmpty()) {
                infoMessage.setVisible(false);
                Set<GamePlayer> g = player.getGames();
                Iterator<GamePlayer> gamePlayerIterator = g.iterator();

                while (gamePlayerIterator.hasNext()) {
                    GamePlayer gp = gamePlayerIterator.next();
                    if (gp.getGamePlayerStatus().equals(GamePlayerStatus.INVITED)) {
                        invitedGamePlayers.add(gp);
                    } else if (!gp.getGamePlayerStatus().equals(GamePlayerStatus.DECLINED)) {
                        gamePlayers.add(gp);
                    }
                }
            }
        } catch (NoPlayerFoundException e) {
            infoMessage.replaceWith(new Label("msg", "You seem to not exist as a player, wich is weird"));
        }

        final WebMarkupContainer wmcCurrentGames = new WebMarkupContainer("wmc-current-games");
        wmcCurrentGames.setOutputMarkupId(true);
        form.add(wmcCurrentGames);

        final WebMarkupContainer wmcInvitedGames = new WebMarkupContainer("wmc-invited-games");
        wmcInvitedGames.setOutputMarkupId(true);
        form.add(wmcInvitedGames);

        final ListView<GamePlayer> invitesLV = getListViewOfInvitedGames(invitedGamePlayers,
                gamePlayers, form, wmcInvitedGames, wmcCurrentGames);
        final ListView<GamePlayer> gameLV = getListViewOfGames(gamePlayers);

        gameLV.setReuseItems(true);
        gameLV.setOutputMarkupId(true);
        wmcCurrentGames.add(gameLV);

        invitesLV.setReuseItems(true);
        invitesLV.setOutputMarkupId(true);
        wmcInvitedGames.add(invitesLV);

        add(form);
    }

    protected ListView<GamePlayer> getListViewOfInvitedGames(final List<GamePlayer> invitedGames,
                                                             final List<GamePlayer> currentGames,
                                                             final Form form,
                                                             final WebMarkupContainer wmcInvites,
                                                             final WebMarkupContainer wmcCurrent) {
        return new ListView<GamePlayer>("inviteRows", invitedGames) {
            final ListView lv = this;

            public void populateItem(final ListItem<GamePlayer> item) {
                final GamePlayer gameplayer = item.getModelObject();

                item.add(new Label("invitedGameId",gameplayer.getGameId()));
                item.add(new Label("invitedGameName",gameplayer.getGame().getName()));
                item.add(new Label("invitedAccepts",
                        getNumberOfAcceptsForGame(gameplayer)+"/"+getTotalNumberOfPlayersForGame(gameplayer)));


                AjaxFallbackButton acceptButton = new AjaxFallbackButton("acceptbutton", form) {

                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                        GamePlayer selected = item.getModelObject();
                        invitedGames.remove(selected);
                        currentGames.add(selected);
                        preGameInterface.acceptInvite(selected.getPlayerId(),selected.getGameId());
                        lv.removeAll();

                        if (target != null) {
                            target.add(wmcInvites);
                            target.add(wmcCurrent);
                        }
                    }
                };

                AjaxFallbackButton declineButton = new AjaxFallbackButton("declinebutton", form) {

                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                        GamePlayer selected = item.getModelObject();
                        invitedGames.remove(selected);
                        currentGames.remove(selected);
                        preGameInterface.declineInvite(selected.getPlayerId(), selected.getGameId());
                        lv.removeAll();

                        if (target != null) {
                            target.add(wmcInvites);
                            target.add(wmcCurrent);
                        }
                    }
                };

                item.add(acceptButton);
                item.add(declineButton);
            }
        };
    }

    protected ListView<GamePlayer> getListViewOfGames(List<GamePlayer> games) {

        return new ListView<GamePlayer>("rows", games)
        {
            public void populateItem(final ListItem<GamePlayer> item)
            {
                final GamePlayer gameplayer = item.getModelObject();
                item.add(new Label("gameid", gameplayer.getGameId()));
                item.add(new Label("status", gameplayer.getGame().getGameStatus()));
                item.add(new Label("gamename", gameplayer.getGame().getName()));

                item.add(new Label("accepts",
                        getNumberOfAcceptsForGame(gameplayer)+"/"+getTotalNumberOfPlayersForGame(gameplayer)));

                // Check if player is the creator if the game
                MySession session = (MySession) getSession();

                Button button;
                if (gameplayer.getGame().getGameCreatorPlayerId() == session.getPlayerId()) {
                    button = new Button("button") {

                        @Override
                        public void onSubmit() {
                            String gameIdParamValue = this.getParent().
                                    get("gameid").getDefaultModelObjectAsString();
                            Long gameId = Long.parseLong(gameIdParamValue);

                            // Check status of game:
                            try {
                                gameEngineInterface.startNewGame(gameId);
                            } catch (NotEnoughPlayers notEnoughPlayers) {
                                setResponsePage(ErrorPage.class,
                                        new PageParameters().add("message",
                                                "Messed number of players.."));
                            }
                            PageParameters params = new PageParameters();
                            params.add("gameId", gameIdParamValue);
                            setResponsePage(ActiveGamePage.class,params);
                        }
                    };
                } else {
                    button = new Button("button") {
                        @Override
                        public void onSubmit() {
                        }
                    };
                    button.add(new AttributeModifier("class", new Model("btn btn-mini btn-warning disabled")));
                    button.add(new AttributeModifier("value", new Model("Not started yet")));
                }
                item.add(button);
            }
        };

    }

    protected int getTotalNumberOfPlayersForGame(GamePlayer gamePlayer) {
        long gameId = gamePlayer.getGameId();
        Game g = preGameInterface.getGameById(gameId);
        int total = 0;
        for (GamePlayer gp : g.getPlayers()) {
            if (!gp.getGamePlayerStatus().equals(GamePlayerStatus.DECLINED)) {
                total++;
            }
        }
        return total;
    }

    protected int getNumberOfAcceptsForGame(GamePlayer gamePlayer) {
        long gameId = gamePlayer.getGameId();
        Game g = preGameInterface.getGameById(gameId);
        Iterator<GamePlayer> iter = g.getPlayers().iterator();
        int accepted = 0;
        while(iter.hasNext()) {
            GamePlayer gp = iter.next();
            if (gp.getGamePlayerStatus().equals(GamePlayerStatus.ACCEPTED)) {
                accepted++;
            }
        }
        return accepted;
    }

}
