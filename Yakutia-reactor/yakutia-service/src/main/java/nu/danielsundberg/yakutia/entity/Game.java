package nu.danielsundberg.yakutia.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Game implements Serializable {

    // TODO add temporal values (for when created game etc
    // TODO add enum for state of game (STARTED, ONGOING, FINISHED)


    @Id @GeneratedValue
    private long gameId;

    @OneToMany(mappedBy = "game")
    private Set<GamePlayer> players;

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public Set<GamePlayer> getPlayers() {
        return players;
    }

    public void setPlayers(Set<GamePlayer> players) {
        this.players = players;
    }
}
