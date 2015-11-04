package engine.game.entity;

import java.util.ArrayList;
import java.util.List;

import engine.game.GameWorld;
import engine.game.MapObject;
import engine.keyhandlers.KeyHandler;
import engine.map.TileMap;

public class EntityPlayer extends EntityLiving{

	protected EntityMovement movement = new EntityMovement();

	public boolean isCollidingWithBlock;

	protected List<MapObject> collidingEntities = new ArrayList<MapObject>();

	public EntityPlayer(TileMap tm, GameWorld world) {
		super(tm, world, "player");
	}

	@Override
	public void getNextPosition() {
		
		movement.doPlayerMovement(this);
	}

	public void handleInput(){
		
		setLeft(KeyHandler.isLeftKeyPressed());

		setRight(KeyHandler.isRightKeyPressed());

		setUp(KeyHandler.isUpKeyPressed());

		setDown(KeyHandler.isDownKeyPressed());
		
	}
	
	@Override
	public void update() {

		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
	}
	
	/**
	 * Returns list with blocks the player is currently colliding with
	 */
	public List<MapObject> getCollidingMapObjects(){
		return collidingEntities;
	}

	/**
	 *Adds a block to the list of blocks the player is colliding with 
	 */
	public void setCollidingMapObjects(MapObject obj){
		collidingEntities.add(obj);
	}
}
