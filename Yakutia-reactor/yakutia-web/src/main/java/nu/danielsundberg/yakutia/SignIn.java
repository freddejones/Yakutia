package nu.danielsundberg.yakutia;

import nu.danielsundberg.yakutia.session.MySession;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import javax.naming.NamingException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Logger;

public class SignIn extends BasePage {

    private static final String USERNAME = "username";

    Token requestToken;

    public SignIn(PageParameters parameters) throws NamingException {
        super(parameters);
        getSession().bind();


        final String SCOPE = "https://www.googleapis.com/auth/userinfo.email";
        OAuthService service = new ServiceBuilder()
                .provider(GoogleApi.class)
                .apiKey("199525905624.apps.googleusercontent.com")
                .apiSecret("dFojU6QE5tdbi7BQ_j9wd-bo")
                .scope(SCOPE)
                .callback("http://localhost:8080/adminweb-1.0-SNAPSHOT/wicket/bookmarkable/nu.danielsundberg.yakutia.CallbackStuffPage")
                .build();

        requestToken = service.getRequestToken();
        getSession().setAttribute("requestToken", requestToken);
        final String authUrl = service.getAuthorizationUrl(requestToken);

        Form formgoogle = new Form("google-form");

        Button googleSign = new Button("google") {
            @Override
            public void onSubmit() {
                throw new RedirectToUrlException(authUrl);
            }

        };

        formgoogle.add(googleSign);
        add(formgoogle);
    }
}
