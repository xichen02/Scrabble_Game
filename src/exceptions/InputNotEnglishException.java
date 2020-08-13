package exceptions;

public class InputNotEnglishException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7576573341761175215L;
	private char input;

	public InputNotEnglishException(char c) {
		super();
		this.input = c;
	}
	
	@Override 
	public String toString() {
		String message ="ERROR: The entered letter \"" + input + "\" is not a valid Input! "
				+ "\nTry input a character of English Alphabet" + "(a-z or A-Z).";
		
		return (message);
	}
}
