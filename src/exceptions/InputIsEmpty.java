package exceptions;

public class InputIsEmpty extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4083996251947201867L;

	@Override
	public String toString() {
		String msg = "ERROR: The input is empty, please enter a letter from English Alphabet.\n"
				+ "If want to pass this turn,please press the \"Pass\" button.";
		return (msg);
	}
}
