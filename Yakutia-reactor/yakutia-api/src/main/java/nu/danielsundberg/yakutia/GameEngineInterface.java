package nu.danielsundberg.yakutia;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface GameEngineInterface {

    public List<String> getPlayers();

    public long startNewGame(List<Long> playerIds);

    public void deleteAllPlayers();

    public long createNewPlayer(String name);

    public void addUnitsToGamePlayer(long gameId, long playerId);

}
