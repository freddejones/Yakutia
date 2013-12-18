package se.freddejones.game.yakutia.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import se.freddejones.game.yakutia.dao.GameDao;
import se.freddejones.game.yakutia.entity.Game;
import se.freddejones.game.yakutia.entity.GamePlayer;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.model.statuses.GamePlayerStatus;
import se.freddejones.game.yakutia.model.statuses.GameStatus;

import java.util.Date;

@Repository
public class GameDaoImpl extends AbstractDaoImpl implements GameDao {

    @Override
    public Long createNewGame(Long playerId, String gameName) {
        Session session = getCurrentSession();

        Game g = new Game();
        g.setCreationTime(new Date());
        g.setGameCreatorPlayerId(playerId);
        g.setName(gameName);
        g.setGameStatus(GameStatus.CREATED);
        session.saveOrUpdate(g);
        session.refresh(g);

        GamePlayer gp = new GamePlayer();
        gp.setGame(g);
        gp.setGameId(g.getGameId());
        Player p = (Player) getCurrentSession().get(Player.class, playerId);
        gp.setPlayer(p);
        gp.setPlayerId(p.getPlayerId());
        gp.setGamePlayerStatus(GamePlayerStatus.ACCEPTED);
        gp.setActivePlayerTurn(false);

        getCurrentSession().saveOrUpdate(gp);
        session.refresh(g);
        g.getPlayers().add(gp);
        session.merge(g);

        return g.getGameId();
    }

    @Override
    public Game getGameByGameId(long gameId) {
        return (Game)getCurrentSession().get(Game.class, gameId);
    }

    @Override
    public void startGame(long gameId) {
        Session session = getCurrentSession();
        Game realGame = (Game)session.get(Game.class, gameId);
        realGame.setGameStatus(GameStatus.ONGOING);
        realGame.setStartedTime(new Date());
        session.saveOrUpdate(realGame);
    }
}
