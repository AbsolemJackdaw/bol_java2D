package game;

import engine.gamestate.GameStateManagerBase;
import engine.window.GamePanel;

@SuppressWarnings("serial")
public class GamePanelExtended extends GamePanel{

	public GamePanelExtended() {
		super();
	}
	
	@Override
	protected GameStateManagerBase getGameStateManager() {
		return new GameStateManager();
	}
	

}
