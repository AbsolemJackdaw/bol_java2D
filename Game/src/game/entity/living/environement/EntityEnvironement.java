package game.entity.living.environement;

import engine.game.GameWorld;
import engine.save.DataTag;
import game.entity.living.EntityLiving;
import game.entity.living.player.Player;

import java.awt.Graphics2D;

public class EntityEnvironement extends EntityLiving{

	public EntityEnvironement(GameWorld world, String uin) {
		super(world, uin);

		entitySizeX = 32;
		entitySizeY = 32;

		width = 32;
		height = 32;

		//environement entities can only fall by basic
		initMoveSpeed(0.0);
		initMaxSpeed(0.0); 
		stopSpeed = 0;
		fallSpeed = 0.15; 
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
	public void onEntityHit(Player p) {
		//override to prevent killing
	}
	
	@Override
	public void onEntityHit(float damage) {
		//override to prevent killing
	}
	
	@Override
	public void update() {
		super.update();

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
