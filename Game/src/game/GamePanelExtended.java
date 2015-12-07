package game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import engine.gamestate.GameStateManagerBase;
import engine.image.Images;
import engine.window.GamePanel;
import engine.window.gameAid.Utility;
import game.map.Maps;
import game.util.Constants;
import game.util.Util;

@SuppressWarnings("serial")
public class GamePanelExtended extends GamePanel{

	private BufferedImage icon;
	private double rotation = 0;

	public GamePanelExtended() {
		super();

		icon = Images.loadImage("/items/tools/rock_pickaxe.png");

		// Create a new blank cursor.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		setCursor(blankCursor);
	}

	@Override
	protected GameStateManagerBase getGameStateManager() {
		return new GameStateManager();
	}

	@Override
	public void addNotify() {
		super.addNotify();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				new Maps().init();
			}
		}).start();
	}
	
	public static boolean drawLoadingIcon;

	@Override
	protected void draw() {
		
		if(drawLoadingIcon)
		{
			if(g != null){
				BufferedImage rotatedIcon = Util.rotateImage(icon, rotation);

				g.setColor(new Color(0f,0f,0f, 0.4f));

				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.fillOval(GamePanel.WIDTH/2 - rotatedIcon.getWidth()/2, GamePanel.HEIGHT/2 - rotatedIcon.getHeight()/2,
						rotatedIcon.getWidth(), rotatedIcon.getHeight());
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

				g.drawImage(rotatedIcon, GamePanel.WIDTH/2 - rotatedIcon.getWidth()/2, GamePanel.HEIGHT/2 - rotatedIcon.getHeight()/2, null);

				Utility.drawCenteredStringWithShadow(g, "Loading, Please Wait", Constants.FONT_HEADER_SMALL, GamePanel.WIDTH/2, GamePanel.HEIGHT/2 - rotatedIcon.getHeight()/2 - 25, Color.white, Color.darkGray);
			}
		}

		else
			super.draw();


	}

	@Override
	protected void update() {

		if(drawLoadingIcon)
			if(rotation < 360)
				rotation += 10;
			else 
				rotation = 0;
		else
			super.update();
	}
}
