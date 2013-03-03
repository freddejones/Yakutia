package nu.danielsundberg.yakutia.landAreas;


public enum LandArea {
    NORGE ("NORGE"),
    SVERIGE ("SVERIGE"),
    FINLAND ("FINLAND");

    private String landArea;

    private LandArea(String landArea) {
        this.landArea = landArea;
    }

    @Override
    public String toString() {
        return landArea;
    }
}
