package nu.danielsundberg.yakutia.application.service.iface;

import nu.danielsundberg.yakutia.application.service.exceptions.NotEnoughPlayers;

import javax.ejb.Remote;

@Remote
public interface GameEngineInterface {

    public long createNewGame(long playerId);

    public void startNewGame(long gameId) throws NotEnoughPlayers;

    public boolean isGameFinished(long gameId);

    public void endGame(long gameId);

    // TODO add some check that number of players have been reached in gameEngineBEAN
    // Like maximum number of players

    // TODO add logic here like (nr of reinforcements and other game rules)
}
