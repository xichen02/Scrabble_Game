package exceptions;

public class NoRightToStartGameException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9082276073460416498L;

	@Override
	public String toString() {
		String msg = "ERROR: Only the owner of this game has the right to start the game.\n";
		return msg;
	}
	
}
