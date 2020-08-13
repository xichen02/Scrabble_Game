package exceptions;

import scrabbleGame.PlayerAction;

public class GameNotCreatedException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4267522019057816942L;
	private PlayerAction action;
	
	
	public GameNotCreatedException(PlayerAction action) {
		super();
		this.action = action;
	}


	@Override
	public String toString() {
		String msg = String.format("ERROR: Game hasn't been created yet. Action \"%s\" is invalid. "
				+ "Plase ask game owner to \"CREATE_GAME\" first.\n", 
				action);
		
		return msg;
	}
	
}
