package nu.danielsundberg.yakutia.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Game {

    @Id @GeneratedValue
    private long gameId;
    @OneToMany
    private Set<LandArea> gameBoard;
    @OneToMany
    private Set<Player> players;

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public Set<LandArea> getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(Set<LandArea> gameBoard) {
        this.gameBoard = gameBoard;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }
}
