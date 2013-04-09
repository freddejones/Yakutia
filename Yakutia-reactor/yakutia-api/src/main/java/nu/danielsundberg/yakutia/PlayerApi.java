package nu.danielsundberg.yakutia;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * User: Fredde
 * Date: 4/4/13 9:18 PM
 */
@XmlRootElement
public class PlayerApi implements Serializable {

    private String playerName;
    private Long playerId;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
}
