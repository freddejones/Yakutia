package se.freddejones.yakutia.application.service.random;

import se.freddejones.yakutia.application.service.landAreas.LandArea;
import se.freddejones.yakutia.entity.GamePlayer;

import java.util.Collections;
import java.util.List;

/**
 * User: Fredde
 * Date: 9/15/13 8:48 PM
 */
public class RandomEventHandler {

    public List<GamePlayer> shufflePlayer(List<GamePlayer> gamePlayers) {
        Collections.shuffle(gamePlayers);
        return gamePlayers;
    }

    public List<LandArea> shuffleLandAreas(List<LandArea> landAreas) {
        Collections.shuffle(landAreas);
        return landAreas;
    }
}
