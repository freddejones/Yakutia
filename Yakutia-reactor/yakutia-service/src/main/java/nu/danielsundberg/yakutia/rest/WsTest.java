package nu.danielsundberg.yakutia.rest;

import nu.danielsundberg.yakutia.GameEngineInterface;
import nu.danielsundberg.yakutia.GameplayerId;
import nu.danielsundberg.yakutia.PlayerActionsInterface;
import nu.danielsundberg.yakutia.PreGameInterface;
import nu.danielsundberg.yakutia.application.service.impl.GameEngineBean;
import nu.danielsundberg.yakutia.application.service.impl.PreGameBean;
import nu.danielsundberg.yakutia.exceptions.LandIsNotNeighbourException;
import nu.danielsundberg.yakutia.exceptions.PlayerAlreadyExists;
import nu.danielsundberg.yakutia.exceptions.TurnCannotBeEndedException;
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
            gameEngine = (GameEngineInterface) ctx.lookup("kickass"); // TODO RENAME THIS BEAN
            playerActions = (PlayerActionsInterface) ctx.lookup("playerActionsBean");

            log.info("Creating players + creating a new game");
            long p1ID = preGame.createNewPlayer("Jones");
            long p2ID = preGame.createNewPlayer("Apan");
            long p3Id = preGame.createNewPlayer("trean");
            long gameID = gameEngine.createNewGame(p1ID);

            preGame.invitePlayerToGame("pan",gameID);
            preGame.invitePlayerToGame("trean", gameID);

            log.info("invited to game: "+ preGame.getInvites(p2ID).get(0));

            log.info("Accepting invite");
            preGame.acceptInvite(p2ID,gameID);

            log.info("Declined invite");
            preGame.declineInvite(p3Id, gameID);

            log.info("starting new game");
            gameEngine.startNewGame(gameID);

            log.info("Place units for player1");
            List<LandArea> landAreasP1 = playerActions.getPlayersLandAreas(p1ID, gameID);
            playerActions.placeUnits(p1ID, gameID, landAreasP1.get(0), 14);
            log.info("turn ended correctly? " + playerActions.endTurn(p1ID,gameID));

            log.info("Place units for player2");
            List<LandArea> landAreasP2 = playerActions.getPlayersLandAreas(p2ID, gameID);
            playerActions.placeUnits(p2ID, gameID, landAreasP2.get(0), 14);
            log.info("turn ended correctly? " + playerActions.endTurn(p2ID,gameID));

            log.info("is game finished? " + gameEngine.isGameFinished(gameID));

            landAreasP1 = playerActions.getPlayersLandAreas(p1ID, gameID);
            landAreasP2 = playerActions.getPlayersLandAreas(p2ID, gameID);
            playerActions.attackLandArea(landAreasP1.get(0),landAreasP2.get(0));
            log.info("did attacking end turn correctly? " +
                    playerActions.endTurn(p1ID, gameID));

            log.info("is game finished? " + gameEngine.isGameFinished(gameID));
            gameEngine.endGame(gameID);

        } catch (NamingException e) {
        } catch (PlayerAlreadyExists playerAlreadyExists) {
        } catch (LandIsNotNeighbourException e) {
//        } catch (LandIsNotNeighbourException e) {
        }

    }


}
