package nu.danielsundberg.yakutia.session;

import nu.danielsundberg.yakutia.BasePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * User: Fredde
 * Date: 3/27/13 12:03 AM
 */
public class SignOut extends BasePage {


    public SignOut(PageParameters parameters) {
        super(parameters);
        getSession().invalidate();
    }
}
