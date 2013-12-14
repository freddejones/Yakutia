package se.freddejones.game.yakutia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import se.freddejones.game.yakutia.model.YakutiaModel;
import se.freddejones.game.yakutia.model.dto.CreateGameDTO;
import se.freddejones.game.yakutia.model.dto.GameDTO;
import se.freddejones.game.yakutia.service.GameService;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = "/game")
public class GameController {

    private Logger log = Logger.getLogger(GameController.class.getName());
    GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @RequestMapping(value  = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Long createNewGame(@RequestBody final CreateGameDTO createGameDTO) {
        log.info("Received CreateGameDTO: " + createGameDTO.toString());
        return gameService.createNewGame(createGameDTO);
    }

    @RequestMapping(value = "/get/{playerId}", method = RequestMethod.GET)
    @ResponseBody
    public List<GameDTO> getAllGamesById(@PathVariable("playerId") Long playerid) {
        log.info("Getting games for playerId: " + playerid);
        List<GameDTO> list = gameService.getGamesForPlayerById(playerid);
        log.info("Fetched " + list.size() + " number of games");
        return list;
    }

    @RequestMapping(value = "/get/{playerId}/game/{gameId}", method = RequestMethod.GET)
    @ResponseBody
    public List<YakutiaModel> getGame(@PathVariable("playerId") Long playerId,
                                       @PathVariable("gameId") Long gameId) {
        log.info("Getting game information for gameId: " + gameId + " and playerId: " + playerId);
        return gameService.getGameModelForPlayerAndGameId(playerId, gameId);
    }
}
