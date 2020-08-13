package scrabbleGame;

import exceptions.InvalidActionInCurrentPhaseExcception;

//TODO put it inside Scrabble game
public enum GamePhase {
	INITIAL{
		@Override
		GamePhase consumeAction(PlayerAction action) throws 
		InvalidActionInCurrentPhaseExcception {
			switch(action) {
			case CREATE_GAME: 	return GamePhase.INITIAL;
			case INVITE_PLAYER: return GamePhase.WAITING;
			case JOIN_GAME: 	return GamePhase.WAITING;
			case START_GAME:	return GamePhase.PLAYING;
				
			default:
				throw new InvalidActionInCurrentPhaseExcception(INITIAL, action);
			}
		}
	},
	WAITING{
		@Override
		GamePhase consumeAction(PlayerAction action) throws 
		InvalidActionInCurrentPhaseExcception {
			switch (action) {
			case INVITE_PLAYER:	return GamePhase.WAITING;	
			case JOIN_GAME:		return GamePhase.WAITING;	
			case START_GAME:	return GamePhase.PLAYING;	
			case LOGOUT:		return GamePhase.ENDING;
			case WAIT_RESULT:	return GamePhase.ENDING;
				
			default:
				throw new InvalidActionInCurrentPhaseExcception(WAITING, action);	
			}
		}
	},
	PLAYING{
		@Override
		GamePhase consumeAction(PlayerAction action) throws 
		InvalidActionInCurrentPhaseExcception {
			switch (action) {
			case WRITE_DOWN_LETTER:	return GamePhase.PLAYING;
			case PASS_TURN:			return GamePhase.PLAYING;
			case WAIT_RESULT:		return GamePhase.ENDING;
			case LOGOUT:			return GamePhase.ENDING;

			default:
				throw new InvalidActionInCurrentPhaseExcception(PLAYING, action);
			}
		}
		
	},
	VOTING{
		@Override
		GamePhase consumeAction(PlayerAction action) throws 
		InvalidActionInCurrentPhaseExcception {
			switch (action) {
			case ACCEPT_WORD:		return GamePhase.VOTING;
			case REJECT_WORD:		return GamePhase.VOTING;
			case WAIT_RESULT:		return GamePhase.ENDING;
			case LOGOUT:			return GamePhase.ENDING;

			default:
				throw new InvalidActionInCurrentPhaseExcception(VOTING, action);
			}
		}
	},
	ENDING{

		@Override
		GamePhase consumeAction(PlayerAction action) throws 
		InvalidActionInCurrentPhaseExcception {
			switch (action) {
			default:
				throw new InvalidActionInCurrentPhaseExcception(ENDING, action);
			}
		}
		
	};
	abstract GamePhase consumeAction(PlayerAction action) throws 
	InvalidActionInCurrentPhaseExcception;
}
