package nu.danielsundberg.yakutia;

import javax.ejb.Remote;

@Remote
public interface GameEngineInterface {

    public long createNewGame(long playerId);

    public void startNewGame(long gameId);

    public boolean isGameFinished(long gameId);

    //public void endGame();

    // TODO add some check that number of players have been reached in gameEngineBEAN
    // Like maximum number of players

    // TODO add logic here like (nr of reinforcements and other game rules)
}
