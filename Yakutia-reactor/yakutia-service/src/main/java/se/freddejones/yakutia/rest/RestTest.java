package se.freddejones.yakutia.rest;


import se.freddejones.yakutia.application.service.exceptions.PlayerAlreadyExists;
import se.freddejones.yakutia.application.service.iface.PreGameInterface;
import se.freddejones.yakutia.entity.Player;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
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

    @Path("/getAllNonFriendPlayers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Player> getAllPlayers() {
        try {
            InitialContext ctx = new InitialContext();
            preGame = (PreGameInterface) ctx.lookup("preGameBean");
            return preGame.getPlayers();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return new ArrayList<Player>();
    }

//    @Path("/getPlayerByEmail")
//    @GET
//    @Produces("text/html")
//    @Consumes("text/html")
//    public String getPlayers(@QueryParam("email") String email) {
//        try {
//            InitialContext ctx = new InitialContext();
//            preGame = (PreGameInterface) ctx.lookup("preGameBean");
//            return preGame.getPlayerByEmail(email);
//        } catch (NamingException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

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
