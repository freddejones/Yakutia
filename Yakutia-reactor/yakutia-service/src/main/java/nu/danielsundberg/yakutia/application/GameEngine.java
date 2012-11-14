package nu.danielsundberg.yakutia.application;

import javax.ejb.Remote;

@Remote
public interface GameEngine {

	public void startNewGame();
	
	public void startTurn();
	
	public void attack();
	
	// intelligence
	// harvest resources
	
	public void endTurn();
	
	public void endGame(); 
	
}
