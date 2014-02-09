package se.freddejones.game.yakutia.model;

import java.util.ArrayList;
import java.util.List;

public class GameManager {

    public static List<LandArea> getLandAreas() {
        List<LandArea> landAreas = new ArrayList<LandArea>();
        landAreas.add(LandArea.SWEDEN);
        landAreas.add(LandArea.FINLAND);
        landAreas.add(LandArea.NORWAY);
        landAreas.add(LandArea.DENMARK);
        landAreas.add(LandArea.ICELAND);
        landAreas.add(LandArea.TYSKLAND);
        landAreas.add(LandArea.UKRAINA);
        landAreas.add(LandArea.SKAUNE);
        landAreas.add(LandArea.TOMTEBODA);
        return landAreas;
    }

    public static boolean isTerritoriesConnected(LandArea src, LandArea dst) {
        switch (src) {
            case SWEDEN:
                if (dst == LandArea.NORWAY || dst == LandArea.ICELAND) {
                    return true;
                } break;
            case NORWAY:
                if (dst == LandArea.SWEDEN || dst == LandArea.FINLAND
                        || dst == LandArea.TYSKLAND) {
                    return true;
                } break;
            case FINLAND:
                if (dst == LandArea.NORWAY || dst == LandArea.DENMARK
                    || dst == LandArea.UKRAINA) {
                    return true;
                } break;
            case DENMARK:
                if (dst == LandArea.FINLAND) {
                    return true;
                } break;
            case ICELAND:
                if (dst == LandArea.SWEDEN || dst == LandArea.SKAUNE
                        || dst == LandArea.TYSKLAND) {
                    return true;
                } break;
        }

        return false;
    }
}
