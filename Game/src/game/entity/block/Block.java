package game.entity.block;

import static engine.keyhandlers.KeyHandler.SPACE;
import static engine.keyhandlers.KeyHandler.getKeyName;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import engine.game.MapObject;
import game.World;
import game.item.ItemStack;


public class Block extends MapObject{

	private int type;
	public final static int ROCK = 0;
	public final static int WOOD = 1;
	
	public Random rand = new Random();

	protected List<String> blockInfo = new ArrayList<String>();
	
	public Block(World world, String uin) {
		super(world, uin);
		width = 32;
		height = 32;

		entitySizeX = 32;
		entitySizeY = 32;

		moveSpeed = 0; 
		stopSpeed = 0;
		fallSpeed = 0.05;
		maxFallSpeed = 2;

		facingRight = true;

		blockInfo.add(getKeyName(SPACE)+ " to Harvest");
	}

	@Override
	public void update() {
		super.update();

		getNextPosition(); // needed for falling
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
	}

	@Override
	public void getNextPosition() {
		if (falling && !ignoreGravity)
			dy += fallSpeed;

		//TODO falling needs tweeking for blocks only under his position
		if(falling){
			for(MapObject obj : getWorld().listWithMapObjects){
				if(obj instanceof Block){
					Block b = (Block)obj;

					if(b != this){
						if(this.currentRow == b.currentRow){
							if(this.currentColumn+1 == b.currentColumn){
								if(this.yScreen+32 >= b.yScreen && b.isStackable()){
									falling = false;
									dy = 0;
									this.yScreen = b.yScreen-32;
								}
							}
						}
					}
				}
			}
		}
	}

	public ItemStack getDrop(){
		return null;
	}

	public boolean isStackable(){
		return true;
	}

	public MapObject setType(int type){
		this.type = type;
		return this;
	}

	public int getType(){
		return type;
	}
	
	public List<String> getBlockInfo(){
		return blockInfo;
	}
}
