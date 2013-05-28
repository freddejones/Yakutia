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

        if (preGameInterface.playerExists(email)) {
            try {
                Player sessionPlayer = preGameInterface.getPlayerByEmail(email);
                setPlayerId(sessionPlayer.getPlayerId());
                setPlayerName(sessionPlayer.getName());
                return true;
            } catch (NoPlayerFoundException e) {
                e.printStackTrace(); //TODO Fix something here, a page or something
            }
        }

        if ("admin".equals(email)) {

            if (!preGameInterface.playerExists("admin@jones.com")) {
                try {
                    playerId = preGameInterface.createNewPlayer("admin", "admin@jones.com");
                } catch (PlayerAlreadyExists playerAlreadyExists) {
                    playerAlreadyExists.printStackTrace();
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
