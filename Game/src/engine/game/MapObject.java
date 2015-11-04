package engine.game;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import engine.game.entity.EntityPlayer;
import engine.imaging.Animation;
import engine.map.Tile;
import engine.map.TileMap;
import engine.save.DataTag;
import engine.window.GamePanel;


public abstract class MapObject {

	// tile stuff
	public TileMap tileMap;

	protected int tileSize;
	/**pos x on the map*/
	public double xmap;
	/**pos y on the map*/
	public double ymap;

	// position and vector
	/**pos x on screen*/
	public double xScreen;
	/**pos y on screen*/
	public double yScreen;
	/**direction x. used for updating position*/
	public double dx;
	/**direction y. used for updating position*/
	public double dy;

	// dimensions
	/**texture width*/
	protected int width;
	/**texture height*/
	protected int height;

	// collision box
	/**the object's size, x. (CollisionBox)*/
	protected int entitySizeX;
	/**the object's size, y. (CollisionBox)*/
	protected int entitySizeY;

	// collision
	/**y column on the map*/
	public int currentColumn; 
	/**x row on the map*/
	public int currentRow;

	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;

	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;

	// animation
	/**The Entity's animation. */
	protected Animation animation;
	/**Action this entity is currently executing*/
	public int currentAction;
	protected int previousAction;

	/**Set to true if the entity should face right. False if it has to be turned around*/
	public boolean facingRight;

	// movement
	/**Wether the entity is walking left or not*/
	public boolean left;
	/**Wether the entity is walking right or not*/
	public boolean right;
	/**Wether the entity is going up or not*/
	public boolean up;
	/**Wether the entity is going down or not*/
	public boolean down;
	/**Wether the entity is currently jumping or not*/
	public boolean jumping;
	/**Wether the entity is currently falling or not*/
	public boolean falling;

	// movement attributes
	/**Initial move speed. You speed up as you walk*/
	public double moveSpeed;
	/**Maximum of speed this entity can walk. Also affects jumping distance*/
	public double maxSpeed;
	/**Speed this entity will stop with. lower numbers result in a slower stop, thus sliding*/
	public double stopSpeed;
	/**Affects falling and jumping*/
	public double fallSpeed;
	public double maxFallSpeed;
	/**How high this entity can jump. Counts from a negative number up to 0*/
	public double jumpStart;
	public double stopJumpSpeed;

	/**used for flying entities. true will skip falling logic*/
	protected boolean ignoreGravity = false;

	public GameWorld world;

	/**Unique Item Name. Every entity needs a unique name that is used to save it, and reconstruct it when loading it back into the world.*/
	private final String UIN;

	protected Random rand = new Random();

	/**used to remove objects. if this is set to true, it will remove the entity from the entity list > simulate his death*/
	public boolean remove;
	
	// constructor
	public MapObject(TileMap tm, GameWorld world, String uin) {
		UIN = uin;

		tileMap = tm;
		tileSize = tm.getTileSize();
		this.world = world;
		currentAction = -1;

		if(hasAnimation()){
			animation = new Animation();
		}else{
			animation = new Animation();
			BufferedImage[] bi = new BufferedImage[]{getEntityTexture()};
			animation.setFrames(bi);
			animation.setDelay(Animation.NONE);
		}
	}

	public String getUin(){
		return UIN;
	}

	public Animation getAnimation(){
		return animation;
	}

	/**if this returns false, make sure you override getEntityTexture to set a non-animated texture*/
	public boolean hasAnimation(){
		return false;
	}

	/**
	 * Gets called only once to set the Animation texture sequence.
	 * Returns a new instance of the texture.
	 */
	protected BufferedImage getEntityTexture(){
		return this.animation.getDefaultAnimation()[0];
	}

	/**processes the surounding tiles so the entity can update it's falling/walking logic*/
	public void calculateCorners(double x, double y) {

		final int leftTile = (int) (x - (entitySizeX / 2)) / tileSize;
		final int rightTile = (int) ((x + (entitySizeX / 2)) - 1) / tileSize;
		final int topTile = (int) (y - (entitySizeY / 2)) / tileSize;
		final int bottomTile = (int) ((y + (entitySizeY / 2)) - 1) / tileSize;

		final int tl = tileMap.getType(topTile, leftTile);
		final int tr = tileMap.getType(topTile, rightTile);
		final int bl = tileMap.getType(bottomTile, leftTile);
		final int br = tileMap.getType(bottomTile, rightTile);

		topLeft = tl == Tile.SOLID;
		topRight = tr == Tile.SOLID;
		bottomLeft = bl == Tile.SOLID;
		bottomRight = br == Tile.SOLID;

	}

