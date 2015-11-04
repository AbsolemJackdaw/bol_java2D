package engine.imaging;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import engine.window.GamePanel;


public class Background {

	private BufferedImage image;

	private double x;
	private double y;

	private double dx;
	private double dy;

	private double moveScaleX;
	private double moveScaleY;

	public boolean isStatic;
	public int speed;

	/**Creates a static background image
	 * @param s path to file*/
	public Background(BufferedImage img){
		this(img, 0, 0, true, 0);
	}

	/**
	 * @param img : image to draw as background
	 * @param msX : move scale for x axis
	 * @param msY : move scale for y axis
	 * @param isStatic : whether the image will be static, or move around with the player
	 * @param scrollSpeed : the speed at which the image will move
	 * */
	public Background(BufferedImage img, double msX, double msY, boolean isStatic, int scrollSpeed) {

		image = img;
		moveScaleX = msX;
		moveScaleY = msY;

		this.isStatic = isStatic;
		speed = scrollSpeed;
	}

	public void draw(Graphics2D g) {

		g.drawImage(image, (int) x, (int) y, null);

		if (x < 0)
			g.drawImage(image, (int) x + GamePanel.WIDTH, (int) y, null);
		if (x > 0)
			g.drawImage(image, (int) x - GamePanel.WIDTH, (int) y, null);
	}

	public void setPosition(double x, double y) {
		this.x = (x * moveScaleX) % GamePanel.WIDTH;
		this.y = (y * moveScaleY) % image.getHeight();

	}

	/** used to move around the back ground */
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	/** used to move around the back ground */
	public void update() {
		x += dx;
		y += dy;
	}

}
