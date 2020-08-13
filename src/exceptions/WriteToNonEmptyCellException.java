package exceptions;

public class WriteToNonEmptyCellException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8151412714828732526L;
	private String existingChar;

	public WriteToNonEmptyCellException(String gameBoard) {
		super();
		this.existingChar = gameBoard;
	}
	
	@Override
	public String toString() {
		String msg = "ERROR: \""+existingChar+"\" is already putted in this cell, please try other non-empty cells.\n";
		
		return (msg);
	}
}
