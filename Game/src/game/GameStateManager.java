package game;

import engine.gamestate.GameStateManagerBase;

public class GameStateManager extends GameStateManagerBase{

	public static final int DEATH = 0;
	public static final int MENU = 1;
	public static final int HELP = 2;
	public static final int GAME = 3;
	public static final int LOADING = 4;

	public GameStateManager() {
		super();

		currentState = LOADING;
		loadState(currentState);
	}

	@Override
	protected void loadState(int state) {
		switch (state){
		case MENU:
			gameStates[state] = new Menu(this);
			break;
		case GAME:
			gameStates[state] = new World(this);
			break;
		case HELP:
			gameStates[state] = new Help(this);
			break;
		case LOADING:
			gameStates[state] = new LoadingScreen(this);
			break;
		}
	}
}
