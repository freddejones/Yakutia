package nu.danielsundberg.yakutia.auth;

import nu.danielsundberg.yakutia.base.BasePage;
import nu.danielsundberg.yakutia.base.WelcomePage;
import nu.danielsundberg.yakutia.session.MySession;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.ResourceBundle;

/**
 * User: Fredde
 * Date: 5/28/13 9:10 PM
 */
public class AdminLoginPage extends BasePage {

    private String password;

    public String getPassword() {
        return password;
    }

    public AdminLoginPage(PageParameters parameters) {
        super(parameters);

        Form formAdmin = new Form("admin-form");

        PropertyModel model = new PropertyModel<String>(this, "password");
        PasswordTextField passwordTextField = new PasswordTextField("adminPassword", model);

        Button adminButton = new Button("admin") {
            @Override
            public void onSubmit() {
                MySession session = (MySession) getSession();

                if (session.signIn("admin",getPassword())) {
                    setResponsePage(WelcomePage.class);
                }

            }
        };
        formAdmin.add(adminButton);
        formAdmin.add(passwordTextField);
        add(formAdmin);

    }
}
