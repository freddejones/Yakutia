package nu.danielsundberg.yakutia.entity;

public enum UnitType {
    TANK ("TANK"),
    AIR ("AIR"),
    SOLDIER ("SOLDIER");

    private String unitType;

    private UnitType(String unitType) {
        this.unitType = unitType;
    }

    @Override
    public String toString() {
        return unitType;
    }
}
