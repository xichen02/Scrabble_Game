package scrabbleGame;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is a class that stores simplified game data structure.
 * I use this to protect the game system from been view by client side
 * @author Zhang Yuanlong
 *
 */
public class ScrabbleGameInfo {
	private String playerRank;
	private String currentTurn;
	private String currentPhase;
	private String[][] gameBoard;
	private int rows,colums;
	private String gameLog;
	//helpful to implement invite function
	private ArrayList<Player> playerList;
	//votedWord represents the voted word in the current turn.
	private String votedWord;
	//when the isTimeToVote is turned on, client GUI need to pop up the voting 
	//window for the current votedWord
	private boolean isTimeToVote;
	//When the isGameEnd is turned on, which means the game has met one of its 
	//terminate conditions. In this case, client need to ask for final result
	//and represent it to the player and then disconnect from current game
	private boolean isGameEnd;
	
	
	public ScrabbleGameInfo() {
		super();
		this.playerRank = null;
		this.gameBoard = null;
		this.gameLog = null;
		this.votedWord = null;
		this.currentTurn = null;
		this.currentPhase = null;
		this.isTimeToVote = false;
		this.isGameEnd = false;
		this.playerList = new ArrayList<Player>();
		this.colums = 0;
		this.rows = 0;
	}

	public String getPlayerRank() {
		return playerRank;
	}

	public void setPlayerRank(String playerRank) {
		this.playerRank = playerRank;
	}

	public String[][] getGameBoard() {
		return gameBoard;
	}

	public void setGameBoard(String[][] gameBoard) {
		this.gameBoard = gameBoard;
	}

	public String getGameLog() {
		return gameLog;
	}

	public void setGameLog(String gameLog) {
		this.gameLog = gameLog;
	}
	
	public String getVotedWord() {
		return votedWord;
	}

	public void setVotedWord(String votedWord) {
		this.votedWord = votedWord;
	}

	public boolean isTimeToVote() {
		return isTimeToVote;
	}

	public void setTimeToVote(boolean isTimeToVote) {
		this.isTimeToVote = isTimeToVote;
	}

	public boolean isGameEnd() {
		return isGameEnd;
	}

	public void setGameEnd(boolean isGameEnd) {
		this.isGameEnd = isGameEnd;
	}

	/**
	 * which will print whole gameboard
	 */
	public String printGameBoard() {
		
		return(Arrays.deepToString(gameBoard).replace("null"," ").
				replace("], ", "|\n").replace("[[", "|").
				replace("]]", "|").replace("[", "|"));
	}

	@Override
	public String toString() {
		String msg = String.format("Game Data:\n"
				+ "playerRank = \n%s\n"
				+ "gameBoard = \n%s\n"
				+ "gameLog = %s\n"
				+ "votedWord = %s\n"
				+ "isTimeToVote = %s\n"
				+ "isGameEnd = %s\n" ,
				playerRank, printGameBoard(), gameLog, votedWord, isTimeToVote, isGameEnd);
		
		return msg;
	}

	public ArrayList<Player> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(ArrayList<Player> playerList) {
		this.playerList = playerList;
	}
	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColums() {
		return colums;
	}

	public void setColums(int colums) {
		this.colums = colums;
	}

	public String getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(String currentTurn) {
		this.currentTurn = currentTurn;
	}

	public String getCurrentPhase() {
		return currentPhase;
	}

	public void setCurrentPhase(String currentPhase) {
		this.currentPhase = currentPhase;
	}
	
}
