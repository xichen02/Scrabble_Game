package scrabbleGame;

import exceptions.GameAlreadyCreatedException;
import exceptions.GameNotCreatedException;
import exceptions.NoGameDataCreatedException;

/**
 * controller for the whole game, is the only object that will be accessed by RMI
 * @author Zhang Yuanlong
 *
 */
public class ScrabbleGameController {
	private int controllerID;
	private String configFile;
	private ScrabbleGame game;
	private ScrabbleGameInfo gameData;
	private boolean isTimeToCloseController;
	
	public ScrabbleGameController(String configFile, int id) {
		this.controllerID = id;
		this.configFile = configFile;
		this.gameData = new ScrabbleGameInfo();
		this.isTimeToCloseController = false;
	}
	
	public void performAction(String playerName, PlayerAction action) throws Exception{
		performAction(playerName, action, 0, 0, null);
	}
	
	public void performAction(String playerName, PlayerAction action,
			int row, int col, String letter) throws Exception{
		
		//the same game can't be repeatedly created
		if (action == PlayerAction.CREATE_GAME) {
			if (game != null) throw new GameAlreadyCreatedException();
			game = new ScrabbleGame(configFile, playerName);
		}	
		//if any action want to perform before the game start
		else {
			if (game == null) throw new GameNotCreatedException(action);
		}
		
		//invite players inside lobby
		if (action == PlayerAction.INVITE_PLAYER) {
			//TODO
		}
		
		game.playerActionHandler(playerName, action, row, col, letter);
	}
	
	public boolean compareID(int id) {
		return id==controllerID;
	}
	
	public ScrabbleGameInfo getGameData() throws NoGameDataCreatedException, GameNotCreatedException {
		if (gameData == null) throw new NoGameDataCreatedException();
		if (game == null) throw new GameNotCreatedException(null);
		gameData.setGameBoard(game.getGameBoard().getBoard());
		gameData.setRows(game.getGameBoard().getRows());
		gameData.setColums(game.getGameBoard().getColumns());
		gameData.setPlayerRank(game.playerRank());
		gameData.setCurrentTurn(game.getPlayerTurn());
		gameData.setCurrentPhase(game.getGamePhase().toString());
		gameData.setGameLog(game.getGameLog());
		gameData.setGameEnd(game.isGameEnd());
		gameData.setTimeToVote(game.isTimeToVote());
		gameData.setVotedWord(game.getVotedWord());
		gameData.setPlayerList(game.getPlayerList());
		
		//update game controller's info
		isTimeToCloseController = game.isGameClose();
		return gameData;
	}

	public boolean isTimeToCloseController() {
		return isTimeToCloseController;
	}

	public int getControllerID() {
		return controllerID;
	}

	@Override
	public String toString() {
		return String.format("%s's room: ID %d. (%s)\n", 
				game.getGameOwner(), controllerID, game.getGamePhase());
	}
	
	
	
}
