package nu.danielsundberg.yakutia.entity;

import javax.persistence.*;

@Entity
public class Unit {

    @Id @GeneratedValue
	private int id;
    private int health;
	private int level;
    private String description;
    private UnitType typeOfUnit;
    private boolean movable;


    public boolean isMovable() {
        return movable;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    @Enumerated(EnumType.STRING)
    public UnitType getTypeOfUnit() {
        return typeOfUnit;
    }

    public void setTypeOfUnit(UnitType typeOfUnit) {
        this.typeOfUnit = typeOfUnit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
