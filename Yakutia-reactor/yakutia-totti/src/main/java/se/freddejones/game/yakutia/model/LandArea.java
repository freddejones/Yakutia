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
}
