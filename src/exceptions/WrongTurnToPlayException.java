package exceptions;

public class WrongTurnToPlayException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5669062515556187585L;
	private String currentTurn;

	public WrongTurnToPlayException(String currentTurn) {
		super();
		this.currentTurn = currentTurn;
	}

	@Override
	public String toString() {
		String msg = "ERROR: It is "+currentTurn+ "'s turn. Please wait your turn to make action.\n";
		return msg;
	}
	
	
}
