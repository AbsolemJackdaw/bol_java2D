package game.gui;

import java.awt.Graphics2D;
import java.util.ArrayList;

import engine.keyhandlers.KeyHandler;
import engine.keyhandlers.XboxController;
import engine.window.GamePanel;
import game.World;
import game.entity.living.player.Player;


public class Gui {

	/**Returns the center of the screen*/
	protected final int centerX = GamePanel.WIDTH/2;
	/**returns the center of the screen*/
	protected final int centerY = GamePanel.HEIGHT/2;

	/**the player opening this gui*/
	protected final Player player;
	
	/**the world this gui is opened in*/
	protected final World world;

	/**list that holds all buttons that will be drawn to interact with*/
	protected ArrayList<Button> buttonList = new ArrayList<Button>();

	public Gui(World world, Player p) {
		player = p;
		this.world = world;
	}

	public void draw(Graphics2D g){

	}

	/**
	 * Handle your key input here.
	 * Always use super() unless you do not want your gui to close with escape
	 */
	public void handleGuiKeyInput() {
		if(KeyHandler.isPressed(KeyHandler.ESCAPE)){
			world.displayGui(null);
		}
		if(KeyHandler.isPressed(KeyHandler.ESCAPE2) && XboxController.controller != null){
			world.displayGui(null);
		}
	}
	
	/**
	 * This should only be set to false if the GUI is a HUD GUI, meaning that no 
	 * special interaction of the player is needed to manipulate the gui.
	 */
	public boolean pausesGame(){
		return true;
	}

	/**
	 * Returns the position of the tooltip window.
	 * Use centerX and centerY +/- to maintain a good ratio between screen resolutions
	 */
	public int[] getToolTipWindowPosition(){
		return new int[]{0,0};
	}
}
