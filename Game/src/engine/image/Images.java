package engine.image;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Images {

	public static Images instance;

	public Images() {
		instance = this;
	}

	public static void init(){
		instance = new Images();
	}

	public static BufferedImage loadImage(String path){
		BufferedImage img = null;
		try {
			img = ImageIO.read(Images.class.getClass().getResourceAsStream(path));
		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics." + " " + path + " might be an invalid directory");
			System.exit(0);
		}

		return img;
	}

	public static ArrayList<BufferedImage[]> loadMultiAnimation(int[] frames, int width, int height, String path){
		try {
			ArrayList<BufferedImage[]> sprites;

			final BufferedImage spritesheet = ImageIO.read(Images.class.getClass().getResourceAsStream(path));

			sprites = new ArrayList<BufferedImage[]>();
			for (int i = 0; i < frames.length; i++) {

				final BufferedImage[] bi = new BufferedImage[frames[i]];

				for (int j = 0; j < frames[i]; j++)
					bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
				sprites.add(bi);
			}
			return sprites;

		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println("Failed to load " + path + ". Shutting down system.");
			System.exit(0);
		}
		return null;
	}

	public static BufferedImage[] loadMultiImage(String s, int x, int y, int sizeX, int sizeY, int subImages) {
		BufferedImage[] ret;
		try {
			final BufferedImage spritesheet = ImageIO.read(Images.class.getResourceAsStream(s));

			ret = new BufferedImage[subImages];

			for (int i = 0; i < subImages; i++)
				ret[i] = spritesheet.getSubimage(i * x, y, sizeX, sizeY);

			return ret;
		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics." + " " + s + " might be an invalid directory");
			System.exit(0);
		}
		return null;
	}

	public static BufferedImage[] loadMultiImage(String s, int x, int y, int subImages) {
		return loadMultiImage(s, x, y, x, x, subImages);
	}
}
