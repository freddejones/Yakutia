package se.freddejones.game.yakutia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.model.YakutiaModel;
import se.freddejones.game.yakutia.model.YakutiaSuperModel;
import se.freddejones.game.yakutia.service.SomeService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/game")
public class GameController {

    @Autowired
    private SomeService someService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public YakutiaSuperModel getPlayers() {
        YakutiaModel model1 = new YakutiaModel("sweden", 7, true);
        YakutiaModel model2 = new YakutiaModel("finland", 2, true);
        YakutiaModel model3 = new YakutiaModel("norway", 12, false);
        List<YakutiaModel> list = new ArrayList<YakutiaModel>();
        list.add(model1);
        list.add(model2);
        list.add(model3);

        YakutiaSuperModel model = new YakutiaSuperModel();
        model.setLandAreas(list);

        return model;
    }

    @RequestMapping(value  = "/create/player", method = RequestMethod.GET)
    @ResponseBody
    public String addRandomPlayer() {
        someService.doSomething();
        Player p = new Player();
        p.setEmail("fiddetest@gmail.apa");
        p.setName("tjoho");
        someService.savePlayer(p);
        return "200";
    }
}
