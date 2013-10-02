package se.freddejones.yakutia.application.service.iface;


import se.freddejones.yakutia.application.service.exceptions.NoPlayerFoundException;
import se.freddejones.yakutia.entity.Game;
import se.freddejones.yakutia.application.service.exceptions.PlayerAlreadyExists;
import se.freddejones.yakutia.entity.Player;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface PreGameInterface {

    boolean playerExists(String email);

    long createNewPlayer(String name, String email) throws PlayerAlreadyExists;

    long createNewGame(long playerId, String name);

    Game getGameById(long gameId);

    void invitePlayerToGame(long playerId, long gameId);

    List<Long> getInvites(long playerId);

    boolean acceptInvite(long playerId, long gameId);

    boolean declineInvite(long playerId, long gameId);

    List<Player> getPlayers();

    Player getPlayerByName(String name) throws NoPlayerFoundException;

    Player getPlayerById(long id) throws NoPlayerFoundException;

    Player getPlayerByEmail(String email) throws  NoPlayerFoundException;

    void deletePlayerById(long id);
}
