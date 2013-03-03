package nu.danielsundberg.yakutia.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@NamedQueries(
        @NamedQuery(name = "Player.findPlayerByName",
        query = "SELECT p FROM Player p WHERE name=:pName")
)
public class Player implements Serializable {

    private long playerId;
    private String name;
    private Set<GamePlayer> games;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "player")
    public Set<GamePlayer> getGames() {
        return games;
    }

    public void setGames(Set<GamePlayer> games) {
        this.games = games;
    }
}
