package game;

import static base.main.GamePanel.HEIGHT;
import static base.main.GamePanel.WIDTH;
import game.content.Images;
import game.content.save.Save;
import game.item.Items;
import game.util.Util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import base.main.GamePanel;
import base.main.GameState;
import base.main.GameStateManager;
import base.main.keyhandler.KeyHandler;

public class GameStateMenu extends GameState{

	private Font font;
	private Font fontChoices;
	private Color color = new Color(250, 231, 217);
	private Color clr = new Color(0xcfd9e7);

	private int currentChoice = 0;
	private final String[] options = { "Start", "Help", "Quit" };

	private BufferedImage backGround;

	public GameStateMenu(GameStateManager gsm) {
		super();
		this.gsm = gsm;

		if(Images.instance.stalactites[0] == null) //check to only load once
			for(int i = 0; i < Images.instance.stalactites.length; i++){
				Images.instance.stalactites[i] = Images.loadImage("/background/stalactite_"+i+".png");
			}

		backGround = Util.generateStalactiteBackGround();
		
		font = new Font("Constantia", Font.PLAIN, 36);

		fontChoices = new Font("Arial", Font.PLAIN, 12);

		Save.readKeyBinds();

		Items.loadItems();
	}

	@Override
	public void draw(Graphics2D g){

		
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.drawImage(backGround, 0, 0, null);

		
		g.setFont(font);
		g.setColor(color);
		Util.drawCenteredString(g, "The Brim Of Life", font, WIDTH/2, 175);

		// Draw menu square
		g.setColor(clr);
		g.drawRoundRect((GamePanel.WIDTH / 2) - 16, GamePanel.HEIGHT - 150 + (currentChoice * 15) - 12 , 27, 14, 5, 5);

		// draw menu options
		g.setFont(fontChoices);
		for (int i = 0; i < options.length; i++){
			if (i == currentChoice)
				g.setColor(Color.gray);
			else
				g.setColor(color);
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
