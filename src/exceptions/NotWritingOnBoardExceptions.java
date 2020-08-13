package exceptions;

public class NotWritingOnBoardExceptions extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6398815015221589754L;
	int row, col, maxRow, maxCol;

	public NotWritingOnBoardExceptions(int row, int col, int maxRow, int maxCol) {
		super();
		this.row = row;
		this.col = col;
		this.maxRow = maxRow;
		this.maxCol = maxCol;
	}

	@Override
	public String toString() {
		String msg = String.format("ERROR: Not Writing on the game board!.\n"
				+ "This game board size %d*%d. However, you want to write"
				+ " on cell (%d . %d).\nTry somewhere other on board.\n", maxRow, maxCol, row, col);
		return msg;
	}
	
	
}
