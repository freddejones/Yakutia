package nu.danielsundberg.yakutia.session;

import nu.danielsundberg.yakutia.WelcomePage;
import nu.danielsundberg.yakutia.auth.CreateAccountPage;
import nu.danielsundberg.yakutia.auth.RestParameters;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;


public class MySession extends AuthenticatedWebSession {

    private String playerName = "";

    public MySession(Request request) {
        super(request);
    }

    @Override
    public boolean authenticate(String email, String s2) {
        HttpClient client = new DefaultHttpClient();

        String restUrl = RestParameters.PLAYEREXIST_URL;
        restUrl += "?email="+email;
        HttpGet request = new HttpGet(restUrl);
        HttpResponse response = null;
        try {
            response = client.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                if (line.contains("true")) {
                    request = new HttpGet(RestParameters.PLAYERBYEMAIL_URL+"?email="+email);
                    response = client.execute(request);
                    BufferedReader rd2 = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String line2 = "";
                    while ((line2 = rd2.readLine()) != null) {
                        setPlayerName(line2);
                        return true;
                    }

                }
            }
        } catch (IOException e) {
            // TODO What to do?
            e.printStackTrace();
        }

        if ("admin".equals(email)) {
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

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
