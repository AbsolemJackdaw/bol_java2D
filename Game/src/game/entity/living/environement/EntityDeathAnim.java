package game.entity.living.environement;

import game.World;
import game.content.Images;
import game.content.save.DataTag;
import game.entity.MapObject;
import game.entity.living.EntityLiving;
import game.entity.living.player.Player;

import java.awt.Graphics2D;

import base.tilemap.TileMap;

public class EntityDeathAnim extends EntityLiving{

	public EntityDeathAnim(TileMap tm, World world) {
		super(tm, world, "DeathAnim");
		
		getAnimation().setFrames(Images.loadMultiImage("/entity/deathAnim.png", 32, 0, 8));
		getAnimation().setDelay(50);

		entitySizeX = 32;
		entitySizeY = 32;

		width = 32;
		height = 32;

		moveSpeed = 0.0; // inital walking speed. you speed up as you walk
		maxSpeed = 0; // change to jump farther and walk faster
		stopSpeed = 0;
		fallSpeed = 0.15; // affects falling and jumping
		maxFallSpeed = 4.0;
		jumpStart = 0;
		stopJumpSpeed = 0;

		boolean b = rand.nextBoolean();
		facingRight = b;
		right = b;
		left = !b;
	}

	@Override
	public void writeToSave(DataTag data) {
		super.writeToSave(data);
	}
	
	@Override
	public void readFromSave(DataTag data) {
		super.readFromSave(data);
	}
	
	@Override
	public void onEntityHit(Player p, MapObject mo) {
		//override to prevent killing
	}
	
	@Override
	public void update() {
		super.update();

		getNextPosition(); // needed for falling
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(getAnimation().hasPlayedOnce())
			this.remove = true;
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
	}
	@Override
	public boolean canPlayDeathAnimation() {
		return false;
	}
}
