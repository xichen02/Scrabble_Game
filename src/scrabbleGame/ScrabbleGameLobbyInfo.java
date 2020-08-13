package scrabbleGame;

import java.util.HashMap;

public class ScrabbleGameLobbyInfo {
	private String chatLog;
	private String playerActivitiesLog;
	private String roomActivityLog;
	private String roomList;
	private String playerList;
	private HashMap<String, String> inviteList;
	
	public ScrabbleGameLobbyInfo() {
		super();
		this.chatLog = new String();
		this.playerActivitiesLog = new String();
		this.roomActivityLog = new String();
		this.playerList = new String();
		this.roomList = new String();
		this.inviteList = new HashMap<String, String>();
	}

	public String getChatLog() {
		return chatLog;
	}

	public void setChatLog(String chatLog) {
		this.chatLog = chatLog;
	}

	public String getPlayerActivitiesLog() {
		return playerActivitiesLog;
	}

	public void setPlayerActivitiesLog(String playerActivitiesLog) {
		this.playerActivitiesLog = playerActivitiesLog;
	}

	public String getRoomActivityLog() {
		return roomActivityLog;
	}

	public void setRoomActivityLog(String roomActivityLog) {
		this.roomActivityLog = roomActivityLog;
	}

	public String getPlayerList() {
		return playerList;
	}

	public void setPlayerList(String playerList) {
		this.playerList = playerList;
	}

	public String getRoomList() {
		return roomList;
	}

	public void setRoomList(String roomList) {
		this.roomList = roomList;
	}

	public HashMap<String, String> getInviteList() {
		return inviteList;
	}

	public void setInviteList(HashMap<String, String> inviteList) {
		this.inviteList = inviteList;
	}
	
	
	
	
}
