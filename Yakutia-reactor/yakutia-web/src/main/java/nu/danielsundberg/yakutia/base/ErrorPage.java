package nu.danielsundberg.yakutia.base;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * User: Fredde
 * Date: 5/28/13 8:31 PM
 */
public class ErrorPage extends BasePage {


    public ErrorPage(PageParameters parameters) {
        super(parameters);
        getSession().invalidate();

        String message = parameters.get("message").toString();

        add(new Label("msg",message));

        add(new BookmarkablePageLink("homelink", WelcomePage.class));
    }
}
