package nu.danielsundberg.yakutia.session;

import nu.danielsundberg.yakutia.application.service.exceptions.NoPlayerFoundException;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.auth.RestParameters;
import nu.danielsundberg.yakutia.application.service.exceptions.PlayerAlreadyExists;
import nu.danielsundberg.yakutia.entity.Player;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
//import wicket.injection.web.InjectorHolder;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MySession extends AuthenticatedWebSession {

    @EJB(name = "pregamebean")
    protected PreGameInterface preGameInterface;

    private String playerName = "";
    private Long playerId;

    public MySession(Request request) {
        super(request);
        Injector.get().inject(this);
    }

    @Override
    public boolean authenticate(String email, String s2) {

        if (preGameInterface.playerExists(email)) {
            try {
                Player seesionPlayer = preGameInterface.getPlayerByEmail(email);
                setPlayerId(seesionPlayer.getPlayerId());
                setPlayerName(seesionPlayer.getName());
                return true;
            } catch (NoPlayerFoundException e) {
                // TODO just add logging here?
            }
        }

        if ("admin".equals(email)) {

            if (!preGameInterface.playerExists("admin@jones.com")) {
                try {
                    playerId = preGameInterface.createNewPlayer("admin", "admin@jones.com");
                } catch (PlayerAlreadyExists playerAlreadyExists) {
                    // TODO add logging here?
                }
            }

            setPlayerName("admin");
            setPlayerId(playerId);
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

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
}
