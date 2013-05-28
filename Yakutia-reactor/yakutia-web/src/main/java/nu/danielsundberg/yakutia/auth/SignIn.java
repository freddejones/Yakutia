package nu.danielsundberg.yakutia.auth;

import nu.danielsundberg.yakutia.base.BasePage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import javax.naming.NamingException;

public class SignIn extends BasePage {

    protected Token requestToken;
//    protected  service;

    public SignIn(PageParameters parameters) throws NamingException {
        super(parameters);
        getSession().bind();

        OAuthService service = getOAuthService();

        requestToken = service.getRequestToken();
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

        // TODO REMOVE when not needed anymore
        Form formAdmin = new Form("admin-form");
        Button adminButton = new Button("admin") {
            @Override
            public void onSubmit() {
                PageParameters param = new PageParameters();
                param.add("admin","");
                setResponsePage(CallbackPage.class,param);
            }
        };
        formAdmin.add(adminButton);
        add(formAdmin);
    }

    public OAuthService getOAuthService() {
        return new ServiceBuilder()
                    .provider(GoogleApi.class)
                    .apiKey(OauthParameters.APIKEY)
                    .apiSecret(OauthParameters.APISECRET)
                    .scope(OauthParameters.SCOPE)
                    .callback(OauthParameters.CALLBACKURL)
                    .build();
    }
}
