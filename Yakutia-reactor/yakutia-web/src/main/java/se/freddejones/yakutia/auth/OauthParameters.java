package se.freddejones.yakutia.auth;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi;
import org.scribe.oauth.OAuthService;

import java.io.Serializable;

/**
 * User: Fredde
 * Date: 4/2/13 6:09 PM
 */
public class OauthParameters implements Serializable {

    public static final String SCOPE = "https://www.googleapis.com/auth/userinfo.email";
    public static final String APIKEY = "199525905624.apps.googleusercontent.com";
    public static final String APISECRET = "dFojU6QE5tdbi7BQ_j9wd-bo";
    public static String CALLBACKURL;
    public static final String GOOGLE_API_USERINFO = "https://www.googleapis.com/oauth2/v1/userinfo";

    static {
        CALLBACKURL = System.getProperty("YAKUTIA_CALLBACK_URL") +
                "yakutia-web/wicket/bookmarkable/se.freddejones.yakutia.auth.CallbackPage";
    }

    protected static OAuthService oathService;

    public static OAuthService getOAuthService() {
        if (oathService == null) {
            return new ServiceBuilder()
                    .provider(GoogleApi.class)
                    .apiKey(OauthParameters.APIKEY)
                    .apiSecret(OauthParameters.APISECRET)
                    .scope(OauthParameters.SCOPE)
                    .callback(OauthParameters.CALLBACKURL)
                    .build();
        } else {
            return oathService;
        }
    }
}
