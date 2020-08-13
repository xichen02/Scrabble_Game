package exceptions;

public class InputMoreThanOneLetter extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8937726375317859917L;
	private String input;
		
	public InputMoreThanOneLetter(String input) {
		super();
		this.input = input;
	}


	@Override
	public String toString() {
		String msg = "ERROR: Please enter only one letter!\nYou have entered \""
				+ input + "\",try enter \"" + input.charAt(0) +" \".";
		
		return msg;
	}
}
