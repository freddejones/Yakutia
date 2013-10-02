package se.freddejones.yakutia.base;

import de.agilecoders.wicket.core.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.*;
import se.freddejones.yakutia.friends.FriendsPage;
import se.freddejones.yakutia.games.GamesPage;
import se.freddejones.yakutia.session.SignOut;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.naming.NamingException;


public class NavbarPage extends BasePage {
	private static final long serialVersionUID = 1L;


	public NavbarPage(final PageParameters parameters) throws NamingException {
		super(parameters);

        Navbar navbar = new Navbar("wicket-markup-id");
        navbar.setPosition(Navbar.Position.TOP);

        navbar.fluid();
        navbar.brandName(Model.of("Yakutia"));

        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.LEFT,
                new NavbarButton<NavbarPage>(WelcomePage.class, Model.of("Home")).setIconType(IconType.home),
                new NavbarButton<NavbarPage>(GamesPage.class, Model.of("Games")).setIconType(IconType.thlist),
                new NavbarButton<NavbarPage>(FriendsPage.class, Model.of("Friends")).setIconType(IconType.search),
                new NavbarButton<NavbarPage>(SignOut.class, Model.of("Logout")).setIconType(IconType.off)
                /*new NavbarDropDownButton(Model.of("WTF?"))
                        .addButton(new MenuBookmarkablePageLink<NavbarPage>(NavbarPage.class, Model.of("Home1")).setIconType(IconType.thumbsup))
                        .addButton(new MenuBookmarkablePageLink<NavbarPage>(NavbarPage.class, Model.of("Home2")).setIconType(IconType.thumbsdown))
                        .setIconType(IconType.bell).add(new DropDownAutoOpen())*/
        ));

        add(navbar);

    }

}
