package nu.danielsundberg.yakutia.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Set;

@Entity
public class LandArea {

    @Id
    private int id;

    private Set<LandArea> neighbours;     // tänker jag fel här?

}
