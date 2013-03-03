package nu.danielsundberg.yakutia;

import nu.danielsundberg.yakutia.exceptions.PlayerAlreadyExists;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface GameEngineInterface {

    public long createNewGame(long playerId);

    public void startNewGame(List<GameplayerId> gamePlayers);

    // fix game object
    //public boolean isGameFinished();

    // fix game object
    //public void endGame();
}
