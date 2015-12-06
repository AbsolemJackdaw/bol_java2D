package game;

import static engine.window.GamePanel.HEIGHT;
import static engine.window.GamePanel.WIDTH;
import static game.util.Constants.COLOR_GENERAL;
import static game.util.Constants.FONT_CHOICES;
import static game.util.Constants.FONT_HEADER;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.SwingWorker;

import engine.gamestate.GameState;
import engine.keyhandlers.KeyHandler;
import engine.window.GamePanel;
import engine.window.gameAid.Utility;
import game.content.Loading;
import game.util.Util;

public class TutorialGui extends GameState{

	private int currentChoice = 0;
	private final String[] options = { "Yes", "No" };

	private BufferedImage backGround;

	public TutorialGui(GameStateManager gsm) {
		this.gsm = gsm;
		backGround = Util.generateStalactiteBackGround();
	}

	@Override
	public void draw(Graphics2D g) {

		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.drawImage(backGround, 0, 0, null);


		g.setFont(FONT_HEADER);
		g.setColor(COLOR_GENERAL);
		Utility.drawCenteredString(g, "Play Tutorial ?", FONT_HEADER, WIDTH/2, 175);

		for (int i = 0; i < options.length; i++){
			if (i == currentChoice){
				Utility.drawCenteredStringWithShadow(g, options[i], FONT_CHOICES, (GamePanel.WIDTH / 2),  GamePanel.HEIGHT - 150 + (i * 15), Color.gray, Color.gray.darker().darker());
			}
			else{
				g.setColor(COLOR_GENERAL);
				Utility.drawCenteredString(g, options[i], FONT_CHOICES, (GamePanel.WIDTH / 2),  GamePanel.HEIGHT - 150 + (i * 15));
			}
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

		if(currentChoice == 1){
			Loading.gotoNextLevel(gsm);

		}else{

			Util.startLoadIcon();

			new SwingWorker<Void, Integer>() {
				@Override
				protected Void doInBackground() throws Exception {

					try {

						gsm.setState(GameStateManager.GAME);

						//new blank world
						World currentWorld = (World)gsm.getGameState(gsm.getCurrentState());

						//no need to read any saves here !
						//this screen pops up when no game has been played yet
						//or no saves have been found

						currentWorld.loadMap("maps/tutorial_island.map");

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
