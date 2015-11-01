package engine.imaging;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import engine.window.GamePanel;


public class Background {

	private BufferedImage image;

	private double x;
	private double y;

	private double dx;
	private double dy;

	private double moveScale;

	public boolean isStatic;
	public int speed;

	/**Creates a static background image
	 * @param s path to file*/
	public Background(String s){
		this(s, 0, true, 0);
	}

	/**
	 * @param s : path to image to use as background
	 * @param ms : move scale
	 * @param isStatic : whether the image will be static, or move around with the player
	 * */
	public Background(String s, double ms, boolean isStatic, int scrollSpeed) {

		try {
			image = ImageIO.read(getClass().getResourceAsStream(s));
			moveScale = ms;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		this.isStatic = isStatic;
		speed = scrollSpeed;
	}

	/**
	 * @param img : image to draw as background
	 * @param ms : move scale
	 * @param isStatic : whether the image will be static, or move around with the player
	 * */
	public Background(BufferedImage img, double ms, boolean isStatic, int scrollSpeed) {

		image = img;
		moveScale = ms;
		
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
		this.x = (x * moveScale) % GamePanel.WIDTH;
		this.y = (y * moveScale) % image.getHeight();

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
