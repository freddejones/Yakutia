package se.freddejones.yakutia.games;

import de.agilecoders.wicket.core.markup.html.bootstrap.components.*;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.ClientSideImageMap;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;
import se.freddejones.yakutia.application.service.iface.PlayerActionsInterface;
import se.freddejones.yakutia.application.service.iface.PreGameInterface;
import se.freddejones.yakutia.base.ErrorPage;
import se.freddejones.yakutia.base.NavbarPage;
import se.freddejones.yakutia.entity.Game;
import se.freddejones.yakutia.entity.GamePlayer;
import se.freddejones.yakutia.session.MySession;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;

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

        String gameIdParam = String.valueOf(parameters.get("gameId"));
        Long gameId = Long.parseLong(gameIdParam);

        MySession session = (MySession) getSession();
        Long currentPlayerId = session.getPlayerId();
        Game g = preGameInterface.getGameById(gameId);
        Set<GamePlayer> playersInCurrentGame = g.getPlayers();
        Iterator<GamePlayer> gpIterator = playersInCurrentGame.iterator();
        boolean playerPartOfGame = false;
        while (gpIterator.hasNext()) {
            GamePlayer gp = gpIterator.next();
            if (gp.getPlayerId() == currentPlayerId) {
                playerPartOfGame = true;
            }
        }

        if (!playerPartOfGame) {
            PageParameters errorPageParams = new PageParameters();
            errorPageParams.add("message", "You are not able to see game since you are not part of it");
            setResponsePage(ErrorPage.class,errorPageParams);
        }

        Label message = new Label("message","Current game id: " + gameIdParam);
        message.setOutputMarkupId(true);
        add(message);


//        add(new Label("tooltip-top", Model.of("any"))
//                .add(new PopoverBehavior(Model.of("TEEESY"), Model.of("Supercool content"))));

        add(new Label("tooltip-top", "Popover (hover, top)").add(new PopoverBehavior(
                Model.of("title"),
                Model.of("content"),
                new PopoverConfig().withHoverTrigger().withPlacement(TooltipConfig.Placement.top)
        )));

//        add(new Label("richpopover", "Popover (hover, top)").add(new RichPopoverBehavior(
//                Model.of("title"),
//                new PopoverConfig().withHoverTrigger().withPlacement(TooltipConfig.Placement.top)
//        ) {
//
//            @Override
//            public Component newBodyComponent(String markupId) {
//                Label label = new Label(markupId, Model.of("<h2>rich content</h2><a href=\"http://wb.agilecoders.de\">Link</a>"));
//                label.setEscapeModelStrings(false);
//
//                return label;
//            }
//        }));


        TooltipConfig config = new TooltipConfig();
        config.withContent("fucker");
        config.withPlacement(TooltipConfig.Placement.right);
        config.withHtml(true);

        Form actionForm = new Form("form-action");
        WebMarkupContainer wmcAction = new WebMarkupContainer("wmc-action");
        Button actionAttackButton = new Button("action-attack");

        actionAttackButton.add(new TooltipBehavior(Model.of("Whaaat up"), config));
        wmcAction.add(actionAttackButton);
        actionForm.add(wmcAction);
        wmcActionForm.add(actionForm);


        if (!playeractionInterface.isPlayerTurn(currentPlayerId ,gameId)) {
            wmcActionForm.setVisible(false);
            message.replaceWith(new Label("message","Not your turn"));
        } else {
            message.replaceWith(new Label("message","Actually your turn"));
        }


//        Image imageForMap = new Image("imageForMap", new PackageResourceReference(ActiveGamePage.class,
//                "yakutiav2-1.png"));
//        add(imageForMap);

//        PopoverBehavior pb = new PopoverBehavior(
//                Model.of("title"),
//                Model.of("content"),
//                new PopoverConfig().withHoverTrigger().withPlacement(TooltipConfig.Placement.top)
//        );


//        add(new ClientSideImageMap("imageMap", imageForMap)
//                .addPolygonArea(new BookmarkablePageLink<ActiveGamePage>("ActiveGamePage", ActiveGamePage.class), 209, 130, 196, 131, 158, 158, 144, 175, 142, 225, 157, 254, 220, 263, 257, 266, 289, 237, 291, 219, 279, 208, 259, 195, 246, 191, 246, 180, 263, 169, 273, 151, 272, 133, 266, 112, 245, 109, 242, 113, 229, 115, 220, 119, 217, 129, 214, 129)
//                .add(new AttributeAppender("class","sweden"))
//                .add(new AttributeModifier("href","#")));

