package scrabbleGame;

import exceptions.GameNotCreatedException;
import exceptions.NoGameDataCreatedException;
import exceptions.UserNotExistException;

public class ScrabbleGameLobbyController {
	//TOBE Extend to ArrayList to simulate muti-server
	private ScrabbleGameLobbyInfo gameLobbyData;
	private ScrabbleGameLobby gameLobby;
	
	
	public ScrabbleGameLobbyController(String config) {
		super();
		gameLobby = new ScrabbleGameLobby(config);
		gameLobbyData = new ScrabbleGameLobbyInfo();
	}


	/**
	 * Handle player actions like: create game, Logout, Pass turn, Start Game, Vote
	 * @param player
	 * @param action
	 * @throws Exception 
	 */
	public void performAction(String playerName, PlayerAction action) throws Exception {
		gameLobby.performAction(playerName, action);
	}

	
	/**
	 * Handle player actions like: join room
	 * @param player
	 * @param action
	 * @param roomNm
	 * @throws Exception 
	 */
	public void performAction(String playerName, PlayerAction action, int roomNm) throws Exception {
		gameLobby.performAction(playerName, action, roomNm);
	}

	
	/**
	 * Handle player actions like: chat
	 * @param player
	 * @param action
	 * @param msg
	 * @throws Exception 
	 */
	public void performAction(String playerName, PlayerAction action, String msg) throws Exception {
		gameLobby.performAction(playerName, action, msg);
	}
	
	/**
	 * Handle player actions like: invite
	 * @param player
	 * @param player2
	 * @param action
	 * @throws Exception 
	 */
	public void performAction(String playerName, String playerName2, PlayerAction action) throws Exception {
		gameLobby.performAction(playerName, playerName2, action);
	}
	
	/**
	 * Handle all other normal game actions
	 * @param player
	 * @param action
	 * @param row
	 * @param col
	 * @param letter
	 * @throws Exception 
	 */
	public void performAction(String playerName, PlayerAction action, String msg, int roomNm,
			int row, int col, String letter) throws Exception {
		gameLobby.performAction(playerName, action, msg, roomNm, row, col, letter, null);
	}
	
	public ScrabbleGameLobbyInfo getGameLobbyData() {
		gameLobbyData.setChatLog(gameLobby.getChatLog());
		gameLobbyData.setPlayerActivitiesLog(gameLobby.getPlayerActivityLog());
		gameLobbyData.setRoomActivityLog(gameLobby.getRoomActivityLog());
		gameLobbyData.setPlayerList(gameLobby.getPlayerList());
		gameLobbyData.setRoomList(gameLobby.getRoomList());
		gameLobbyData.setInviteList(gameLobby.getInviteList());
		return gameLobbyData;
	}
	
	//for test propuse
	public String getFreePlayers() {
		String freeList = new String("Avilable players:\n\n");
		for (Player player : gameLobby.getFreePlayerList())	freeList+= player.getUserName()+"\n";
		return freeList+="\n";
	}
	
	//To passing the game data to players
	public ScrabbleGameController getSpecificGame(String playerName) throws 
	NoGameDataCreatedException, UserNotExistException, GameNotCreatedException {
		return gameLobby.findGame(playerName);
	}
}
