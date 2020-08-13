package scrabbleGame;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import exceptions.AllPlayerPassedException;
import exceptions.GameBoardIsFullException;
import exceptions.InvalidActionInCurrentPhaseExcception;
import exceptions.NoRightToStartGameException;
import exceptions.PlayerLogOutException;
import exceptions.UserNameNotUniqueException;
import exceptions.UserNotExistException;
import exceptions.WrongTurnToPlayException;

public class ScrabbleGame {
	private ScrabbleBoard gameBoard;
	private ScrabbleRules rule;
	private GamePhase gamePhase;
	private ArrayList<String> gameLog;
	private ArrayList<Player> playerList;
	private Player gameOwner;
	private boolean isGameClose;
	//to indicate this is who's turn
	private String playerTurn;
	private int playerTurnIndex;
	/*
	 * Things need for voting a word, if isTimeToVote
	 * is true. Client need to consume all string in stack
	 * before move to next activity
	 */
	private boolean isTimeToVote;
	private ArrayList<String> votingWordList;
	private String votedWord;
	private int voteForYes;
	private int totalVotes;
	int i = 0;
	//activate when no one stay in this game anymore
	private boolean isGameEnd;
	/**
	 * {@link ScrabbleGame#dictionary} :
	 * A Dictionary that used to store all appeared word in the game,
	 * <word, Accept State> true= word been accepted; 
	 * false = word been rejected
	 */
	private HashMap<String, Boolean> dictionay;
	
	
	
	
	public ScrabbleGame(String properityFile, String player) {
		try(FileReader reader = new FileReader(properityFile)){
			Properties config = new Properties();
			config.load(reader);
			
			this.gameBoard = new ScrabbleBoard(config.getProperty("gameBoardRows"),
					config.getProperty("gameBoardColumns"));
		} catch (Exception e) {
			String msg = "ERROR: Load Config File Failed!\n Using Deafult arguments:\n"
					+ "BoardSize = 20*20";
			this.gameBoard = new ScrabbleBoard("20","20");
			System.out.println(msg);
			
			e.printStackTrace();
		}
		
		this.playerList = new ArrayList<Player>();
		this.rule = new ScrabbleRules();
		this.gameOwner = new Player(player);
		this.playerTurn = null;
		this.playerTurnIndex = -1;
		this.gamePhase = GamePhase.INITIAL;
		this.gameLog = new ArrayList<String>();
		this.isTimeToVote = false;
		this.votingWordList = new ArrayList<String>();
		this.dictionay = new HashMap<String, Boolean>();
		this.voteForYes = 0;
		this.totalVotes = 0;
		this.isGameEnd = false;
		this.isGameClose = false;
		//add player owner to the game
		playerList.add(gameOwner);
		
		//record all to the game log
		recordEvent(player, PlayerAction.CREATE_GAME, new String("Create the game.\n"));
		
	}

	
	/**
	 * Overloaded function to provide option when don't need to pass last three arguments
	 * @param playerName
	 * @param action
	 * @throws UserNameNotUniqueException 
	 * @throws InvalidActionInCurrentPhaseExcception 
	 * @throws WrongTurnToPlayException 
	 * @throws NoRightToStartGameException 
	 * @throws AllPlayerPassedException 
	 * @throws PlayerLogOutException 
	 * @throws GameBoardIsFullException 
	 */
	public void playerActionHandler(String playerName, PlayerAction action) throws Exception {
		playerActionHandler(playerName, action, 0, 0, null);
	}
	
	
	/**
	 * Game Main handler to consume all actions. 
	 * @param playerName
	 * @param action
	 * @param row
	 * @param col
	 * @param letter
	 * @throws Exception 
	 */
	public void playerActionHandler(String playerName, PlayerAction action,
			int row, int col, String letter) throws Exception {
		
		//check if the user exist in the game
		if ( action != PlayerAction.JOIN_GAME)
			findPlayer(playerName);
		
		//only the game owner can start the game
		if (action == PlayerAction.START_GAME) {
			if (playerName != gameOwner.getUserName()) throw new NoRightToStartGameException();
		}
		
		//Behaviors of actions are restricted by game phase
		gamePhase = gamePhase.consumeAction(action);
		
		
		//block some invalid actions players need to play in order
		if ( playerTurn != null && playerName != playerTurn && action != PlayerAction.LOGOUT
				&& action != PlayerAction.ACCEPT_WORD && action != PlayerAction.REJECT_WORD) 
			throw new WrongTurnToPlayException(playerTurn);
		
		//update player's last action if the action is valid to player turn and game phase
		for (Player player : playerList){
			if (playerName == player.getUserName()) {
				player.setLastAction(action);
			}	
		}
		
		
		//If any terminate condition is been met. Game Ends. All player wait for final results
		try{
			rule.gameTerminate(playerList, gameBoard);
		}catch(Exception e) {
			//update all player states
			for (Player player : playerList){
				if (playerName == player.getUserName()) {
					player.setLastAction(PlayerAction.WAIT_RESULT);
					action = PlayerAction.WAIT_RESULT;
					gamePhase = GamePhase.ENDING;
				}
			}
			throw new Exception(e.toString());
		}
		
		
		
		switch(action) {
		case JOIN_GAME:
			addPlayer(playerName);
			recordEvent(playerName, action, new String("Joined the game.\n"));
			break;
			
		case START_GAME:
			recordEvent(playerName, action, 
					new String("Game Start at "+playerTurn +"'s turn.\n"));
			nextTurn();
			break;
			
		case LOGOUT:
			recordEvent(playerName, action, 
					new String("Want to Exit the game. Game Ends Wait Results.\n"));
			break;
			
		case WRITE_DOWN_LETTER:
			gameBoard.writeToBoard(row, col, letter);
			recordEvent(playerName, action,
					new String(String.format("Written down letter \"%s\" at cell (%d . %d).\n",
							letter, row, col)));
			//check the newly formed word each time after the letter been placed
			startVote( row, col);
			checkAllWord();
			break;
			
		case ACCEPT_WORD:
			voteForYes++;
			totalVotes++;
			i++;
			recordEvent(playerName, action,
					new String(String.format("Vote YES for word \"%s\".\n", votedWord)));
			checkAllWord();
			
			break;
			
		case REJECT_WORD:
			totalVotes++;
			recordEvent(playerName, action,
					new String(String.format("Vote NO for word \"%s\".\n", votedWord)));
			checkAllWord();
			
			break;
			
		case PASS_TURN:
			if (playerName == playerTurn) {
				recordEvent(playerName, action,
						new String(String.format("Player (%s) passed his/her turn.\n", playerName)));
				nextTurn();
			} else throw new InvalidActionInCurrentPhaseExcception(gamePhase, action);
			break;
			
		case WAIT_RESULT:
			isGameEnd = true;
			//sign player out from current game
			findPlayer(playerName).setJoinedRoomId(-1);
			playerList.remove(findPlayer(playerName));
			if (playerList.size() == 0) isGameClose = true;
			recordEvent(playerName, action,
					new String(String.format("Player (%s) got game result. "
							+ "Exited from the Game.\n %d player remains.", 
							playerName, playerList.size())));
			break;
			
		default: 
			//throw new GameInUnknowPhaseException();
		}
		
		
	}
	
	
	/**
	 * Release the game result, rank player with their score in descending order
	 * @return String
	 */
	public String playerRank() {
		//sort player with their score number in descending order
		ArrayList<Player> playerList = getPlayerList();
		playerList.sort(new Comparator<Player>() {
			public int compare(Player p1, Player p2) {return (p2.getScore() - p1.getScore());}
		});
		
		//format the output result
		String result = "   <Rank>\n";
		for (int i=0; i<playerList.size(); i++) {
			if (i == 0) result += "1st: " + playerList.get(i).toString() + "\n";
			if (i == 1) result += "2nd: " + playerList.get(i).toString() + "\n";
			if (i == 2) result += "3rd: " + playerList.get(i).toString() + "\n";
			if (i >  2) result += (i+1) + "th: " + playerList.get(i).toString() + "\n";
		}
		
		return result;
	}
	
	
	/**
	 * move to next player turn, initial order is randomized  
	 */
	private void nextTurn() {
		//player's turn finish after writing down letter
		String previousPlayer = playerTurn;
		
		//if last payer has moved, back to first player's turn
		if (playerTurnIndex == playerList.size()-1)
			playerTurnIndex = -1;
		playerTurnIndex++;
		
		playerTurn = getPlayerList().get(playerTurnIndex).getUserName();
		
		recordEvent( new String(String.format("%s's turn finished. Move to %s's turn.\n",
				previousPlayer, playerTurn)));
	}
	
	
	/**
	 * Adding a new player to the current game
	 * @param userName
	 * @throws UserNameNotUniqueException
	 */
	public void addPlayer(String userName) throws UserNameNotUniqueException {
		//All user name need to be unique,
		//if user not exist then we can add it
		Player player = null;
		try {
			player = findPlayer(userName);
		}catch (Exception e) {
			playerList.add(new Player(userName));
		}
		
		if (player != null) throw new UserNameNotUniqueException(userName);		
	}

	
	/**
	 * Record activity to a game log without a player action, which means it is the result of 
	 * previous action
	 * @param player
	 * @param event
	 */
	private void recordEvent( String event) {
		String msg = new Date().toString() + "|  >Result: "+event;
		gameLog.add(msg);
	}
	
	
	/**
	 * Record activity to a game log
	 * @param player
	 * @param action
	 * @param event
	 */
	private void recordEvent(String player, PlayerAction action, String event) {
		String msg = String.format("%s|%9s: (%s) %8s: %-17s%10s: %s", new Date().toString(),
				 "Player", player, "Action", action, "Event", event);
		gameLog.add(msg);
	}
	
	
	
