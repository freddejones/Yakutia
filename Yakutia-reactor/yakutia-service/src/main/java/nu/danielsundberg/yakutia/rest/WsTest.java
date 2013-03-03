package nu.danielsundberg.yakutia.rest;

import nu.danielsundberg.yakutia.GameEngineInterface;
import nu.danielsundberg.yakutia.GameplayerId;
import nu.danielsundberg.yakutia.PlayerActionsInterface;
import nu.danielsundberg.yakutia.PreGameInterface;
import nu.danielsundberg.yakutia.application.service.impl.GameEngineBean;
import nu.danielsundberg.yakutia.application.service.impl.PreGameBean;
import nu.danielsundberg.yakutia.exceptions.LandIsNotNeighbourException;
import nu.danielsundberg.yakutia.exceptions.PlayerAlreadyExists;
import nu.danielsundberg.yakutia.landAreas.LandArea;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@WebService
@Stateless
public class WsTest {

//    @Resource
//    InitialContext ctx;

    private Logger log = Logger.getLogger(WsTest.class.getName());


    // TODO How to inject beans correctly? (strongly typed)
    @EJB
    private GameEngineInterface gameEngine;
    @EJB
    private PreGameInterface preGame;
    @EJB
    private PlayerActionsInterface playerActions;

    @WebMethod
    public void runScenario() {
        try {
            InitialContext ctx = new InitialContext();
            preGame = (PreGameInterface) ctx.lookup("preGameBean");
            gameEngine = (GameEngineInterface) ctx.lookup("kickass");
            playerActions = (PlayerActionsInterface) ctx.lookup("playerActionsBean");
            long p1ID = preGame.createNewPlayer("Jones");
            long p2ID = preGame.createNewPlayer("Apan");
            long gameID = gameEngine.createNewGame(p1ID);

            preGame.acceptInvite(p2ID,gameID);

            log.warning("Accepting invite");

            log.info("Testing log info level");

            // TODO user playerid instead of gameplayer
            List<GameplayerId> listOfPlayers = new ArrayList<GameplayerId>();
            GameplayerId gameplayerId = new GameplayerId();
            GameplayerId gamePlayerId2  = new GameplayerId();
            gameplayerId.setPlayerId(p1ID);
            listOfPlayers.add(gameplayerId);
            gamePlayerId2.setPlayerId(p2ID);
            listOfPlayers.add(gamePlayerId2);
            log.info("starting new game");
            gameEngine.startNewGame(listOfPlayers);
            log.info("New game started");
            List<LandArea> landAreasP1 = playerActions.getPlayersLandAreas(p1ID, gameID);
            List<LandArea> landAreasP2 = playerActions.getPlayersLandAreas(p2ID, gameID);
            playerActions.attackLandArea(landAreasP1.get(0),landAreasP2.get(0));

        } catch (NamingException e) {
        } catch (PlayerAlreadyExists playerAlreadyExists) {
        } catch (LandIsNotNeighbourException e) {
        }

    }


}
