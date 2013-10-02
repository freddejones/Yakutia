package se.freddejones.yakutia.auth;

import se.freddejones.yakutia.base.BasePage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import javax.naming.NamingException;

public class SignIn extends BasePage {

    public SignIn(PageParameters parameters) throws NamingException {
        super(parameters);
        getSession().bind();

        OAuthService service = OauthParameters.getOAuthService();

        Token requestToken = service.getRequestToken();
        getSession().setAttribute("requestToken", requestToken);
        final String authUrl = service.getAuthorizationUrl(requestToken);

        Button googleSign = new Button("google") {
            @Override
            public void onSubmit() {
                throw new RedirectToUrlException(authUrl);
            }
        };

        Form form = new Form("google-form");
        form.add(googleSign);
        add(form);
    }

}
