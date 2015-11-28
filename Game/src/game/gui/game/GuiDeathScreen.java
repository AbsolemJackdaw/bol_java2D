package game.gui.game;

import static game.util.Constants.COLOR_GENERAL;
import static game.util.Constants.FONT_CHOICES;
import static game.util.Constants.FONT_HEADER;
import engine.keyhandlers.KeyHandler;
import engine.window.GamePanel;
import engine.window.gameAid.Utility;
import game.GameStateManager;
import game.World;
import game.content.Loading;
import game.content.save.Save;
import game.entity.living.player.Player;
import game.gui.Gui;
import game.util.Util;

import java.awt.Graphics2D;

import javax.swing.SwingWorker;


public class GuiDeathScreen extends Gui {

	private final GameStateManager gsm;

	public GuiDeathScreen(World world, Player p) {
		super(world, p);

		gsm = (GameStateManager) world.gsm;
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);

		g.setFont(FONT_HEADER);
		g.setColor(COLOR_GENERAL);

		Utility.drawCenteredString(g, "You Dieded ! ", FONT_HEADER, GamePanel.WIDTH/2, 175);

		g.setFont(FONT_CHOICES);
		Utility.drawCenteredString(g, "Press Enter to reload last save ! ", FONT_CHOICES, GamePanel.WIDTH/2, GamePanel.HEIGHT- 150);

	}

	@Override
	public void handleGuiKeyInput() {

		if(KeyHandler.isPressed(KeyHandler.ENTER)){
			//any save check. i chose random parts
			if(!Save.readRandomParts()){ // if no save is found

				gsm.setState(GameStateManager.TUTORIAL);

			}else{ //read saves and continue playing

				Util.startLoadIcon();

				new SwingWorker<Void, Integer>() {
					@Override
					protected Void doInBackground() throws Exception {

						try {
							gsm.setState(GameStateManager.GAME);

							//new blank world
							World currentWorld = (World)gsm.getGameState(gsm.getCurrentState());

							//read map index
							Save.readRandomParts();

							//load saves from world. if none, the basic map will be loaded 
							Loading.startAtLastSavedLevel(gsm);

							//initiate current world. sets new player 
							currentWorld.init();

						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("an exception was cought trying to start up the game");
							System.out.println("Exiting game");
							System.exit(0);
						}

						return null;
					}

					@Override
					protected void done() {
						super.done();
						Util.stopLoadIcon();
					}
				}.execute();
			}
		}
	}
}
