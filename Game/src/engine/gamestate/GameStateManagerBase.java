package engine.gamestate;

import java.awt.Color;
import java.awt.Graphics2D;

import engine.image.Images;
import engine.keyhandlers.XboxController;
import engine.window.GamePanel;


public class GameStateManagerBase {

	protected final GameState[] gameStates;
	protected int currentState;

	public static final int NUMGAMESTATES = 100;

	public GameStateManagerBase() {

		gameStates = new GameState[NUMGAMESTATES];

		Images.init();
		XboxController.init();
		
	}

	public void draw(Graphics2D g) {
		if (gameStates[currentState] != null)
			gameStates[currentState].draw(g);
		else {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		}
	}

	/**
	 * Returns the current state that the game is running.
	 */
	public int getCurrentState() {
		return currentState;
	}

	protected void loadState(int state) {
		
	}

	/**
	 * Switches out gamestate. Unloads previous gamestate and loads in the gamestate given by identifier
	 * @param state : identifier for state
	 */
	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}

	private void unloadState(int state) {
		gameStates[state] = null;
	}

	public void update() {
		if(XboxController.controller != null)
			XboxController.update();
		
		if (gameStates[currentState] != null)
			gameStates[currentState].update();
		
	}

	public GameState getGameState(int state){
		return gameStates[state];
	}

}