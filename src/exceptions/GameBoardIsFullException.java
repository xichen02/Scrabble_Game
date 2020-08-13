package exceptions;

public class GameBoardIsFullException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8114759437863087098L;

	@Override 
	public String toString() {
		return ("Warning: The game board is full. No more free cells.\n Game End! "
				+ "Wait Final Result.\n");
	}
}
