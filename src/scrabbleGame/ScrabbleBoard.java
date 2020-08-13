package scrabbleGame;

import java.util.ArrayList;
import java.util.Arrays;

import exceptions.InputIsEmpty;
import exceptions.InputMoreThanOneLetter;
import exceptions.InputNotEnglishException;
import exceptions.NotWritingOnBoardExceptions;
import exceptions.WriteToNonEmptyCellException;

public class ScrabbleBoard { 
	private String[][] gameBoard;
	private int rows, columns;


	private boolean boardIsFull;
	
	/**
	 * Constructor that take argument form the configuration file
	 * @param rows
	 * @param columns
	 */
	public ScrabbleBoard(String rows, String columns) {
		this.rows = Integer.parseInt(rows);
		this.columns = Integer.parseInt(columns);
		this.gameBoard = new String[this.rows][this.columns];
		this.boardIsFull = false;
	}
	
	/**
	 * write a letter to a required cell at the gameBoard
	 * @param row
	 * @param col
	 * @param c
	 * @throws InputNotEnglishException
	 * @throws WriteToNonEmptyCellException
	 * @throws InputIsEmpty 
	 * @throws InputMoreThanOneLetter 
	 * @throws NotWritingOnBoardExceptions 
	 */
	public void writeToBoard(int row, int col, String input) throws Exception {
		
		//removing all empty space inside input string
		String inputWithoutSpace = input.replaceAll("\\s", "");
		
		//if user put empty input
		if ( inputWithoutSpace.isEmpty()) 
			throw new InputIsEmpty();
		
		//take first inputed letter
		char c = inputWithoutSpace.charAt(0);
		
		//if user write more than one letter to a single cell
		if(input.replaceAll("\\s", "").length() > 1) 
			throw new InputMoreThanOneLetter(inputWithoutSpace);
		
		//if a character is not of an English Alphabet
		if ( !(c >= 'a' && c <= 'z') && !(c >= 'A' && c <= 'Z')) 
			throw new InputNotEnglishException(c);
		//if somehow player write not on board...
		if((row < 0)||(row >= rows)||(col < 0)||(col >= columns)) 
			throw new NotWritingOnBoardExceptions(row, col, rows, columns);
				
		
		//if the GUI not restrict the writing area, and player write in a non empty cell
		if (gameBoard[row][col] != null) 
			throw new WriteToNonEmptyCellException(gameBoard[row][col]);
		
		
		
		//otherwise write this word to the gameboard
		gameBoard[row][col] = Character.toString(c);
		
		//check if the board is full after each word placement
		checkBoardIsFull();
	}
	
	
	/**
	 * check if the game board is full
	 */
	private void checkBoardIsFull() {
		boolean hasFreeSapce = false;
		
		//check if there are more free space left in the board
		outerLoop:
		for (String[] rows: gameBoard) {
			for(String cell : rows) {
				if (cell == null) {
					hasFreeSapce = true;
					break outerLoop;
				}
			}
		}
		
		boardIsFull = !hasFreeSapce;
	}
	
	/**
	 * This returns all words in given row and col
	 * @param row
	 * @param col
	 * @return
	 */
	public ArrayList<String> getNewFormedWords(int row, int col) {
		ArrayList<String> wordList = new ArrayList<String>();
		String verWord = "";
		String horWord = "";
		boolean isAtEdge = false;
		boolean isAtCorner = false;
		boolean isDuplicate = false;

		//find connected word in vertical direction
		//search first half of word
		for (int i = row; i >= 0 ; i--) {
			if (gameBoard[i][col] != null) {
				verWord += gameBoard[i][col];
			}else break;
		}
		//reverse the first part as we add from back
		verWord = new StringBuilder(verWord).reverse().toString();
		//now go to find second half
		for (int i = row + 1; i <rows; i++) {
			if ((i <rows) && (gameBoard[i][col] != null)) {
				verWord += gameBoard[i][col];
			} else break;
		}
		
		
		//find connected word in horizontal direction
		//search first half of word
		for (int i = col; i >= 0 ; i--) {
			if (gameBoard[row][i] != null) {
				horWord += gameBoard[row][i];
			}else break;
		}
		//reverse the first part as we add from back
		horWord = new StringBuilder(horWord).reverse().toString();
		//now go to find second half
		for (int i = col + 1; i <columns; i++) {
			if ((i <columns) && (gameBoard[row][i] != null)) {
				horWord += gameBoard[row][i];
			} else break;
		}
		
		
		//If a word is isolated from any other letter, it should only be scan once no matter 
		//vertically or horizontally 
		
		//check for isolated situation
		//if at corner
		if ((row == 0 && col == 0) || (row == 0 && col == (columns-1)) || 
				(row == (rows-1) && col == 0) || (row == (rows-1) && col == (columns-1))) 
			isAtCorner = true;
		//if at edge
		else if (row == 0 || row == (rows-1) || col ==0 || col == (columns-1))	isAtEdge = true;
		
		//check isolate 
		if (isAtCorner) {
			if (findNearByNulls(row,col) == 2)	isDuplicate = true;
		}
		else if (isAtEdge) {
			if (findNearByNulls(row,col) == 3)	isDuplicate = true;
		}
		else {
			if (findNearByNulls(row,col) == 4)	isDuplicate = true;
		}
		
		
		wordList.add(horWord);
		wordList.add(verWord);
		
		//remove duplicate scans
		if (isDuplicate) wordList.remove(gameBoard[row][col]); 
		return wordList;
	}
	
	private int findNearByNulls(int row, int col) {
		int counterNulls = 0;
		if((row-1) >= 0) {
			if (gameBoard[row-1][col] == null) counterNulls++;
		}
		if ((row+1) < rows) {
			if (gameBoard[row+1][col] == null) counterNulls++;
		}
		if((col-1) >= 0) {
			if (gameBoard[row][col-1] == null) counterNulls++;
		}
		if ((col+1) < columns) {
			if (gameBoard[row][col+1] == null) counterNulls++;
		}

		return counterNulls;
	}
	
	/**
	 * Override toString, which will represent whole gameboard
	 */
	@Override
	public String toString() {
		
		return(Arrays.deepToString(gameBoard).replace("null"," ").
				replace("], ", "|\n").replace("[[", "|").
				replace("]]", "|").replace("[", "|"));
	}

	public String[][] getBoard() {
		return gameBoard;
	}

	public boolean isBoardIsFull() {
		return boardIsFull;
	}
	
	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
	
	
}
