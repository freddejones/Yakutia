package nu.danielsundberg.yakutia.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class LandArea {

    private int id;
    private Set<LandArea> neighbours;
//    private Set<Unit> units;

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    @OneToMany
//    public Set<Unit> getUnits() {
//        return units;
//    }
//
//    public void setUnits(Set<Unit> units) {
//        this.units = units;
//    }

    @OneToMany
    public Set<LandArea> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(Set<LandArea> neighbours) {
        this.neighbours = neighbours;
    }


}
