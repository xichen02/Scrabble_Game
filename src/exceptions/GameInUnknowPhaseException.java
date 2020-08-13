package exceptions;

public class GameInUnknowPhaseException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6353051798157593418L;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ERROR: The Game handler can't handle the request due to in invalid phase.\n";
	}
	
}
