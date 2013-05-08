package nu.danielsundberg.yakutia.rest;

import nu.danielsundberg.yakutia.application.service.iface.GameEngineInterface;
import nu.danielsundberg.yakutia.application.service.iface.PlayerActionsInterface;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.application.service.exceptions.LandIsNotNeighbourException;
import nu.danielsundberg.yakutia.application.service.exceptions.PlayerAlreadyExists;
import nu.danielsundberg.yakutia.application.service.landAreas.LandArea;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
            long p1ID = preGame.createNewPlayer("Jones","mail");
            long p2ID = preGame.createNewPlayer("Apan","mail");
            long p3Id = preGame.createNewPlayer("trean","mail");
            long gameID = gameEngine.createNewGame(p1ID);

            preGame.invitePlayerToGame(p2ID,gameID);
            preGame.invitePlayerToGame(p3Id, gameID);

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
