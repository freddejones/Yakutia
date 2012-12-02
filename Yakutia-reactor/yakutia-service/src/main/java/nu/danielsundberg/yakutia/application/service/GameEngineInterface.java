package nu.danielsundberg.yakutia.application.service;

import java.util.Set;

import javassist.NotFoundException;

import javax.ejb.Remote;

@Remote
public interface GameEngineInterface {

	// TODO fix a customized exception
	public void startNewGame(Set<Long> playerIds) throws NotFoundException;
	
	public void startTurn();
	
	public void attack();
	
	// intelligence
	// harvest resources
	
	public void endTurn();
	
	public void endGame(); 
	
}
