package exceptions;

public class AllPlayerPassedException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8481718118726438825L;
	private int totalPlayerNumber;

	public AllPlayerPassedException(int totalPass) {
		super();
		this.totalPlayerNumber = totalPass;
	}

	@Override
	public String toString() {
		String msg = "Warning: Total "+totalPlayerNumber+" players. All passed their turn.\nGame Ends!"
				+ " Wait Final Result.\n";
		return msg;
	}
	
	
	
}
