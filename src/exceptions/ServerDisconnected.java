package exceptions;

public class ServerDisconnected extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String error() {
		return "ERROR: The server is closed.";
	}
	

}
