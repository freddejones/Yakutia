package nu.danielsundberg.yakutia;

import javax.ejb.Remote;

@Remote
public interface GameEngineInterface {

    public long createNewGame(long playerId);

    public void startNewGame(long gameId);

    // fix game object
    //public boolean isGameFinished();

    // fix game object
    //public void endGame();


    // TODO add some check that number of players have been reached in gameEngineBEAN
    // Like maximum number of players
}
