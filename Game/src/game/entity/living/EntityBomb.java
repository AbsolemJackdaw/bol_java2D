package game.entity.living;

import java.awt.Graphics2D;

import engine.game.GameWorld;
import engine.game.MapObject;
import engine.image.Images;
import engine.music.Music;
import game.World;
import game.entity.living.environement.EntityDeathAnim;
import game.entity.living.environement.EntityExplosion;
import game.entity.living.player.Player;

public class EntityBomb extends EntityLiving {

	private int blastPower;
	private int blastRadius;

	public EntityBomb(GameWorld world, String uin) {
		super(world, uin);

		initHealth(8f);

		getAnimation().setFrames(Images.loadMultiImage("/entity/bomb_sheet.png", 32, 0, 4));
		getAnimation().setDelay(150);

		entitySizeX = 16;
		entitySizeY = 16;

		width = 24;
		height = 24;

		initMoveSpeed(0.9);  // inital walking speed. you speed up as you walk
		initMaxSpeed(0); // change to jump farther and walk faster
		stopSpeed = 0.02;
		fallSpeed = 0.09; // affects falling and jumping
		maxFallSpeed = 4.0;
		jumpStart = -5;
		stopJumpSpeed = 0.3;

	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
	}

	@Override
	public boolean hasAnimation() {
		return true;
	}

	@Override
	public void update() {
		super.update();


		if(dy == 0 && !jumping && !falling || dx == 0){
			remove = true;
			Music.play("explode_"+rand.nextInt(7));
			hitSurroundingEntities();
			((World)world).shakeWorld();
		}

		if (jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}

		// falling
		if (falling) {
			dy += fallSpeed;

			if (dy > 0)
				jumping = false;
			if ((dy < 0) && !jumping)
				dy += stopJumpSpeed;

			if (dy > maxFallSpeed){
				dy = maxFallSpeed;
			}

		}
	}

	private void hitSurroundingEntities() {

		this.entitySizeX = this.entitySizeY = getBlastRadius();

		for(MapObject mo : getWorld().listWithMapObjects){
			if(!(mo instanceof EntityDeathAnim  || mo instanceof EntityBomb))
				if(this.getRectangle().intersects(mo.getRectangle())){
					mo.onEntityHit(getBlastPower());
				}
		}
	}

	@Override
	public EntityDeathAnim getDeathAnimation() {

		EntityExplosion explosion = new EntityExplosion((World) world, getBlastRadius());
		return explosion ;
	}

	public int getBlastPower(){
		return blastPower;
	}

	public void setBlastPower(int damage){
		blastPower = damage;
	}

	public int getBlastRadius(){
		return blastRadius;
	}

	public void setBlastRadius(int rad){
		blastRadius = rad;
	}


	//keep onEntityHit methods empty. 
	//prevents them from getting hurt
	@Override
	public void onEntityHit(float damage) {
	}
	@Override
	public void onEntityHit(Player player) {
	}
}