	public void checkTileMapCollision() {

		currentRow = (int) xScreen / tileSize;
		currentColumn = (int) yScreen / tileSize;

		if(currentRow < 0 || currentRow >= tileMap.getXRows() || currentColumn <0 || currentColumn >= tileMap.getYRows()){
			this.remove = true;
			System.out.println("The MapObject " + this.getUin() + " evaporated due to it being out of the map.");
			return;
		}

		xdest = xScreen + dx;
		ydest = yScreen + dy;

		xtemp = xScreen;
		ytemp = yScreen;

		calculateCorners(xScreen, ydest);
		if (dy < 0)
			if (topLeft || topRight) {
				dy = 0;
				ytemp = (currentColumn * tileSize) + (entitySizeY / 2);

			} else
				ytemp += dy;
		if (dy > 0)
			if (bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				ytemp = ((currentColumn + 1) * tileSize) - (entitySizeY / 2);
			} else
				ytemp += dy;

		calculateCorners(xdest, yScreen);
		if (dx < 0)
			if (topLeft || bottomLeft) {
				dx = 0;
				xtemp = (currentRow * tileSize) + (entitySizeX / 2);
			} else
				xtemp += dx;
		if (dx > 0)
			if (topRight || bottomRight) {
				dx = 0;
				xtemp = ((currentRow + 1) * tileSize) - (entitySizeX / 2);
			} else
				xtemp += dx;

		if (!falling) {
			calculateCorners(xScreen, ydest + 1);
			if (!bottomLeft && !bottomRight)
				falling = true;
		}
	}

	/**Draws the object with the default animation*/
	public void draw(Graphics2D g) {
		drawSprite(g, animation);
	}

	/**Draws the object with given animation*/
	public void draw(Graphics2D g, Animation anim) {
		drawSprite(g, anim);
	}

	/**
	 * draw the entity's sprite
	 * */
	private void drawSprite(Graphics2D g, Animation am) {

		setMapPosition();

		// draw

		if (facingRight)
			g.drawImage(am.getImage(),
					(int) ((xScreen + xmap) - (width / 2)),
					(int) ((yScreen + ymap) - (height / 2)), null);
		else
			g.drawImage(am.getImage(),
					(int) (((xScreen + xmap) - (width / 2)) + width),
					(int) ((yScreen + ymap) - (height / 2)), -width, height, null);

		if (getWorld().showBoundingBoxes) {
			g.setColor(Color.WHITE);
			g.draw(getRectangle());
		}
	}

	public int getCollisionHeight() {
		return entitySizeY;
	}

	public int getCollisionWidth() {
		return entitySizeX;
	}

	public void getNextPosition() {

		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed)
				dx = -maxSpeed;
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed)
				dx = maxSpeed;
		}

		if (falling && !ignoreGravity)
			dy += fallSpeed;
	}

	public Rectangle getRectangle() {
		return new Rectangle(
				(int) ((xScreen + xmap) - ((entitySizeX) / 2)),
				(int) ((yScreen + ymap) - (entitySizeY / 2)),
				entitySizeX, entitySizeY);
	}

	public int posX(){
		return (int) ((xScreen + xmap) - ((entitySizeX) / 2));
	}

	public int posY(){
		return (int) ((yScreen + ymap) - (entitySizeY / 2));
	}

	public int getScreenXpos() {
		return (int) xScreen;
	}

	public int getScreenYpos() {
		return (int) yScreen;
	}

	public int getTextureHeight() {
		return height;
	}

	public int getTextureWidth() {
		return width;
	}

	public GameWorld getWorld() {
		return world;
	}

	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}

	public boolean notOnScreen() {
		return ((xScreen + xmap + width) < 0)
				|| (((xScreen + xmap) - width) > GamePanel.WIDTH)
				|| ((yScreen + ymap + height) < 0)
				|| (((yScreen + ymap) - height) > GamePanel.HEIGHT);
	}

	public void setDown(boolean b) {
		down = b;
	}

	public void setJumping(boolean b) {
		jumping = b;
	}

	public void setMapPosition() {
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}

	public void setPosition(double x, double y) {
		this.xScreen = x;
		this.yScreen = y;
	}

	//facing has to be set manually in the entity class
	public void setLeft(boolean b) {
		left = b;
		if(right) 
			right = !b;
	}

	public void setRight(boolean b) {
		right = b;
		if(left) 
			left = !b;
	}

	public void setUp(boolean b) {
		up = b;
	}

	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void writeToSave(DataTag data){
		data.writeDouble("xmap", xmap);
		data.writeDouble("ymap", ymap);

		data.writeDouble("xScreen", xScreen);
		data.writeDouble("yScreen", yScreen);

		data.writeDouble("dirX", dx);
		data.writeDouble("dirY", dy);

		data.writeString("UIN", UIN);
	}

	public void readFromSave(DataTag data){
		xmap = data.readDouble("xmap");
		ymap = data.readDouble("ymap");

		xScreen = data.readDouble("xScreen");
		yScreen = data.readDouble("yScreen");

		dx = data.readDouble("dirX");
		dy = data.readDouble("dirY");

	}

	public void update(){
		updateAnimation();
	}

	protected void updateAnimation(){
		animation.update();
		if(remove)
			return;
	}

	/**
	 * @param player : the player in the world that hit the target
	 * @param mo : the mapObject that got hit 
	 */
	public void onEntityHit(EntityPlayer player, MapObject mo){

	}

	/**
	 * @param player : the player that should interact with this object
	 * @param mo : the mapObject that will be interacted with 
	 */
	public void interact(EntityPlayer player, MapObject mo){

	}
}
