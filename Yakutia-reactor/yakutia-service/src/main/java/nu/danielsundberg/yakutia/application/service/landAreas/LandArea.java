package nu.danielsundberg.yakutia.application.service.landAreas;


public enum LandArea {
    NORGE ("NORGE"),
    SVERIGE ("SVERIGE"),
    FINLAND ("FINLAND"),
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
