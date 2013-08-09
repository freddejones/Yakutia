package nu.danielsundberg.yakutia.application.service.impl;

import nu.danielsundberg.yakutia.application.service.iface.FriendManagerInterface;
import nu.danielsundberg.yakutia.entity.Player;
import nu.danielsundberg.yakutia.entity.PlayerFriend;
import nu.danielsundberg.yakutia.entity.statuses.FriendStatus;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * User: Fredde
 * Date: 7/7/13 3:56 PM
 */
@Stateless(mappedName = "friendManagerBean")
public class FriendManagerBean implements FriendManagerInterface {

    @PersistenceContext(name = "yakutiaPU")
    protected EntityManager em;

    @Override
    public Set<Player> getFriends(Player player) {
        Set<Player> friends = new HashSet<Player>();
        player = em.find(Player.class, player.getPlayerId());
        em.refresh(player);
        Iterator<PlayerFriend> playerFriendIter = player.getFriends().iterator();
        while(playerFriendIter.hasNext()) {
            PlayerFriend playerFriend = playerFriendIter.next();
            if (playerFriend.getFriendStatus() == FriendStatus.ACCEPTED) {
                Player friend = em.find(Player.class, playerFriend.getFriend().getPlayerId());
                friends.add(friend);
            }
        }
        return friends;
    }

    @Override
    public Set<Player> getAllNonFriendPlayers(Player player) {
        List<Player> playersList = (List<Player>)em.createNamedQuery("Player.getAllPlayers").getResultList();
        List<Player> playersListCopy = new ArrayList<Player>(playersList);

        player = em.find(Player.class, player.getPlayerId());
//        em.refresh(player);
        if (playersList.contains(player))
        { playersListCopy.remove(player); }

        for (Player p : playersList) {
            Iterator<PlayerFriend> iterPlayerFriend = p.getFriendsReqested().iterator();
            while(iterPlayerFriend.hasNext()) {
                PlayerFriend pf = iterPlayerFriend.next();
                if (pf.getFriendStatus() == FriendStatus.ACCEPTED
                        || pf.getFriendStatus() == FriendStatus.INVITED) {
                    playersListCopy.remove(p);
                }
            }
        }

        Set<Player> nonFriendPlayers = new HashSet<Player>(playersListCopy);
        return nonFriendPlayers;
    }

    @Override
    public Set<Player> getAllInvites(Player player) {
        List<PlayerFriend> pfList = em.createNamedQuery("PlayerFriend.getInvitesForFriend")
                .setParameter("fid",player.getPlayerId()).getResultList();

        Set<Player> invitesFromPlayers = new HashSet<Player>();
        for (PlayerFriend pf : pfList) {
            invitesFromPlayers.add(pf.getPlayer());
        }

        return invitesFromPlayers;
    }

    @Override
    public void sendInvite(Player fromPlayer, Player toPlayer) {
        PlayerFriend pf = new PlayerFriend();
        pf.setFriendStatus(FriendStatus.INVITED);

        fromPlayer = em.find(Player.class, fromPlayer.getPlayerId());
        toPlayer = em.find(Player.class, toPlayer.getPlayerId());
        em.refresh(fromPlayer);
        em.refresh(toPlayer);

        pf.setPlayer(fromPlayer);
        pf.setFriend(toPlayer);
        em.persist(pf);
    }

    @Override
    public void acceptInvite(Player playerToAcceptRelation, Player friendThatInvited) {

        playerToAcceptRelation = em.find(Player.class, playerToAcceptRelation.getPlayerId());
        friendThatInvited = em.find(Player.class, friendThatInvited.getPlayerId());

        em.refresh(playerToAcceptRelation);
        em.refresh(friendThatInvited);
        PlayerFriend playerFriendToAccept;
        Iterator<PlayerFriend> pfIterToAccept = playerToAcceptRelation.getFriendsReqested().iterator();
        while(pfIterToAccept.hasNext()) {
            playerFriendToAccept = pfIterToAccept.next();
            if (playerFriendToAccept.getFriendStatus() == FriendStatus.INVITED) {
                playerFriendToAccept.setFriendStatus(FriendStatus.ACCEPTED);
                em.merge(playerFriendToAccept);

                // Create the other leg as well for the relationship
                em.refresh(playerToAcceptRelation);
                em.refresh(friendThatInvited);
                PlayerFriend automaticNewLeg = new PlayerFriend();
                automaticNewLeg.setFriendStatus(FriendStatus.ACCEPTED);
                automaticNewLeg.setPlayer(playerToAcceptRelation);
                automaticNewLeg.setFriend(friendThatInvited);
                em.persist(automaticNewLeg);
            }
        }
    }

    @Override
    public void declineInvite(Player player, Player friend) {

    }

}
