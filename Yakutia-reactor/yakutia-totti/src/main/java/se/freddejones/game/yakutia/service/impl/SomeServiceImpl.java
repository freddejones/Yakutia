package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.PlayerDao;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.service.SomeService;

import java.util.logging.Logger;

@Service("testingsevice")
@Transactional(readOnly = true)
public class SomeServiceImpl implements SomeService {

    private Logger log = Logger.getLogger(SomeServiceImpl.class.getName());

    @Autowired
    protected PlayerDao playerDao;

    @Override
    public void doSomething() {
        log.info("Testme out");
    }

    @Override
    @Transactional(readOnly = false)
    public void savePlayer(Player p) {
        log.info("adding player");
        playerDao.createPlayer(p);
        log.info("player added");
    }
}
