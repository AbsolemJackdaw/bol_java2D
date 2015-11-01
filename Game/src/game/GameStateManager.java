package game;

import engine.gamestate.GameStateManagerBase;

public class GameStateManager extends GameStateManagerBase{

	public static final int DEATH = 0;
	public static final int MENUSTATE = 1;
	public static final int HELP = 2;

	public static final int GAME = 3;

	public GameStateManager() {
		super();

		currentState = MENUSTATE;
		loadState(currentState);
	}

	@Override
	protected void loadState(int state) {
		switch (state){
		case MENUSTATE:
			gameStates[state] = new GameStateMenu(this);
			break;
		case GAME:
			gameStates[state] = new World(this);
			break;
		case HELP:
			gameStates[state] = new Help(this);
			break;
		}
	}

}
