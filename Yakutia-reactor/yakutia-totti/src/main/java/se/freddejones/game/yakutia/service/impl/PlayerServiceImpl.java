package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.PlayerDao;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.exception.PlayerAlreadyExistsException;
import se.freddejones.game.yakutia.service.PlayerService;

import java.util.logging.Logger;

@Service("playerservice")
@Transactional(readOnly = true)
public class PlayerServiceImpl implements PlayerService {

    private Logger log = Logger.getLogger(PlayerServiceImpl.class.getName());

    @Autowired
    PlayerDao playerDao;

    @Override
    @Transactional(readOnly = false)
    public Long createNewPlayer(Player p) throws PlayerAlreadyExistsException {
        return playerDao.createPlayer(p);
    }


}
