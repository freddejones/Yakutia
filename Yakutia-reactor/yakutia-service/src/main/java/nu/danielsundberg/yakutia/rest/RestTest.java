package nu.danielsundberg.yakutia.rest;


import nu.danielsundberg.yakutia.application.service.impl.GameEngineBean;
import nu.danielsundberg.yakutia.entity.Player;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

@Path("/player")
@Stateless
public class RestTest {

    @EJB
    private GameEngineBean gameEngine;

    public RestTest() {
    }

    @GET
    public String getPlayers() {
        return "JUST A TEST";
    }

}