//        add(new ClientSideImageMap("imageMap", imageForMap).addPolygonArea(
//                new BookmarkablePageLink<ActiveGamePage>("ActiveGamePage", ActiveGamePage.class), 95,52,85,62,75,75,63,100,64,118,79,127,98,134,99,153,92,167,92,187,121,204,143,205,145,177,152,164,177,143,191,137,213,129,218,127,227,112,227,99,214,84,192,78,161,78,155,81,135,80,121,56,99,51)
////                .add(pb)
//                .add(new AttributeAppender("class","norway"))
//                .add(new AttributeModifier("href","#"))
//        );

//        AjaxEventBehavior event = new AjaxEventBehavior("onload") {
//            @Override
//            protected void onEvent(AjaxRequestTarget ajaxRequestTarget) {
//                ajaxRequestTarget.appendJavaScript("$('.yakimg').click(function (e) {\n" +
//                        "    var offset = $(this).offset();\n" +
//                        "    var left = e.pageX;\n" +
//                        "    var top = e.pageY;\n" +
//                        "    var theHeight = $('.luls').height();\n" +
//                        "    $('.luls').show();\n" +
//                        "    $('.luls').css('left', (left+10) + 'px');\n" +
//                        "    $('.luls').css('top', (top-(theHeight/2)-10) + 'px');\n" +
//                        "});");
//            }
//        };

        WebMarkupContainer wmcFinland = new WebMarkupContainer("finland");
        wmcFinland.setOutputMarkupId(true);
        wmcFinland.add(new AttributeAppender("data-maphilight","{\"alwaysOn\":true,\"stroke\":false,\"fillColor\":\"FC0303\"}"));
        add(wmcFinland);

        AjaxEventBehavior event = new AjaxEventBehavior("onload") {
            @Override
            protected void onEvent(AjaxRequestTarget ajaxRequestTarget) {
                ajaxRequestTarget.appendJavaScript("$('.SWE,.NOR').css({\n" +
                        "    position: 'absolute'\n" +
                        "}).hide()\n" +
                        "$('area').each(function(i) {\n" +
                        "    $('.sweden').eq(i).bind('mouseover', function(e) {\n" +
                        "        $('.SWE').eq(i).css({\n" +
                        "            top: e.pageY+10,\n" +
                        "            left: e.pageX+10\n" +
                        "        }).show()\n" +
                        "    })\n" +
                        "    $('.sweden').eq(i).bind('mousemove', function(e) {\n" +
                        "        $('.SWE').eq(i).css({\n" +
                        "            top: e.pageY+10,\n" +
                        "            left: e.pageX+10\n" +
                        "        }).show()\n" +
                        "    })\n" +
                        "    $('.norway').eq(i).bind('mouseover', function(e) {\n" +
                        "        $('.NOR').eq(i).css({\n" +
                        "            top: e.pageY+10,\n" +
                        "            left: e.pageX+10\n" +
                        "        }).show()\n" +
                        "    })\n" +
                        "    $('.norway').eq(i).bind('mousemove', function(e) {\n" +
                        "        $('.NOR').eq(i).css({\n" +
                        "            top: e.pageY+10,\n" +
                        "            left: e.pageX+10\n" +
                        "        }).show()\n" +
                        "    })\n" +
                        "    $('area').eq(i).bind('mouseout', function() {\n" +
                        "        $('.SWE,.NOR').hide()\n" +
                        "    })\n" +
                        "})");

                ajaxRequestTarget.appendJavaScript("$(function() {\n" +
                        "$('.yakimg').maphilight({fade: false, fillColor:'87FF47', stroke: false});\n" +
                        "});");
            }
        };
        add(event);
//        AjaxEventBehavior event = new AjaxEventBehavior("onload") {
//            @Override
//            protected void onEvent(final AjaxRequestTarget target) {
//                target.appendJavaScript("$('#gamemap1')\n" +
//                        ".mapster({\n" +
//                        "mapKey: 'data-key',\n" +
//                        "fillColor: '1BE01E',\n" +
//                        "fillOpacity: 0.25\n" +
//                        "})\n" +
//                        ".mapster('set',true,'norge');");
//            }
//        };
//        add(event);
    }
}
