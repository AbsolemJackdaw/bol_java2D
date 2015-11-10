package game;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import engine.gamestate.GameStateManagerBase;
import engine.window.GamePanel;

@SuppressWarnings("serial")
public class GamePanelExtended extends GamePanel{

	public GamePanelExtended() {
		super();
		
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		setCursor(blankCursor);
	}
	
	@Override
	protected GameStateManagerBase getGameStateManager() {
		return new GameStateManager();
	}
}
