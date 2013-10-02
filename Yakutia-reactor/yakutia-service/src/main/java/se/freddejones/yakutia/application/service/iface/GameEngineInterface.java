package se.freddejones.yakutia.application.service.iface;

import se.freddejones.yakutia.application.service.exceptions.NotEnoughPlayers;

import javax.ejb.Remote;

@Remote
public interface GameEngineInterface {


    void startNewGame(long gameId) throws NotEnoughPlayers;

    boolean isGameFinished(long gameId);

    void endGame(long gameId);

    boolean isGameStarted(long gameId);

    // TODO add logic here like (nr of reinforcements and bleh game rules)
}
