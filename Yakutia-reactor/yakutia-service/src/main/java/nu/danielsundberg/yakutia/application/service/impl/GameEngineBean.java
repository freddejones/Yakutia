package nu.danielsundberg.yakutia.application.service.impl;

import java.util.Set;

import javassist.NotFoundException;

import javax.ejb.Stateless;

import nu.danielsundberg.yakutia.application.service.GameEngineInterface;


@Stateless
public class GameEngineBean implements GameEngineInterface {

	@Override
	public void startNewGame(Set<Long> playerIds) throws NotFoundException {
		
		if (playerIds.size() <= 1) {
			throw new NotFoundException("Could not find more than one player");
		}
		
		// scramble up the game: 
		// => order of turns
		// => set land area to players
		
		// Should this be saved to db?
		// how to store the order?
		
	}

	@Override
	public void startTurn() {
		
	}

	@Override
	public void attack() {
		
	}

	@Override
	public void endTurn() {
		// current player ends its turn
	}

	@Override
	public void endGame() {
		
		// select whos winner?
		// boolean to decide if game is over?
		
	}
	
}
