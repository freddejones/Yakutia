package se.freddejones.game.yakutia.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.freddejones.game.yakutia.dao.PlayerDao;
import se.freddejones.game.yakutia.dao.PlayerFriendDao;
import se.freddejones.game.yakutia.entity.Player;
import se.freddejones.game.yakutia.entity.PlayerFriend;
import se.freddejones.game.yakutia.model.statuses.FriendStatus;
import se.freddejones.game.yakutia.service.FriendService;

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


}
