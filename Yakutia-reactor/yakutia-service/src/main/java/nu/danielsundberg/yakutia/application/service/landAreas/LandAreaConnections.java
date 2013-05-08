package nu.danielsundberg.yakutia.application.service.landAreas;

public class LandAreaConnections {


    /**
     * Connection rules for landAreas
     * @param from
     * @param to
     * @return
     */
    public static boolean isConnected(LandArea from, LandArea to){

        switch (from) {
            case SVERIGE:
                if (to == LandArea.NORGE || to == LandArea.FINLAND)
                    return true;
                break;
            case FINLAND:
                if (to == LandArea.SVERIGE)
                    return true;
                break;
            case NORGE:
                if (to == LandArea.SVERIGE)
                    return true;
                break;
        }

        return false;
    }


}
