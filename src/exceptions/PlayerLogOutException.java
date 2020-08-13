package exceptions;

public class PlayerLogOutException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1504058385445632486L;
	private String playerName;

	public PlayerLogOutException(String playerName) {
		super();
		this.playerName = playerName;
	}

	@Override
	public String toString() {
		String msg = "Warning: Player < "+playerName + " > exited the game.\nGame Ends!"
				+ " Wait Final Result.\n";
		return msg;
	}
	
	
}
