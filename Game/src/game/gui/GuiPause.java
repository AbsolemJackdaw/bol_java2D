package game.gui;

import game.Loading;
import game.World;
import game.content.save.Save;
import game.entity.living.player.Player;
import game.util.Util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import base.main.GamePanel;
import base.main.GameStateManager;
import base.main.keyhandler.KeyHandler;

public class GuiPause extends Gui {

	private Font font = new Font("Constantia", Font.PLAIN, 36);
	private Font fontChoices = new Font("Arial", Font.PLAIN, 12);
	private Color color = new Color(250, 231, 217);
	private Color clr = new Color(0xcfd9e7);
	private float alpha = 1.0F;

	private boolean showMessageSaved = false;
	private int currentChoice = 0;
	private final String[] options = { "Resume", "Save", "Menu", "Quit" };

	public GuiPause(World world, Player p) {
		super(world, p);

	}

	@Override
	public void draw(Graphics2D g) {
	
		super.draw(g);
		
		g.setFont(font);
		g.setColor(color);
		
		Util.drawCenteredString(g, "Pause", font, GamePanel.WIDTH/2, 175);

		// Draw menu square
		g.setColor(clr);
		g.drawRoundRect((GamePanel.WIDTH / 2) - 28, GamePanel.HEIGHT - 150 + (currentChoice * 15) - 19, 55, 15, 5, 5);

		// draw menu options
		g.setFont(fontChoices);
		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice)
				g.setColor(Color.black);
			else
				g.setColor(Color.red.darker());
			
			Util.drawCenteredString(g, options[i], fontChoices, GamePanel.WIDTH / 2, GamePanel.HEIGHT - 150 + (i * 15));
			
		}
		
		if(showMessageSaved){	
			//set the opacity
			g.setFont(font);
			g.setColor(new Color(1f, 1f, 1f, alpha));
			Util.drawCenteredString(g, "Save Succesful", font, GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2 + 30);
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
		else if(currentChoice == 2)
		{
			saveGame();
			world.gsm.setState(GameStateManager.MENUSTATE);
		}
		else if (currentChoice == 3)
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

	@Override
	public boolean pausesGame() {
		return true;
	}
}
