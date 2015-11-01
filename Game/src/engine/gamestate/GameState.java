package engine.gamestate;

import java.awt.Graphics2D;



public abstract class GameState {

	public GameStateManagerBase gsm;

	public abstract void draw(Graphics2D g);

	public abstract void update();

}
