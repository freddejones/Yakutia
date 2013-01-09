package nu.danielsundberg.yakutia.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Player {

    private long playerId;
    private Set<LandArea> landAreas;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @OneToMany
    @Column(nullable = true)
    public Set<LandArea> getLandAreas() {
        return landAreas;
    }

    public void setLandAreas(Set<LandArea> landAreas) {
        this.landAreas = landAreas;
    }
}