	private void startVote(int row, int col) throws UserNotExistException {
		//if a new letter has just been placed, ask for a vote check 
		if (votingWordList.isEmpty()) {
			votingWordList = gameBoard.getNewFormedWords(row, col);
			//make a vote for the new word
			isTimeToVote = true;
			gamePhase = gamePhase.VOTING;
			votedWord = votingWordList.get(0);
		}
		recordEvent(new String(String.format("As the new word been formed. Moving to VOTING phase for words : %s.\n", 
		votingWordList.toString().replaceAll(",", " ").replace("[" ,  "\"").replaceAll("]", "\""))));
	}
	
	
	/**
	 * manually ask for vote result, then calculate result
	 * @param word
	 * @throws UserNotExistException 
	 */
	private void voteWord(String votedWord) throws UserNotExistException {
		//handing in the voting stage until all players make their vote
		int totalPlayerNumber = playerList.size();
		
		//then check word again if success then remove this word
		if (totalVotes == totalPlayerNumber) {
			//A word is accepted if more than half player accept it.
			//What ever the result it will be updated to the dictionary
			if(voteForYes > (totalVotes/2)) dictionay.put(votedWord, true); 
			else dictionay.put(votedWord, false);
		}
		autoCheckWord(votedWord);	
	}
		
	
	/**
	 * auto check word from the local dictionary
	 * then calculate result
	 * @param currentPlayer
	 * @param votedWord
	 * @return
	 * @throws UserNotExistException 
	 */
	private boolean autoCheckWord(String votedWord) throws UserNotExistException {
		//if the newly generated word has been voted and is accepted
		int score;
		Player currentPlayer = findPlayer(playerTurn);
		if(dictionay.containsKey(votedWord)) {
			if (dictionay.get(votedWord)) {
				score = rule.calcScore(votedWord);
				//update player score
				currentPlayer.addScore(score);
				playerList.set(playerList.indexOf(currentPlayer),currentPlayer);
				recordEvent(new String(String.format("Newly Generated word: \"%s\" is acceptted in previous turn"
							+ ". So player %s + %d scores.\n", votedWord, playerTurn, score)));
			}
			//print result if the word was not accepted by player before
			else recordEvent(new String(String.format("Newly Generated word: \"%s\" is rejected in previous turn"
					+ ". So player do not add any scores.\n", votedWord, playerTurn)));
			
			//if the word is been auto checked, reset voting 
			votingWordList.remove(votingWordList.indexOf(votedWord));
			totalVotes = 0;
			voteForYes = 0;
		}
		
		//Need to consume all the voting words before back to playing phase
		if (votingWordList.isEmpty()) {		
			isTimeToVote = false;
			gamePhase = gamePhase.PLAYING;
			this.votedWord = null;
			nextTurn();
		} else {
			this.votedWord = votingWordList.get(0);
		}
		
		return dictionay.containsKey(votedWord);
	}
	
	
	private void checkAllWord() throws UserNotExistException {
		voteWord(votedWord);
		for (int i= (votingWordList.size()-1); i>=0; i--) voteWord(votingWordList.get(i));	
	}
	
	
	/**
	 * If the player exists return the player, otherwise throw UserNotExistException
	 * @param playerName
	 * @return Player
	 * @throws UserNotExistException
	 */
	private Player findPlayer(String playerName) throws UserNotExistException {
		Player result = null;
		for (Player player: playerList) {
			if (playerName == player.getUserName())
				result = player;
		}
		
		if (result == null) throw new UserNotExistException(playerName);
		
		return result;
	}
	
	
	public ArrayList<Player> getPlayerList() {
		return playerList;
	}

	
	public ScrabbleBoard getGameBoard() {
		return gameBoard;
	}
	
	
	public ScrabbleRules getRule() {
		return rule;
	}
	
	
	public String getGameLog() {
		return gameLog.toString().replaceAll(",", "\n").
				replace("[", "\n----------------\n----Game Log----\n\n ").
				replace("]", "");
	}


	public boolean isTimeToVote() {
		return isTimeToVote;
	}


	public String getVotedWord() {
		return votedWord;
	}


	public boolean isGameEnd() {
		return isGameEnd;
	}


	public boolean isGameClose() {
		return isGameClose;
	}


	public String getPlayerTurn() {
		return playerTurn;
	}


	public GamePhase getGamePhase() {
		return gamePhase;
	}


	public String getGameOwner() {
		return gameOwner.getUserName();
	}
	
}
