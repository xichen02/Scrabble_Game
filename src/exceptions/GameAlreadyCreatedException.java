package exceptions;

public class GameAlreadyCreatedException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5817123445730554078L;

	@Override
	public String toString() {
		return "ERROR: The Game already been created. Can't create the game.\n";
	}
	
}
