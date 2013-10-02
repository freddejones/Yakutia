package se.freddejones.yakutia.session;

import se.freddejones.yakutia.base.BasePage;
import se.freddejones.yakutia.base.WelcomePage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * User: Fredde
 * Date: 3/27/13 12:03 AM
 */
public class SignOut extends BasePage {


    public SignOut(PageParameters parameters) {
        super(parameters);
        getSession().invalidate();

        add(new BookmarkablePageLink("homelink", WelcomePage.class));
    }
}
