package nu.danielsundberg.yakutia.application.service.iface;


import nu.danielsundberg.yakutia.application.service.PlayerApi;
import nu.danielsundberg.yakutia.application.service.exceptions.PlayerAlreadyExists;
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

    public void acceptInvite(long playerId, long gameId);

    public void declineInvite(long playerId, long gameId);

    public List<Player> getPlayers();

    public String getPlayerByEmail(String email);

    public PlayerApi getPlayerByName(String name);

    public PlayerApi getPlayerById(long id);

    //public void deleteAllPlayers();


}
