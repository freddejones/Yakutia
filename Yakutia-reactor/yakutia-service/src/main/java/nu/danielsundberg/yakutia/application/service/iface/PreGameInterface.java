package nu.danielsundberg.yakutia.application.service.iface;


import nu.danielsundberg.yakutia.application.service.exceptions.NoPlayerFoundException;
import nu.danielsundberg.yakutia.application.service.exceptions.PlayerAlreadyExists;
import nu.danielsundberg.yakutia.entity.Game;
import nu.danielsundberg.yakutia.entity.Player;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface PreGameInterface {

    public boolean playerExists(String email);

    public long createNewPlayer(String name, String email) throws PlayerAlreadyExists;

    public long createNewGame(long playerId);

    public void invitePlayerToGame(long playerId, long gameId);

    public List<Long> getInvites(long playerId);

    public boolean acceptInvite(long playerId, long gameId);

    public boolean declineInvite(long playerId, long gameId);

    public List<Player> getPlayers();

    public Player getPlayerByName(String name) throws NoPlayerFoundException;

    public Player getPlayerById(long id) throws NoPlayerFoundException;

    //public void deleteAllPlayers();


}
