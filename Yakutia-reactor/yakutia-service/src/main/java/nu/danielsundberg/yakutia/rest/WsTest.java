package nu.danielsundberg.yakutia.rest;

import nu.danielsundberg.yakutia.GameEngineInterface;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
@Stateless
public class WsTest {

    @EJB
    private GameEngineInterface gameEngine;

    @WebMethod
    public long createNewGame(List<Long> playerIds) {
        return gameEngine.startNewGame(playerIds);
    }

    @WebMethod
    public String getPlayers() {
        List<String> players = gameEngine.getPlayers();
        return "Number of players: " + players.size();
    }

    @WebMethod
    public void deleteAllPlayers() {
        gameEngine.deleteAllPlayers();
    }

    @WebMethod
    public long createNewPlayer(String name) {
        return gameEngine.createNewPlayer(name);
    }

    @WebMethod
    public void addNewUnitsToPlayer(long gameId, long playerId) {
        gameEngine.addUnitsToGamePlayer(gameId,playerId);
    }

}
