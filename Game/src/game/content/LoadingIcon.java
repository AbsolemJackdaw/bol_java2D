package game.content;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import engine.image.Images;
import engine.window.GamePanel;

public class LoadingIcon implements Runnable {

	private volatile boolean isRunning;

	// image
	private BufferedImage image;
	private Graphics2D g;

	private BufferedImage icon;
	
	@Override
	public void run() {

		init();

		while (isRunning){



		}

	}

	public void start(){
		isRunning = true;
	}

	public void stop(){
		isRunning = false;
	}

	public void init(){

		icon = Images.loadImage("/items/pickaxe.png");
		
		image = new BufferedImage(GamePanel.WIDTH, GamePanel.HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		start();

	}

	/**
	 * Draws world to the game
	 */
	private void draw() {
		g.drawImage(icon, 50, 50, null);
	}

}
