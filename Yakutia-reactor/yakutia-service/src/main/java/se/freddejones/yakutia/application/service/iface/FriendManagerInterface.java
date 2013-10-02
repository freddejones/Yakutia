package se.freddejones.yakutia.application.service.iface;

import se.freddejones.yakutia.entity.Player;

import javax.ejb.Remote;
import java.util.Set;

/**
 * User: Fredde
 * Date: 7/7/13 1:00 AM
 */
@Remote
public interface FriendManagerInterface {

    Set<Player> getFriends(Player player);

    Set<Player> getAllNonFriendPlayers(Player player);

    Set<Player> getAllInvites(Player player);

    void sendInvite(Player player, Player friend);

    void acceptInvite(Player player, Player friend);

    void declineInvite(Player player, Player friend);

}
