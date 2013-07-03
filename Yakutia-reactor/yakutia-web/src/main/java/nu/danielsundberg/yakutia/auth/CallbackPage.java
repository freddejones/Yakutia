package nu.danielsundberg.yakutia.auth;

import nu.danielsundberg.yakutia.base.BasePage;
import nu.danielsundberg.yakutia.base.ErrorPage;
import nu.danielsundberg.yakutia.base.WelcomePage;
import nu.danielsundberg.yakutia.session.MySession;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;
import java.util.Iterator;

/**
 * User: Fredde
 * Date: 3/29/13 12:19 AM
 */
public class CallbackPage extends BasePage {

    public CallbackPage(PageParameters parameters) {
        super(parameters);
        getSession().bind();

        String oauthVerifier = null;
        Iterator<String> iter = parameters.getNamedKeys().iterator();
        while (iter.hasNext()) {
            String name = iter.next();
            if (name.equals("oauth_verifier")) {
                oauthVerifier = parameters.get(name).toString();
                if (oauthVerifier.equals("undefined")) {
                    oauthVerifier = null;
                    // TODO handle some exception here?
                }
            }
        }

        Token requestToken = null;
        if (getSession().getAttribute("requestToken") != null) {
            requestToken = (Token) getSession().getAttribute("requestToken");
        }

        OAuthService service = new ServiceBuilder()
                .provider(GoogleApi.class)
                .apiKey(OauthParameters.APIKEY)
                .apiSecret(OauthParameters.APISECRET)
                .scope(OauthParameters.SCOPE)
                .build();

        String emailExtracted = "";
        if (oauthVerifier != null) {
            try {
                Verifier verifier = new Verifier(oauthVerifier);
                Token accessToken = service.getAccessToken(requestToken, verifier);

                OAuthRequest request = new OAuthRequest(Verb.GET,
                        OauthParameters.GOOGLE_API_USERINFO);
                service.signRequest(accessToken, request);
                request.addHeader("GData-Version", "3.0");
                Response response = request.send();

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response.getBody().toString());
                    emailExtracted = obj.getString("email");
                } catch (JSONException e) {
                    PageParameters pgp = new PageParameters();
                    pgp.add("message","email not found..");
                    setResponsePage(ErrorPage.class, pgp);
                }

            } catch (RuntimeException re) {
                PageParameters pgp = new PageParameters();
                pgp.add("message","failed oauth verification");
                setResponsePage(ErrorPage.class, pgp);
            }
        } else {
            PageParameters pgp = new PageParameters();
            pgp.add("message","no oauth stuff");
            setResponsePage(ErrorPage.class, pgp);
        }

        MySession session = (MySession)getSession();

        if (session.signIn(emailExtracted,""))
        {
            setResponsePage(WelcomePage.class);
        } else {
            if (session.signIn("temp","")) {
                PageParameters params = new PageParameters();
                params.set("email",emailExtracted);
                setResponsePage(CreateAccountPage.class, params);
            } else {
                // TODO FIX ME
                System.out.println("COULDNT DO THE CREATE LOGIN STUFF");
            }
        }

        // TODO add some nice message here
        add(new Label("msg", "Something went wrong.."));

    }



}
