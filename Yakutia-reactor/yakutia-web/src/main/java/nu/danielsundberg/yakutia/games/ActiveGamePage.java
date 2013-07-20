package nu.danielsundberg.yakutia.games;

import nu.danielsundberg.yakutia.base.NavbarPage;
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
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ClientSideImageMap;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.PackageResourceReference;

import javax.naming.NamingException;

/**
 * User: Fredde
 * Date: 5/14/13 11:41 PM
 */
@AuthorizeInstantiation("USER")
public class ActiveGamePage extends NavbarPage {

//    @Override
//    public void renderHead(IHeaderResponse response) {
//        super.renderHead(response);
//        response.render(OnLoadHeaderItem.forScript("function version(){alert('test');}"));
////        response.render(JavaScriptHeaderItem.forScript("function version(){alert('test');}",null));
//    }

    public ActiveGamePage(PageParameters parameters) throws NamingException {
        super(parameters);

        String gameId = String.valueOf(parameters.get("gameId"));

        Label gameMsgId = new Label("gameIdMsg","Game Id: " + gameId);
        add(gameMsgId);


        AjaxEventBehavior event = new AjaxEventBehavior("onload") {
            @Override
            protected void onEvent(final AjaxRequestTarget target) {
                // do stuff here
//                target.appendJavaScript("$('#gamemap1').mapster({\n" +
//                        "fillColor: '1BE01E',\n" +
//                        "fillOpacity: 0.25\n" +
//                        "});");

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


//        final WebMarkupContainer container1 = new WebMarkupContainer("container1");
//        container1.setOutputMarkupId(true);
//
//
//        final WebMarkupContainer gameContainer = new WebMarkupContainer("map-container");
//        gameContainer.setOutputMarkupId(true);
//
//        final Image baseImage = new Image("imageForMap", "imgname");
//        final Image img2 = new Image("img2", "img2");
//
//        img2.setVisible(false);
//        img2.setOutputMarkupId(true);
//
//        gameContainer.add(img2);
//        gameContainer.add(baseImage);
//
//        ClientSideImageMap imgMap = new ClientSideImageMap("imageMap", baseImage);
//        imgMap.setOutputMarkupId(true);
//        AjaxFallbackLink linkBase = new AjaxFallbackLink<String>("img2") {
//            @Override
//            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
//                System.out.println("Tested OK");
//                img2.setVisible(true);
//                ajaxRequestTarget.add(img2);
//            }
//        };
//        imgMap.addPolygonArea(linkBase, 260,110,144,175,151,248,265,260,291,220);
//        gameContainer.add(imgMap);
//
//
//
//
//        AjaxFallbackLink link = new AjaxFallbackLink<String>("img2") {
//            @Override
//            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
//                System.out.println("Tested OK");
//                img2.setVisible(false);
//                ajaxRequestTarget.add(img2);
//            }
//        };
//        ClientSideImageMap imgMap2 = new ClientSideImageMap("imageMap2", img2);
//        imgMap2.addPolygonArea(link, 260,110,144,175,151,248,265,260,291,220);
//        gameContainer.add(imgMap2);
//        add(gameContainer);
//
//
//        final Image img1 = new Image("img1", "img1");
//        ClientSideImageMap imgMap1 = new ClientSideImageMap("imageMap1", img1);
//
//        AjaxFallbackLink link1 = new AjaxFallbackLink<String>("container1") {
//            @Override
//            public void onClick(AjaxRequestTarget ajaxRequestTarget) {
//                System.out.println("Tested OK");
//                container1.setVisible(false);
//                ajaxRequestTarget.add(container1);
//            }
//        };
//
//        imgMap1.addPolygonArea(link1, 103,53,50,122,64,179,163,151,218,88,198,66,144,74);
//        container1.add(imgMap1);
//        container1.add(img1);
//        img1.setVisible(true);
//        add(container1);

//        WebMarkupContainer css = new WebMarkupContainer( "mycss" );
//        add( css );
    }
}
