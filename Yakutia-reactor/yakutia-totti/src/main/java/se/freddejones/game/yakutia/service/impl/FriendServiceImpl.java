package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.PlayerDao;
import se.freddejones.game.yakutia.dao.PlayerFriendDao;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.entity.PlayerFriend;
import se.freddejones.game.yakutia.model.dto.FriendDTO;
import se.freddejones.game.yakutia.model.statuses.FriendStatus;
import se.freddejones.game.yakutia.service.FriendService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service("friendservice")
@Transactional(readOnly = true)
public class FriendServiceImpl implements FriendService {

    @Autowired
    PlayerDao playerDao;

    @Autowired
    PlayerFriendDao playerFriendDao;

    @Override
    @Transactional(readOnly = false)
    public void inviteFriend(Long playerId, Long playerToFriendInvite) {
        Player player = playerDao.getPlayerById(playerId);
        Player friendToInvite = playerDao.getPlayerById(playerToFriendInvite);
        PlayerFriend pf = new PlayerFriend();
        pf.setFriendStatus(FriendStatus.INVITED);
        pf.setPlayer(player);
        pf.setFriend(friendToInvite);
        playerFriendDao.persistPlayerFriendEntity(pf);
    }

    @Override
    public List<Player> getFriendInvites(Long playerId) {
        List<Player> invitedFriends = new ArrayList<Player>();
        Player p = playerDao.getPlayerById(playerId);
        final Set<PlayerFriend> friends = p.getFriendsReqested();
        Iterator<PlayerFriend> playerFriendIterator = friends.iterator();
        while(playerFriendIterator.hasNext()) {
            PlayerFriend playerFriend = playerFriendIterator.next();
            if (playerFriend.getFriendStatus() == FriendStatus.INVITED) {
                invitedFriends.add(playerFriend.getPlayer());
            }
        }
        return invitedFriends;
    }

    @Override
    public List<Player> getFriends(Long playerId) {
        List<Player> friendsAccepted = new ArrayList<Player>();
        Player p = playerDao.getPlayerById(playerId);
        final Set<PlayerFriend> friends = p.getFriends();
        Iterator<PlayerFriend> playerFriendIterator = friends.iterator();
        while(playerFriendIterator.hasNext()) {
            PlayerFriend playerFriend = playerFriendIterator.next();
            if (playerFriend.getFriendStatus() == FriendStatus.ACCEPTED) {
                friendsAccepted.add(playerFriend.getFriend());
            }
        }
        return friendsAccepted;
    }

    @Override
    @Transactional(readOnly = false)
    public FriendDTO acceptFriendInvite(Long invitedPlayer, Long playerWhoInvited) {
        // TODO clean this method up (extract it)
        FriendDTO friendDTO = new FriendDTO();
        PlayerFriend playerFriend = playerFriendDao.getPlayerFriend(playerWhoInvited, invitedPlayer);
        playerFriend.setFriendStatus(FriendStatus.ACCEPTED);
        playerFriendDao.mergePlayerFriendEntity(playerFriend);
        friendDTO.setFriendStatus(FriendStatus.ACCEPTED);
        friendDTO.setPlayerName(playerFriend.getPlayer().getName());
        friendDTO.setFriendId(playerWhoInvited);

        PlayerFriend playerFriendReplicateLeg = new PlayerFriend();
        playerFriendReplicateLeg.setFriend(playerFriend.getPlayer());
        playerFriendReplicateLeg.setPlayer(playerFriend.getFriend());
        playerFriendReplicateLeg.setFriendStatus(FriendStatus.ACCEPTED);
        playerFriendDao.persistPlayerFriendEntity(playerFriendReplicateLeg);
        return friendDTO;
    }

    @Override
    @Transactional(readOnly = false)
    public void declineFriendInvite(Long playerId, Long friendId) {
        PlayerFriend playerFriend = playerFriendDao.getPlayerFriend(playerId, friendId);
        playerFriendDao.deletePlayerFriend(playerFriend);
    }


}
