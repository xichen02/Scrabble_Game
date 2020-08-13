package scrabbleGame;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import exceptions.GameNotCreatedException;
import exceptions.UserNameNotUniqueException;
import exceptions.UserNotExistException;

public class ScrabbleGameLobby {
	private String Config;
	private ArrayList<ScrabbleGameController> gameList;
	private ArrayList<Player> playerList;
	private ArrayList<String> chatLog;
	private ArrayList<String> playerActivityLog;
	private ArrayList<String> roomActivityLog;
	//TODO: need to invite 
	private HashMap<String, String> inviteList;
	
	
	public ScrabbleGameLobby(String config) {
		super();
		Config = config;
		this.gameList = new ArrayList<ScrabbleGameController>();
		this.playerList = new ArrayList<Player>();
		this.chatLog = new ArrayList<String>();
		this.playerActivityLog = new ArrayList<String>();
		this.roomActivityLog = new ArrayList<String>();
		this.inviteList = new HashMap<String, String>();
	}


	/**
	 * Handle player actions like: create game, Logout, Pass turn, Start Game, Vote
	 * @param player
	 * @param action
	 * @throws Exception 
	 */
	public void performAction(String playerName, PlayerAction action) throws Exception {
		performAction(playerName, action, null, -1, -1, -1, null, null);
	}

	
	/**
	 * Handle player actions like: join room
	 * @param player
	 * @param action
	 * @param roomNm
	 * @throws Exception 
	 */
	public void performAction(String playerName, PlayerAction action, int roomNm) throws Exception {
		performAction(playerName, action, null, roomNm, -1, -1, null, null);
	}

	
	/**
	 * Handle player actions like: chat
	 * @param player
	 * @param action
	 * @param msg
	 * @throws Exception 
	 */
	public void performAction(String playerName, PlayerAction action, String msg) throws Exception {
		performAction(playerName, action, msg, -1, -1, -1, null, null);
	}
	
	/**
	 * Handle player actions like: invite
	 * @param player
	 * @param action
	 * @param msg
	 * @throws Exception 
	 */
	public void performAction(String playerName, String playerName2, PlayerAction action ) throws Exception {
		performAction(playerName, action, null, -1, -1, -1, null, playerName2);
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
			int row, int col, String letter, String playerName2) throws Exception {
		//if somehow a unknown player hacked into the system, without register
		if (action != PlayerAction.LOG_IN && findPlayer(playerName) == null) 
			throw new UserNotExistException(playerName);
		
		
		//filter player name
		switch (action) {
		case LOG_IN:
			if (findPlayer(playerName) != null) 
				throw new UserNameNotUniqueException(playerName);
			playerList.add(new Player(playerName));
			break;
			
		case CHAT:
			chatToAll(playerName, msg);
			break;
			
		case CREATE_GAME:
			//generate a random 4 digit ID for a new generated game
			int gameID = Integer.parseInt(String.format("%04d", new Random().nextInt(10000)));
			//Create a new game controller
			gameList.add(new ScrabbleGameController(Config, gameID));
			//create a new game inside this controller
			findGameController(gameID).performAction(playerName, action);
			//assign this player inside this game
			findPlayer(playerName).setJoinedRoomId(gameID);
			//record the game room activities
			roomActivityLog.add(recordEvent(playerName, "Created game room: "+gameID));
			
			break;
			
		case JOIN_GAME:
			joinGame(playerName, action, roomNm);
			break;
			
		case LOGOUT:
			int playerJoinedRoomID = findPlayer(playerName).getJoinedRoomId();
			findGameController(playerJoinedRoomID).performAction(playerName, action, row, col, letter);
			for (int i = gameList.size()-1; i >= 0; i--) {
				ScrabbleGameController room = gameList.get(i);
				if (room.isTimeToCloseController()) {
					setRoomActivityLog(playerName, room.getControllerID(), "The last player leave the room, room closed.\n");
					gameList.remove(gameList.get(i));
				}
			}
			break;
			
		case LOGOUT_LOBBY:
			playerList.remove(findPlayer(playerName));
			break;
			
		case INVITE_PLAYER:
			//TODO: invite player
			if (findPlayer(playerName2) == null) throw new UserNotExistException(playerName2);
			inviteList.put(playerName2, playerName);
			break;
			
		case ACCEPT_INVITE:
			Player invitor = findPlayer(inviteList.get(playerName));
			if(invitor == null) {
				inviteList.remove(playerName);
				throw new UserNotExistException(inviteList.get(playerName));
			} 
			joinGame(playerName, PlayerAction.JOIN_GAME, invitor.getJoinedRoomId());
			
			break;
			
		case REJECT_INVITE:
			inviteList.remove(playerName);
			break;
			
		default:
			playerJoinedRoomID = findPlayer(playerName).getJoinedRoomId();
			findGameController(playerJoinedRoomID).performAction(playerName, action, row, col, letter);
			break;
		}
	}


