package nu.danielsundberg.yakutia.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "Player.findPlayerById",
                query = "SELECT p FROM Player p WHERE playerId=:pId"
        ),
        @NamedQuery(
                name = "Player.findPlayerByName",
                query = "SELECT p FROM Player p WHERE name=:pName"
        ),
        @NamedQuery(
                name = "Player.findPlayerBySearchName",
                query = "SELECT p FROM Player p WHERE p.name LIKE :pName"
        ),
        @NamedQuery(
                name = "Player.findPlayerBySearchEmail",
                query = "SELECT p FROM Player p WHERE p.email LIKE :pEmail"
        ),
        @NamedQuery(
                name = "Player.getAllPlayers",
                query = "SELECT p FROM Player p"
        )
})
public class Player implements Serializable {

    private long playerId;
    private String name;
    private String email;
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

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    public Set<GamePlayer> getGames() {
        return games;
    }

    public void setGames(Set<GamePlayer> games) {
        this.games = games;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
