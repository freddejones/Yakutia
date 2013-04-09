package nu.danielsundberg.yakutia.auth;

/**
 * User: Fredde
 * Date: 4/2/13 6:19 PM
 */
public class RestParameters {

    private static final String CORE_PATH = "http://localhost:8080/yakutia-services/";
    public static final String PLAYEREXIST_URL = CORE_PATH + "rest/player/playerExist";
    public static final String PLAYERBYEMAIL_URL = CORE_PATH + "rest/player/getPlayerByEmail";
    public static final String CREATEPLAYER_URL = CORE_PATH + "rest/player/createplayer";
    public static final String GETALLPLAYERS_URL = CORE_PATH + "rest/player/getAllPlayers";
}
