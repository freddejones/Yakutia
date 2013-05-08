package nu.danielsundberg.yakutia.harness;

import nu.danielsundberg.yakutia.application.service.PlayerApi;
import nu.danielsundberg.yakutia.application.service.iface.PreGameInterface;
import nu.danielsundberg.yakutia.application.service.exceptions.PlayerAlreadyExists;
import nu.danielsundberg.yakutia.entity.Player;

import java.util.ArrayList;
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
    public void invitePlayerToGame(long playerId, long gameId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Long> getInvites(long playerId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void acceptInvite(long playerId, long gameId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void declineInvite(long playerId, long gameId) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Player> getPlayers() {
        return new ArrayList<Player>();
    }

    @Override
    public String getPlayerByEmail(String email) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PlayerApi getPlayerByName(String name) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PlayerApi getPlayerById(long id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
