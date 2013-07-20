package nu.danielsundberg.yakutia.application.service.iface;

import nu.danielsundberg.yakutia.entity.Player;

import javax.ejb.Remote;
import java.util.Set;

/**
 * User: Fredde
 * Date: 7/7/13 1:00 AM
 */
@Remote
public interface FriendManagerInterface {

    public Set<Player> getFriends(Player player);

    public Set<Player> getAllNonFriendPlayers(Player player);

    public Set<Player> getAllInvites(Player player);

    public void sendInvite(Player player, Player friend);

    public void acceptInvite(Player player, Player friend);

    public void declineInvite(Player player, Player friend);

}
