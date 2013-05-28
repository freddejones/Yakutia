package nu.danielsundberg.yakutia.session;

import nu.danielsundberg.yakutia.application.service.exceptions.NoPlayerFoundException;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.application.service.exceptions.PlayerAlreadyExists;
import nu.danielsundberg.yakutia.entity.Player;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;


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

        if ("admin".equals(email) && s2.equals("bajs")) {
            setPlayerName("admin");
            setPlayerId(-1L);
            return true;
        }

        if (preGameInterface.playerExists(email)) {
            try {
                Player sessionPlayer = preGameInterface.getPlayerByEmail(email);
                setPlayerId(sessionPlayer.getPlayerId());
                setPlayerName(sessionPlayer.getName());
                return true;
            } catch (NoPlayerFoundException e) {
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }

    @Override
    public Roles getRoles() {

        Roles roles = new Roles();

        if ( isSignedIn() ) {
            roles.add("USER");

            if (getPlayerName().equals("admin")) {
                roles.add("ADMIN");
            }
        }

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
