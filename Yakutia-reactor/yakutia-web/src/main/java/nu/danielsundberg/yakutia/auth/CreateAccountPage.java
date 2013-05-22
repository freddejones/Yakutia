package nu.danielsundberg.yakutia.auth;

import nu.danielsundberg.yakutia.BasePage;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.WelcomePage;
import nu.danielsundberg.yakutia.application.service.exceptions.PlayerAlreadyExists;
import nu.danielsundberg.yakutia.session.MySession;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.*;
import javax.ejb.EJB;
import javax.naming.NamingException;

public class CreateAccountPage extends BasePage {

    @EJB(name = "pregamebean")
    private PreGameInterface preGameInterface;

    private String playername = "your player name";
    private String email = "";

    public CreateAccountPage(PageParameters parameters) throws NamingException {
        super(parameters);

        StringValue preEmail = parameters.get("email");
        setEmail(preEmail.toString());
        final String finalEmail = getEmail();

        Form form = new Form("form");

        PropertyModel model = new PropertyModel<String>(this, "playername");
        TextField<String> player = new TextField<String>("playername", model);
        model = new PropertyModel<String>(this, "email");
        TextField<String> email = new TextField<String>("email", model);

        Button button = new Button("createButton") {

            @Override
            public void onSubmit() {

                try {
                    preGameInterface.createNewPlayer(getPlayername(), finalEmail);

                    MySession session = (MySession)getSession();
                    if (session.signIn(finalEmail,""))
                    {   setResponsePage(WelcomePage.class); }

                } catch (PlayerAlreadyExists playerAlreadyExists) {
                    playerAlreadyExists.printStackTrace();
                }

            }
        };

        form.add(button);
        form.add(player);
        form.add(email);
        add(form);
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
