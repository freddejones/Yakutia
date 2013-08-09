package nu.danielsundberg.yakutia.games;

import nu.danielsundberg.yakutia.application.service.iface.PlayerActionsInterface;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.base.ErrorPage;
import nu.danielsundberg.yakutia.base.NavbarPage;
import nu.danielsundberg.yakutia.entity.Game;
import nu.danielsundberg.yakutia.entity.GamePlayer;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.session.MySession;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.head.OnLoadHeaderItem;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ClientSideImageMap;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;

import javax.ejb.EJB;
import javax.naming.NamingException;
import java.util.Iterator;
import java.util.Set;

/**
 * User: Fredde
 * Date: 5/14/13 11:41 PM
 */
@AuthorizeInstantiation("USER")
public class ActiveGamePage extends NavbarPage {

    @EJB(name = "pregamebean")
    private PreGameInterface preGameInterface;
    @EJB(name = "playerActionsBean")
    private PlayerActionsInterface playeractionInterface;

    public ActiveGamePage(PageParameters parameters) throws NamingException {
        super(parameters);

        WebMarkupContainer wmcActionForm = new WebMarkupContainer("wmc-actionform");
        add(wmcActionForm);

        String gameId = String.valueOf(parameters.get("gameId"));
        Long gameIdLong = Long.parseLong(gameId);

        MySession session = (MySession) getSession();
        Game g = preGameInterface.getGameById(gameIdLong);
        Set<GamePlayer> playersInCurrentGame = g.getPlayers();
        Iterator<GamePlayer> gpIterator = playersInCurrentGame.iterator();
        boolean playerPartOfGame = false;
        while (gpIterator.hasNext()) {
            GamePlayer gp = gpIterator.next();
            if (gp.getPlayerId() == session.getPlayerId()) {
                playerPartOfGame = true;
            }
        }

        if (!playerPartOfGame) {
            PageParameters errorPageParams = new PageParameters();
            errorPageParams.add("message", "Not able to see game since you are not part of it");
            setResponsePage(ErrorPage.class,errorPageParams);
        }

        Label message = new Label("message","Current game id: " + gameId);
        message.setOutputMarkupId(true);
        add(message);

        Form actionForm = new Form("form-action");
        WebMarkupContainer wmcAction = new WebMarkupContainer("wmc-action");
        Button actionAttackButton = new Button("action-attack");
        wmcAction.add(actionAttackButton);
        actionForm.add(wmcAction);
        wmcActionForm.add(actionForm);

        Long playerId = ((MySession) getSession()).getPlayerId();
        if (!playeractionInterface.isPlayerTurn(playerId ,gameIdLong)) {
            wmcActionForm.setVisible(false);
            message.replaceWith(new Label("message","Not your turn"));
        }


        AjaxEventBehavior event = new AjaxEventBehavior("onload") {
            @Override
            protected void onEvent(final AjaxRequestTarget target) {
                target.appendJavaScript("$('#gamemap1')\n" +
                        ".mapster({\n" +
                        "mapKey: 'data-key',\n" +
                        "fillColor: '1BE01E',\n" +
                        "fillOpacity: 0.25\n" +
                        "})\n" +
                        ".mapster('set',true,'norge');");
            }
        };
        add(event);
    }
}
