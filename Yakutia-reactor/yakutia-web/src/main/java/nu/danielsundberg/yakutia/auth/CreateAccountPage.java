package nu.danielsundberg.yakutia.auth;

import nu.danielsundberg.yakutia.BasePage;
import nu.danielsundberg.yakutia.NavbarPage;
import nu.danielsundberg.yakutia.PreGameInterface;
import nu.danielsundberg.yakutia.WelcomePage;
import nu.danielsundberg.yakutia.exceptions.PlayerAlreadyExists;
import nu.danielsundberg.yakutia.session.MySession;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateAccountPage extends BasePage {


    private String playername = "your player name";
    private String email = "";

    public CreateAccountPage(PageParameters parameters) throws NamingException {
        super(parameters);

        StringValue preEmail = parameters.get("email");
        setEmail(preEmail.toString());
        final String finalEmail = getEmail();

        System.out.println("MAIL = " + getEmail());

        Form form = new Form("form");

        PropertyModel model = new PropertyModel<String>(this, "playername");
        TextField<String> player = new TextField<String>("playername", model);
        model = new PropertyModel<String>(this, "email");
        TextField<String> email = new TextField<String>("email", model);

        Button button = new Button("createButton") {

            @Override
            public void onSubmit() {
                // TODO SAVE TOKEN AS WELL??????

                System.out.println("MAil out: " + finalEmail);
                String createPlayer = "http://localhost:8080/yakutia-services/rest/player/createplayer";
                HttpPost post = new HttpPost(createPlayer);

                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                nvps.add(new BasicNameValuePair("name", getPlayername()));
                nvps.add(new BasicNameValuePair("email", finalEmail));
//                StringEntity entity1 = new StringEntity("name=BLAHA", Consts.UTF_8);

                UrlEncodedFormEntity entity1 = new UrlEncodedFormEntity(nvps, Consts.UTF_8);
                entity1.setContentType("text/html");
                post.setHeader("Accept","text/html");

                post.addHeader(entity1.getContentType());
                post.setEntity(entity1);
                HttpClient client = new DefaultHttpClient();
                try {
                    HttpResponse resp = client.execute(post);

                    // TODO Check response that it was ok?

                    // TODO fix this
                    MySession session = (MySession)getSession();

                    if (session.signIn("admin",""))
                    {
                        PageParameters params = new PageParameters();
                        params.set("playername",getPlayername());
                        setResponsePage(WelcomePage.class, params);
                    }
                } catch (IOException e) {

                    // TODO How to handle this?

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