	/**
	 * @param playerName
	 * @param action
	 * @param roomNm
	 * @throws GameNotCreatedException
	 * @throws Exception
	 */
	private void joinGame(String playerName, PlayerAction action, int roomNm)
			throws GameNotCreatedException, Exception {
		if (findGameController(roomNm) == null)	throw new GameNotCreatedException(action);
		else 
			findGameController(roomNm).performAction(playerName, PlayerAction.JOIN_GAME);
		findPlayer(playerName).setJoinedRoomId(roomNm);
	}
	
	private Player findPlayer(String playerName){
		Player target = null;
		for (Player player: playerList) {
			if (player.getUserName().equals(playerName)) target = player;
		}
		return target;
	}
	
	
	private ScrabbleGameController findGameController(int controllerId) {
		ScrabbleGameController gameController = null;
		for (ScrabbleGameController controller : gameList) {
			if (controller.compareID(controllerId))	gameController = controller;
		}
		return gameController;
	}
	
	private String recordEvent(String player, String msg) {
		return String.format("%s|%10s:    %s", new Date().toString(), player, msg);
	}



	public String getChatLog() {
		return chatLog.toString().replaceAll(",", "\n").
				replace("[", "\n----------------\n----Player Chat Log----\n\n ").
				replace("]", "");
	}


	/**
	 * spread your voice to all other players to the room
	 * @param player
	 * @param sentence
	 */
	public void chatToAll(String player, String sentence) {
		chatLog.add(recordEvent(player, sentence));
	}



	public String getPlayerActivityLog() {
		return playerActivityLog.toString().replaceAll(",", "\n").
				replace("[", "\n----------------\n----Player Activites Log----\n\n ").
				replace("]", "");
	}


	/**
	 * record all player activities When ever a player join, create, logout
	 * @param player
	 * @param action
	 * @param object
	 */
	public void setPlayerActivityLog(String player, String action, String object) {
		String msg = String.format("%10s : \"%10s %s.\"\n", player, action, object);
		this.playerActivityLog.add(recordEvent(player, msg));
	}



	public String getRoomActivityLog() {
		return roomActivityLog.toString().replaceAll(",", "\n").
				replace("[", "\n----------------\n----Room Activites Log----\n\n ").
				replace("]", "");
	}
	

	/**
	 * record when a room be created and closed
	 * @param i
	 * @param action
	 */
	public void setRoomActivityLog(String player, int i, String action) {
		String msg = String.format("%20s: %s", i, action);
		roomActivityLog.add(recordEvent(player, msg));
	}


	public String getPlayerList() {
		String plyList = new String("   <Online Players>\n\n");
		String plyStatus =  new String();
		for (Player player :playerList) {
			if (player.getJoinedRoomId()>0)	plyStatus = "Playing";
			else plyStatus = "Free";
			
			plyList += String.format("%10s: %s.\n",
					player.getUserName(),plyStatus);
		}
		return plyList;
	}
	
	public ArrayList<Player> getFreePlayerList(){
		ArrayList<Player> freePlyList = new ArrayList<Player>();
		for(Player player: playerList) {
			if (player.getJoinedRoomId()<0)	freePlyList.add(player);
		}
		return freePlyList;
	}
	
	public ScrabbleGameController findGame(String playerName) throws UserNotExistException {
		Player player = findPlayer(playerName);
		if (player == null) {
			throw new UserNotExistException(playerName);
		}
		return findGameController(player.getJoinedRoomId());
	}


	public String getRoomList() {
		String rooms = new String("<Online Room List>\n\n");
		for (ScrabbleGameController room: gameList) rooms += room.toString();
		
		return rooms;
	}


	public HashMap<String, String> getInviteList() {
		return inviteList;
	}
}
