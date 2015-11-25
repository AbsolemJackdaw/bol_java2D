package game.gui;

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

import java.awt.Color;
import java.awt.Graphics2D;

public class GuiPause extends Gui {

	private Color clr = new Color(0xcfd9e7);
	private float alpha = 1.0F;

	private boolean showMessageSaved = false;
	private int currentChoice = 0;
	private final String[] options = { "Resume", "Save", "Options", "Menu", "Quit" };

	public GuiPause(World world, Player p) {
		super(world, p);

	}

	@Override
	public void draw(Graphics2D g) {
	
		super.draw(g);
		
		g.setFont(FONT_HEADER);
		g.setColor(COLOR_GENERAL);
		
		Utility.drawCenteredString(g, "Pause", FONT_HEADER, GamePanel.WIDTH/2, 175);

		// Draw menu square
		g.setColor(clr);
		g.drawRoundRect((GamePanel.WIDTH / 2) - 28, GamePanel.HEIGHT - 150 + (currentChoice * 15) - 19, 55, 15, 5, 5);

		// draw menu options
		g.setFont(FONT_CHOICES);
		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice)
				g.setColor(Color.black);
			else
				g.setColor(Color.red.darker());
			
			Utility.drawCenteredString(g, options[i], FONT_CHOICES, GamePanel.WIDTH / 2, GamePanel.HEIGHT - 150 + (i * 15));
			
		}
		
		if(showMessageSaved){	
			//set the opacity
			g.setFont(FONT_HEADER);
			g.setColor(new Color(1f, 1f, 1f, alpha));
			Utility.drawCenteredString(g, "Save Succesful", FONT_HEADER, GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2 + 30);
			alpha -= 0.01f;

			//increase the opacity and repaint
			if (alpha <= 0.0F)
				alpha = 0.0F;

			if(alpha == 0.0F)
				showMessageSaved = false;
		}
	}

	private void select(){
		if (currentChoice == 0){
			world.displayGui(null);
		}
		else if (currentChoice == 1)
		{
			saveGame();
			showMessageSaved = true;
			alpha = 1.0F;
		}
		else if(currentChoice == 2){
			saveGame();
			world.displayGui(new GuiKeyBinds(world, player));
		}
		else if(currentChoice == 3)
		{
			saveGame();
			world.gsm.setState(GameStateManager.MENU);
		}
		else if (currentChoice == 4)
		{
			saveGame();
			System.exit(0);
		}
	}

	@Override
	public void handleGuiKeyInput() {
		if (KeyHandler.isValidationKeyPressed())
			select();
		if (KeyHandler.isUpKeyHit()) {
			currentChoice--;
			if (currentChoice == -1)
				currentChoice = options.length - 1;
		}
		if (KeyHandler.isDownKeyHit()) {
			currentChoice++;
			if (currentChoice == options.length)
				currentChoice = 0;
		}
	}

	private void saveGame()
	{
		Save.writePlayerData(player);
		Save.writeRandomParts();
		Save.writeWorld((World)world.gsm.getGameState(world.gsm.getCurrentState()), Loading.index);
	}
}
