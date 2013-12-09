package se.freddejones.game.yakutia.dao;


import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.exception.PlayerAlreadyExistsException;
import se.freddejones.game.yakutia.model.dto.PlayerDTO;

import java.util.List;

public interface PlayerDao {

    public Long createPlayer(Player p) throws PlayerAlreadyExistsException;

    public List<Long> getAllPlayerIds();

    public PlayerDTO getPlayerById(Long playerId);

    public PlayerDTO getPlayerByEmail(String email);

}
