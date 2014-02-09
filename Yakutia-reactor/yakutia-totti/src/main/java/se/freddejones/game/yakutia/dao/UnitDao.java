package se.freddejones.game.yakutia.dao;

import se.freddejones.game.yakutia.model.LandArea;

/**
 * Created by fidde on 2014-02-09.
 */
public interface UnitDao {

    Long getGamePlayerIdByLandAreaAndGameId(Long gameId, LandArea territory);

}
