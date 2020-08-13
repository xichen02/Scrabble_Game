package scrabbleGame;

/**
 * Player class contains data of a player, but the function of handle action is not here
 * @author Zhang Yuanlong
 *
 */
public class Player {
	private String userName;
	private int score;
	private PlayerAction lastAction;
	private int joinedRoomId;
	
	
	public Player(String userName) {
		super();
		this.userName = userName;
		this.score = 0;
		this.lastAction = PlayerAction.JOIN_GAME;
		//initialised to not join a room
		this.joinedRoomId = -1;
	}

	public String getUserName() {
		return userName;
	}

	public int getScore() {
		return score;
	}

	public PlayerAction getLastAction() {
		return lastAction;
	}
	
	public void addScore(int score) {
		this.score += score;
	}
	
	public void setLastAction(PlayerAction lastAction) {
		this.lastAction = lastAction;
	}
	
	public int getJoinedRoomId() {
		return joinedRoomId;
	}

	public void setJoinedRoomId(int joinedRoomId) {
		this.joinedRoomId = joinedRoomId;
	}

	@Override
	public String toString() {
		return (userName + " ("+ score + ")");
	}

	@Override
	public boolean equals(Object obj) {
		Player other = Player.class.cast(obj);
		return userName == other.getUserName();
	}
	
	
}
