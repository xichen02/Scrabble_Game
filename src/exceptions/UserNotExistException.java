package exceptions;

public class UserNotExistException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6363695101923764700L;
	String userName;

	public UserNotExistException(String userName) {
		super();
		this.userName = userName;
	}

	@Override
	public String toString() {
		String msg = "ERROR: Your userName("+userName+") has not been registed in this game.\n"
				+ "Please join the game first, maybe next turn.\n";
		return msg;
	}
	
	
}
