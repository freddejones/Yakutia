package nu.danielsundberg.yakutia;


import nu.danielsundberg.yakutia.exceptions.PlayerAlreadyExists;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface PreGameInterface {

    public boolean playerExists(String email);

    public long createNewPlayer(String name, String email) throws PlayerAlreadyExists;

    public void invitePlayerToGame(String playerName, long gameId);

    public List<Long> getInvites(long playerId);

    public void acceptInvite(long playerId, long gameId);

    public void declineInvite(long playerId, long gameId);

    public List<String> getPlayers();

    public void deleteAllPlayers();


}
