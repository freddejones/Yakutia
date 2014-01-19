package se.freddejones.game.yakutia.model.dto;

public class PlaceUnitUpdate {

    private int numberOfUnits;
    private String landArea;

    public int getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(int numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public String getLandArea() {
        return landArea;
    }

    public void setLandArea(String landArea) {
        this.landArea = landArea;
    }
}
