package se.freddejones.game.yakutia.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import se.freddejones.game.yakutia.entity.Player;

@Repository
public class PlayerDaoImpl implements PlayerDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void createPlayer(Player p) {
        sessionFactory.getCurrentSession().saveOrUpdate(p);
    }
}
