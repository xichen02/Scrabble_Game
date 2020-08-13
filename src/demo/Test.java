package demo;

import GUI.GameLobbyGUI;
import scrabbleGame.*;

public class Test {
	public static ScrabbleGameController game;
	public static ScrabbleGameLobbyController lobby;
	
	public ScrabbleGameController getGame() {
		return game;
	}
	
	
	public Test(String config) {
		Test.game = new ScrabbleGameController(config, 0);
		Test.lobby = new ScrabbleGameLobbyController(config);
	}
	
	public static void main (String args[]) {
		
		try{	
			Test lobby = new Test("AppSettings.properties");
			lobby.lobby.performAction("JIM", PlayerAction.LOG_IN);
			lobby.lobby.performAction("TIM", PlayerAction.LOG_IN);
			
			GameLobbyGUI player1 = new GameLobbyGUI("JIM", lobby);
			GameLobbyGUI player2 = new GameLobbyGUI("TIM", lobby);
			
			player1.run("JIM", lobby);
			player2.run("TIM", lobby);
			
			
			System.out.println(lobby.lobby.getGameLobbyData().getPlayerList());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	

	
}
