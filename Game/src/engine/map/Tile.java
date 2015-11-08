package engine.map;

import java.awt.image.BufferedImage;

public class Tile {

	/**The image of the tile*/
	private final BufferedImage image;
	
	/**Every Tile has a type. either Solid or Ghost*/
	private final int type;

	/**Every tile has a unique id*/
	private final int tileID;
	
	// tile types
	public static final int GHOST = 0;
	public static final int SOLID = 1;
	/**prototype. is currently used nowhere*/
	public static final int INVISIBLE = 2;

	public Tile(BufferedImage image, int type, int tileID) {
		this.image = image;
		this.type = type;
		this.tileID = tileID;
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getType() {
		return type;
	}
	
	public int getID(){
		return tileID;
	}

}
