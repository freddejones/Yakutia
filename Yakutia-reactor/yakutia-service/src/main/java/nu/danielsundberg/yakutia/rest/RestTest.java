package nu.danielsundberg.yakutia.rest;


import nu.danielsundberg.yakutia.PreGameInterface;
import nu.danielsundberg.yakutia.application.service.impl.GameEngineBean;
import nu.danielsundberg.yakutia.application.service.impl.PreGameBean;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.exceptions.PlayerAlreadyExists;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/player")
@Stateless
public class RestTest {

    @EJB
    private PreGameInterface preGame;

    public RestTest() {}

    @Path("/playerExist")
    @GET
    @Produces("text/html")
    public String doesPlayerExist(@QueryParam("email") String email) {
        InitialContext ctx = null;
        try {
            ctx = new InitialContext();
            preGame = (PreGameInterface) ctx.lookup("preGameBean");
            if (preGame.playerExists(email)) {
                return "true";
            } else {
                return "false";
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return "false";
    }

    @Path("/getplayer")
    @GET
    @Produces("text/html")
    public String getPlayers() {
        return "JUST A TEST";
    }

    @Path("/createplayer")
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.TEXT_HTML)
    public String createPlayer(@FormParam("name") String name,
                               @FormParam("email") String email) {
        try {
            InitialContext ctx = new InitialContext();
            preGame = (PreGameInterface) ctx.lookup("preGameBean");
            return String.valueOf(preGame.createNewPlayer(name, email));
        } catch (PlayerAlreadyExists playerAlreadyExists) {
            playerAlreadyExists.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return "-1";
    }

}
