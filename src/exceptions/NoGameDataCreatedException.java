package exceptions;

public class NoGameDataCreatedException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2085995588707671977L;

	@Override
	public String toString() {
		return "No game data has generated yet, please start game and perform some actions first.\n";
	}
	
}
