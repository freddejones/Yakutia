package nu.danielsundberg.yakutia.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@NamedQueries(
        @NamedQuery(
                name = "quicky",
                query = "SELECT g FROM GamePlayer g"
        )
)
public class GamePlayer implements Serializable {

    @Id
    @GeneratedValue
    private long gamePlayerId;

    private long gameId;
    private long playerId;

    @ManyToOne
    @JoinColumn(name="gameId", insertable = false, updatable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "playerId", insertable = false, updatable = false)
    private Player player;

    @OneToMany(mappedBy = "gamePlayer")
    private Set<Unit> units;

    public long getGamePlayerId() {
        return gamePlayerId;
    }

    public void setGamePlayerId(long gamePlayerId) {
        this.gamePlayerId = gamePlayerId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Set<Unit> getUnits() {
        return units;
    }

    public void setUnits(Set<Unit> units) {
        this.units = units;
    }
}
