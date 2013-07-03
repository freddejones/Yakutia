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
import java.util.logging.Logger;


public class MySession extends AuthenticatedWebSession {

    @EJB(name = "pregamebean")
    protected PreGameInterface preGameInterface;

    private Logger log = Logger.getLogger(MySession.class.getName());
    private String playerName = "";
    private Long playerId = 0L;

    public MySession(Request request) {
        super(request);
        Injector.get().inject(this);
    }

    @Override
    public boolean authenticate(String email, String s2) {

        if ("admin".equals(email) && s2.equals(System.getProperty("YAKUTIA_ADMIN_PASSWORD"))) {
            setPlayerName("admin");
            setPlayerId(-1L);
            return true;
        } else if (preGameInterface.playerExists(email)) {
            try {
                Player sessionPlayer = preGameInterface.getPlayerByEmail(email);
                setPlayerId(sessionPlayer.getPlayerId());
                setPlayerName(sessionPlayer.getName());
                return true;
            } catch (NoPlayerFoundException e) {
                log.info(e.getMessage() + "\n" + e.toString());
                return false;
            }
        } else if (email.equals("temp")) {
            setPlayerId(666L);
            return true;
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
        } else if (getPlayerId().equals(666L)) {
            roles.add("USER");
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
