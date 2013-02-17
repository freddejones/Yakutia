package nu.danielsundberg.yakutia;

import de.agilecoders.wicket.markup.html.bootstrap.behavior.BootstrapBaseBehavior;
import de.agilecoders.wicket.markup.html.bootstrap.block.Code;
import de.agilecoders.wicket.markup.html.bootstrap.button.dropdown.DropDownButton;
import de.agilecoders.wicket.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.markup.html.bootstrap.extensions.button.DropDownAutoOpen;
import de.agilecoders.wicket.markup.html.bootstrap.html.ChromeFrameMetaTag;
import de.agilecoders.wicket.markup.html.bootstrap.html.HtmlTag;
import de.agilecoders.wicket.markup.html.bootstrap.html.MetaTag;
import de.agilecoders.wicket.markup.html.bootstrap.html.OptimizedMobileViewportMetaTag;
import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.*;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;

import javax.naming.InitialContext;
import javax.naming.NamingException;


public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;


	public HomePage(final PageParameters parameters) throws NamingException {
		super(parameters);


        add(new HtmlTag("html"));

        add(new OptimizedMobileViewportMetaTag("viewport"));
        add(new ChromeFrameMetaTag("chrome-frame"));
        add(new MetaTag("description", Model.of("description"), Model.of("Apache Wicket & Twitter Bootstrap Demo")));
        add(new MetaTag("author", Model.of("author"), Model.of("Michael Haitz <michael.haitz@agile-coders.de>")));

        add(new BootstrapBaseBehavior());
        add(new Code("code-internal"));



        add(makeVersionLabel());

        Navbar navbar = new Navbar("wicket-markup-id");
        navbar.setPosition(Navbar.Position.TOP);

        navbar.fluid();
        navbar.brandName(Model.of("Yakutia admin home"));


        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
                new NavbarButton<HomePage>(HomePage.class, Model.of("Home")).setIconType(IconType.home),
                new NavbarButton<HomePage>(JpaPage.class, Model.of("Jpa")).setIconType(IconType.check),
                new NavbarButton<HomePage>(HomePage.class, Model.of("REST")).setIconType(IconType.home),
                new NavbarButton<HomePage>(ApiPage.class, Model.of("API")).setIconType(IconType.home),
                new NavbarDropDownButton(Model.of("WTF?"))
                        .addButton(new MenuBookmarkablePageLink<HomePage>(HomePage.class, Model.of("Home1")).setIconType(IconType.thumbsup))
                        .addButton(new MenuBookmarkablePageLink<HomePage>(HomePage.class, Model.of("Home2")).setIconType(IconType.thumbsdown))
                        .setIconType(IconType.bell).add(new DropDownAutoOpen())
        ));

        add(navbar);

    }


    private Label makeVersionLabel() {
        return new Label("version", makeVersionValue());
    }

    private String makeVersionValue() {
        return getApplication().getFrameworkSettings().getVersion();
    }
}
