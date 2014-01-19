package se.freddejones.game.yakutia.model;


public enum LandArea {
    NORWAY("NORWAY"),
    SWEDEN("SWEDEN"),
    FINLAND("FINLAND"),
    UNASSIGNEDLAND("UNASSIGNEDLAND");

    private String landArea;

    private LandArea(String landArea) {
        this.landArea = landArea;
    }

    @Override
    public String toString() {
        return landArea;
    }

    public static LandArea translateLandArea(String landArea) {
        if (landArea.equals(NORWAY.toString())) {
            return NORWAY;
        } else if (landArea.equals(FINLAND.toString())) {
            return FINLAND;
        } else if (landArea.equals(SWEDEN.toString())) {
            return SWEDEN;
        } else if (landArea.equals(UNASSIGNEDLAND.toString())) {
            return UNASSIGNEDLAND;
        }

        return null;
    }
}
