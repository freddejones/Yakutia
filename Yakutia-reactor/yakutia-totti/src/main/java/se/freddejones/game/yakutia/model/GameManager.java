package se.freddejones.game.yakutia.model;

import java.util.ArrayList;
import java.util.List;

public class GameManager {

    public static List<LandArea> getLandAreas() {
        List<LandArea> landAreas = new ArrayList<LandArea>();
        landAreas.add(LandArea.SWEDEN);
        landAreas.add(LandArea.FINLAND);
        landAreas.add(LandArea.NORWAY);
        return landAreas;
    }

}
