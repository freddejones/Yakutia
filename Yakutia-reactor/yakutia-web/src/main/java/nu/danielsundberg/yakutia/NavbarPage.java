package nu.danielsundberg.yakutia;

import de.agilecoders.wicket.markup.html.bootstrap.image.IconType;
import de.agilecoders.wicket.markup.html.bootstrap.navbar.*;
import nu.danielsundberg.yakutia.session.SignOut;
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
                new NavbarButton<NavbarPage>(JpaPage.class, Model.of("Jpa")).setIconType(IconType.check),
                new NavbarButton<NavbarPage>(ApiPage.class, Model.of("API")).setIconType(IconType.thumbsup),
                new NavbarButton<NavbarPage>(SignOut.class, Model.of("Logout")).setIconType(IconType.play)
                /*new NavbarDropDownButton(Model.of("WTF?"))
                        .addButton(new MenuBookmarkablePageLink<NavbarPage>(NavbarPage.class, Model.of("Home1")).setIconType(IconType.thumbsup))
                        .addButton(new MenuBookmarkablePageLink<NavbarPage>(NavbarPage.class, Model.of("Home2")).setIconType(IconType.thumbsdown))
                        .setIconType(IconType.bell).add(new DropDownAutoOpen())*/
        ));

        add(navbar);

    }

}
