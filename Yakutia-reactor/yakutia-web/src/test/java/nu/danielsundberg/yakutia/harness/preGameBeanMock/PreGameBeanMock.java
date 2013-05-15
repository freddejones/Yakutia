package nu.danielsundberg.yakutia.harness.preGameBeanMock;

import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.application.service.exceptions.PlayerAlreadyExists;
import nu.danielsundberg.yakutia.entity.Game;
import nu.danielsundberg.yakutia.entity.GamePlayer;
import nu.danielsundberg.yakutia.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * User: Fredde
 * Date: 5/7/13 8:43 PM
 */
public class PreGameBeanMock implements PreGameInterface {

    @Override
    public boolean playerExists(String email) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long createNewPlayer(String name, String email) throws PlayerAlreadyExists {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long createNewGame(long playerId) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void invitePlayerToGame(long playerId, long gameId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Long> getInvites(long playerId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean acceptInvite(long playerId, long gameId) {
        return false;
    }

    @Override
    public boolean declineInvite(long playerId, long gameId) {
        return false;
    }

    @Override
    public List<Player> getPlayers() {
        return new ArrayList<Player>();
    }

    @Override
    public Player getPlayerByName(String name) {
        return null;
    }

    @Override
    public Player getPlayerById(long id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
