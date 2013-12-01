package se.freddejones.game.yakutia.model;

import java.io.Serializable;

public class YakutiaModel implements Serializable {

    private String landName;
    private int units;
    private boolean isOwnedByPlayer;

    public YakutiaModel(String landName, int units, boolean ownedByPlayer) {
        this.landName = landName;
        this.units = units;
        isOwnedByPlayer = ownedByPlayer;
    }

    public String getLandName() {
        return landName;
    }

    public void setLandName(String landName) {
        this.landName = landName;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public boolean isOwnedByPlayer() {
        return isOwnedByPlayer;
    }

    public void setOwnedByPlayer(boolean ownedByPlayer) {
        isOwnedByPlayer = ownedByPlayer;
    }
}
