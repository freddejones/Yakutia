package nu.danielsundberg.yakutia.games;

import nu.danielsundberg.yakutia.application.service.exceptions.NoPlayerFoundException;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.base.NavbarPage;
import nu.danielsundberg.yakutia.entity.Game;
import nu.danielsundberg.yakutia.entity.GamePlayer;
import nu.danielsundberg.yakutia.entity.GamePlayerStatus;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.session.MySession;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import javax.ejb.EJB;
import javax.naming.NamingException;
import java.util.*;

@AuthorizeInstantiation("USER")
public class GamesPage extends NavbarPage {

    @EJB(name = "pregamebean")
    private PreGameInterface preGameInterface;

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

        Label infoMessage = new Label("msg","No games for you");
        add(infoMessage);

        List<GamePlayer> gamePlayers = new ArrayList<GamePlayer>();
        ListView<GamePlayer> gameLV = getListViewOfGames(gamePlayers);
        add(gameLV);


        MySession session = (MySession) getSession();
        try {
            Player player = preGameInterface.getPlayerByName(session.getPlayerName());

            if (!player.getGames().isEmpty()) {
                infoMessage.setVisible(false);
                Set<GamePlayer> g = player.getGames();
                Iterator<GamePlayer> gamePlayerIterator = g.iterator();
                List<GamePlayer> games2 = new ArrayList<GamePlayer>();
                while (gamePlayerIterator.hasNext()) {
                    games2.add(gamePlayerIterator.next());
                }
                gameLV.replaceWith(getListViewOfGames(games2));
            }
        } catch (NoPlayerFoundException e) {
            infoMessage.replaceWith(new Label("msg", "You seem to not exist as a player, wich is weird"));
        }

        // TODO Show invites
    }

    protected ListView<GamePlayer> getListViewOfGames(List<GamePlayer> games) {

        return new ListView<GamePlayer>("rows", games)
        {
            public void populateItem(final ListItem<GamePlayer> item)
            {
                final GamePlayer gameplayer = item.getModelObject();
                item.add(new Label("gameid", gameplayer.getGameId()));
                item.add(new Label("status", gameplayer.getGame().getGameStatus()));


                long gameId = gameplayer.getGameId();
                Game g = preGameInterface.getGameById(gameId);
                int tot = g.getPlayers().size();
                Iterator<GamePlayer> iter = g.getPlayers().iterator();
                int accepted = 0;
                while(iter.hasNext()) {
                    GamePlayer gp = iter.next();
                    if (gp.getGamePlayerStatus().equals(GamePlayerStatus.ACCEPTED)) {
                        accepted++;
                    }
                }

                item.add(new Label("accepts", accepted+"/"+tot));
                Form form = new Form("form");
                Button button = new Button("button") {

                    @Override
                    public void onSubmit() {
                        String gameIdParamValue = this.getParent().
                                getParent().
                                get("gameid").getDefaultModelObjectAsString();
                        PageParameters params = new PageParameters();
                        params.add("gameId", gameIdParamValue);
                        setResponsePage(ActiveGamePage.class,params);
                    }
                };
                form.add(button);
                item.add(form);
            }
        };

    }

}
