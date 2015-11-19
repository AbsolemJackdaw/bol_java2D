package game.entity.living.flying;

import engine.game.GameWorld;
import game.entity.living.EntityLiving;

public class EntityFlying extends EntityLiving {

	public EntityFlying(GameWorld world, String uin) {
		super(world, uin);
		
		moveSpeed = 0.07 + rand.nextDouble()/10;
		maxSpeed = 0.4 + rand.nextDouble(); 
		stopSpeed = 0.14;
		fallSpeed = 0.0; 
		maxFallSpeed = 0.0;
		jumpStart = 0;
		stopJumpSpeed = 0;

		boolean b = rand.nextBoolean();
		facingRight = b;
		right = b;
		left = !b;
		up = b;
		down = !b;
	}

	@Override
	public void update() {
		super.update();
		
		if(tileMap.getBlockID(currentRow, currentColumn) < 6){
			
		}else{
			
		}
		
	}
	
	
}
