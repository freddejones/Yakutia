package nu.danielsundberg.yakutia;


import nu.danielsundberg.yakutia.exceptions.PlayerAlreadyExists;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface PreGameInterface {

    public long createNewPlayer(String name) throws PlayerAlreadyExists;

    public void invitePlayerToGame(String playerName);

    //public List<Long> getInvites(GameplayerId player);

    // What for parameters
    public boolean acceptInvite(long playerId, long gameId);

    public List<String> getPlayers();

    public void deleteAllPlayers();


}
