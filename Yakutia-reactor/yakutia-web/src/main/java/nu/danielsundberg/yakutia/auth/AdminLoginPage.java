package nu.danielsundberg.yakutia.auth;

import nu.danielsundberg.yakutia.base.BasePage;
import nu.danielsundberg.yakutia.base.WelcomePage;
import nu.danielsundberg.yakutia.session.MySession;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * User: Fredde
 * Date: 5/28/13 9:10 PM
 */
public class AdminLoginPage extends BasePage {

    public AdminLoginPage(PageParameters parameters) {
        super(parameters);

        Form formAdmin = new Form("admin-form");
        Button adminButton = new Button("admin") {
            @Override
            public void onSubmit() {
                MySession session = (MySession) getSession();

                if (session.signIn("admin","bajs")) { //TODO fix this with a real form
                    setResponsePage(WelcomePage.class);
                }

            }
        };
        formAdmin.add(adminButton);
        add(formAdmin);

    }
}
