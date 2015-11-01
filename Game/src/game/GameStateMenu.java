package game;

import engine.gamestate.GameState;
import engine.gamestate.GameStateManagerBase;
import engine.image.Images;
import engine.keyhandlers.KeyHandler;
import engine.window.GamePanel;
import game.content.save.Save;
import game.item.Items;
import static engine.window.GamePanel.HEIGHT;
import static engine.window.GamePanel.WIDTH;
import static game.util.Constants.*;
import game.util.Util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class GameStateMenu extends GameState{

	private Color clr = new Color(0xcfd9e7);

	private int currentChoice = 0;
	private final String[] options = { "Start", "Help", "Quit" };

	private BufferedImage backGround;

	public GameStateMenu(GameStateManagerBase gsm) {
		super();
		this.gsm = gsm;

		if(Loading.stalactites[0] == null) //check to only load once
			for(int i = 0; i < Loading.stalactites.length; i++){
				Loading.stalactites[i] = Images.loadImage("/background/stalactite_"+i+".png");
			}

		backGround = Util.generateStalactiteBackGround();
		
		Save.readKeyBinds();

		Loading.loadMusic();
		
		Items.loadItems();
	}

	@Override
	public void draw(Graphics2D g){

		
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.drawImage(backGround, 0, 0, null);

		
		g.setFont(FONT_HEADER);
		g.setColor(COLOR_GENERAL);
		Util.drawCenteredString(g, "The Brim Of Life", FONT_HEADER, WIDTH/2, 175);

		// Draw menu square
		g.setColor(clr);
		g.drawRoundRect((GamePanel.WIDTH / 2) - 16, GamePanel.HEIGHT - 150 + (currentChoice * 15) - 12 , 27, 14, 5, 5);

		// draw menu options
		g.setFont(FONT_CHOICES);
		for (int i = 0; i < options.length; i++){
			if (i == currentChoice)
				g.setColor(Color.gray);
			else
				g.setColor(COLOR_GENERAL);
			g.drawString(options[i], (GamePanel.WIDTH / 2) - 15, GamePanel.HEIGHT - 150 + (i * 15));
		}
	}

	@Override
	public void update() {

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

	private void select() {
		if (currentChoice == 0){
			gsm.setState(GameStateManager.GAME);
			Save.readRandomParts();
			Loading.startAtLastSavedLevel(gsm);

		}
		if (currentChoice == 1)
			gsm.setState(GameStateManager.HELP);
		if (currentChoice == 2)
			System.exit(0);
	}


}
