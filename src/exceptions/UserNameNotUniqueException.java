package exceptions;

public class UserNameNotUniqueException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3067898382741873933L;
	private String userName;

	public UserNameNotUniqueException(String userName) {
		super();
		this.userName = userName;
	}

	@Override
	public String toString() {
		String message ="ERROR: The entered user name \"" + userName + "\" already been taken! "
				+ "\nTry some other user name.";
		
		return (message);
	}
	
	
}
