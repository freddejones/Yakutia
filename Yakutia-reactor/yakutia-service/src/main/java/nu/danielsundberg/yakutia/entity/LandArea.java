package nu.danielsundberg.yakutia.entity;

import javax.persistence.*;
import java.util.Iterator;
import java.util.Set;

@Entity
@NamedQuery(name = "LandArea.getLandByName",
query = "SELECT la FROM LandArea la WHERE la.name =:name")
public class LandArea {

    private int landAreaId;
    private String name;
    private Set<LandArea> neighbours;
//    private Set<Unit> units;

    @Id
    @GeneratedValue
    public int getLandAreaId() {
        return landAreaId;
    }

    public void setLandAreaId(int landAreaId) {
        this.landAreaId = landAreaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isNeighbours(LandArea checkLand) {
        Iterator<LandArea> landAreaIterator = neighbours.iterator();

        while(landAreaIterator.hasNext()) {
            LandArea landArea = landAreaIterator.next();
            if (landArea.getName().equals(checkLand.getName())) {
                return true;
            }
        }
        return false;
    }
}
