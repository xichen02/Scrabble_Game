package scrabbleGame;

import java.util.ArrayList;

import exceptions.AllPlayerPassedException;
import exceptions.GameBoardIsFullException;
import exceptions.PlayerLogOutException;

public class ScrabbleRules {
	public int calcScore(String word) {
		return word.length();
	}
	
	/**
	 * Terminate conditions for this game:
	 * <p>Rule 1: When one player logged out the game should end.
	 * <p>Rule 2: When all player press pass the game end.
	 * <p>Rule 3: When no more free cell in the board, the game end.
	 * @param playerList
	 * @param board
	 * @throws GameBoardIsFullException
	 * @throws PlayerLogOutException
	 * @throws AllPlayerPassedException
	 */
	public void gameTerminate(ArrayList<Player> playerList,ScrabbleBoard board) throws 
	GameBoardIsFullException, PlayerLogOutException, AllPlayerPassedException {
		int totalPlayer = playerList.size();
		int totalPass = 0;
		
		for (Player player : playerList) {
			switch (player.getLastAction()) {
				//Rule 1: When one player logged out the game should end
				case LOGOUT:
					throw new PlayerLogOutException(player.getUserName());
					
				//trace number of player passed the turn
				case PASS_TURN:
					totalPass++;
					break;		
					
				default :
					break;		
			}
		}
		
		//Rule 2: When all player press pass the game end
		if (totalPass == totalPlayer) {
			throw new AllPlayerPassedException(totalPlayer);
		}
		
		//Rule 3: When no more free cell in the board, the game end
		if (board.isBoardIsFull()) {
			throw new GameBoardIsFullException();
		}	
	}
	
}
