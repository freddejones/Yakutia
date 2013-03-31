package nu.danielsundberg.yakutia.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
//import org.joda.time.DateTime;

@Entity
public class Game implements Serializable {

    // TODO add name of game


    @Id @GeneratedValue
    private long gameId;

    @OneToMany(mappedBy = "game")
    private Set<GamePlayer> players;

    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startedTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date finshedTime;

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

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getStartedTime() {
        return startedTime;
    }

    public void setStartedTime(Date startedTime) {
        this.startedTime = startedTime;
    }

    public Date getFinshedTime() {
        return finshedTime;
    }

    public void setFinshedTime(Date finshedTime) {
        this.finshedTime = finshedTime;
    }
}
