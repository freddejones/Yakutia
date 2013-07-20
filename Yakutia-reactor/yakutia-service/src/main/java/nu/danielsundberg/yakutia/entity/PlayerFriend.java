package nu.danielsundberg.yakutia.entity;

import nu.danielsundberg.yakutia.entity.statuses.FriendStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * User: Fredde
 * Date: 7/5/13 11:47 PM
 */
@Entity
@NamedQueries({
@NamedQuery(
        name = "PlayerFriend.getInvitesForFriend",
        query = "SELECT pf from PlayerFriend pf WHERE friendId=:fid " +
                "AND FRIENDSTATUS='INVITED'")
})
public class PlayerFriend implements Serializable {

    @Id
    @GeneratedValue
    private long playerFriendId;

    @ManyToOne
    @JoinColumn(name = "playerId")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "friendId")
    private Player friend;

    @Enumerated(EnumType.STRING)
    private FriendStatus friendStatus;

    public long getPlayerFriendId() {
        return playerFriendId;
    }

    public void setPlayerFriendId(long playerFriendId) {
        this.playerFriendId = playerFriendId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getFriend() {
        return friend;
    }

    public void setFriend(Player friend) {
        this.friend = friend;
    }

    public FriendStatus getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(FriendStatus friendStatus) {
        this.friendStatus = friendStatus;
    }
}
