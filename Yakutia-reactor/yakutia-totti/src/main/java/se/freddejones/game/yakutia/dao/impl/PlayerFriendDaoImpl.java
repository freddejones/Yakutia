package se.freddejones.game.yakutia.dao.impl;

import org.springframework.stereotype.Repository;
import se.freddejones.game.yakutia.dao.PlayerFriendDao;
import se.freddejones.game.yakutia.entity.PlayerFriend;

@Repository
public class PlayerFriendDaoImpl extends AbstractDaoImpl implements PlayerFriendDao {

    @Override
    public void persistPlayerFriendEntity(PlayerFriend playerFriend) {
        getCurrentSession().saveOrUpdate(playerFriend);
    }
}
