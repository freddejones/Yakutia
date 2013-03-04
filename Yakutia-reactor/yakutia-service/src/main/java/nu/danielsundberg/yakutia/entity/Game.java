package nu.danielsundberg.yakutia.entity;

import javax.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

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

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }
}
