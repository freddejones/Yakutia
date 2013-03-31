package nu.danielsundberg.yakutia.session;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.util.Scanner;


public class MySession extends AuthenticatedWebSession {

    public MySession(Request request) {
        super(request);
    }

    private static final String NETWORK_NAME = "Google";
    private static final String AUTHORIZE_URL = "https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=";
    private static final String PROTECTED_RESOURCE_URL = "https://docs.google.com/feeds/default/private/full/";
    private static final String SCOPE = "https://www.googleapis.com/auth/userinfo.profile";


    @Override
    public boolean authenticate(String s, String s2) {
        // TODO make a call to with REST to authenticate uhmhhmm

        if ("admin".equals(s)) {
            return true;
        }

        return false;
    }

    @Override
    public Roles getRoles() {

        Roles roles = new Roles();

        if ( isSignedIn() )
            roles.add("USER");

        return roles;

    }
}
