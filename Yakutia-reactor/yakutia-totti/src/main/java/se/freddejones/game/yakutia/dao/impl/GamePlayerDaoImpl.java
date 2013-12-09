package se.freddejones.game.yakutia.dao.impl;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import se.freddejones.game.yakutia.dao.GameDao;
import se.freddejones.game.yakutia.dao.GamePlayerDao;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.model.dto.GameDTO;
import se.freddejones.game.yakutia.model.statuses.GamePlayerStatus;
import se.freddejones.game.yakutia.model.statuses.GameStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class GamePlayerDaoImpl extends AbstractDaoImpl implements GamePlayerDao {

    @Override
    public List<GamePlayer> getGamePlayersByPlayerId(Long playerid) {
        return (List<GamePlayer>) getCurrentSession().getNamedQuery("GamePlayer.getGamePlayersFromPlayerId")
                .setParameter("playerId", playerid).list();
    }
}
