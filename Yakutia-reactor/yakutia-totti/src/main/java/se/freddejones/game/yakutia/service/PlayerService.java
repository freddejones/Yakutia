package se.freddejones.game.yakutia.service;

import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.exception.PlayerAlreadyExistsException;

public interface PlayerService {

    public Long createNewPlayer(Player p) throws PlayerAlreadyExistsException;

}
