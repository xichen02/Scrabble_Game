package exceptions;

import scrabbleGame.GamePhase;
import scrabbleGame.PlayerAction;

public class InvalidActionInCurrentPhaseExcception extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3800738570799300598L;
	private GamePhase phase;
	private PlayerAction action;
	
	public InvalidActionInCurrentPhaseExcception(GamePhase phase, PlayerAction action) {
		super();
		this.phase = phase;
		this.action = action;
	}

	@Override
	public String toString() {
		String msg = "ERROR: This game is currently at: "+phase+" phase. Action: "+
				action+" is not allowed!\n";
		return msg;
	}
	
	
}
