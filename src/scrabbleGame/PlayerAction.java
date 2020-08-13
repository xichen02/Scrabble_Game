package scrabbleGame;

/**
 * All enum variables that describe all allowed actions of player
 * @author Zhang Yuanlong
 *
 */
public enum PlayerAction {
	//actions allowed to be performed in a game lobby
	LOG_IN, LOGOUT_LOBBY,
	//actions allowed to be performed in a game
	CREATE_GAME,
	INVITE_PLAYER, ACCEPT_INVITE, REJECT_INVITE,
	JOIN_GAME, 
	START_GAME, LOGOUT, 
	WRITE_DOWN_LETTER,
	ACCEPT_WORD, REJECT_WORD, PASS_TURN,
	WAIT_RESULT,
	CHAT
}
