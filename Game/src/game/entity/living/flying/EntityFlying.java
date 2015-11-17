package game.entity.living.flying;

import engine.game.GameWorld;
import game.entity.EntityLiving;
import game.entity.living.player.Player;

public class EntityFlying extends EntityLiving {

	private int moveTimerY = 0;
	private int moveTimerYCalculator = 0;
	private int moveTimerX = 0;
	private int moveTimerXCalculator = 0;

	public EntityFlying(GameWorld world, String uin) {
		super(world, uin);
		
		entitySizeX = 32;
		entitySizeY = 32;

		width = 32;
		height = 32;

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
		
		if(tileMap.getBlockID(currentRow, currentColumn) == 1){
			
			if(dy == 0){
				if(moveTimerYCalculator == 0){
					moveTimerYCalculator = moveTimerY;
				}
				
				if(moveTimerYCalculator - moveTimerY >= 20){
					up = !up;
					down = !down;
					moveTimerYCalculator = 0;
				}
			}
			
			if(dx == 0){
				if(moveTimerXCalculator == 0){
					moveTimerXCalculator = moveTimerX;
				}
				
				if(moveTimerXCalculator - moveTimerX >= 20){
					left = !left;
					right = facingRight = !right;
					moveTimerXCalculator = 0;
				}
			}
			
			if(moveTimerY == 0){
				moveTimerY = rand.nextInt(300) + 300;
				
				if(rand.nextInt(2) == 0){
					up = !up;
					down = !down;
					moveTimerYCalculator = 0;
				}
			}
			
			if(moveTimerX == 0){
				moveTimerX = rand.nextInt(400) + 200;
				
				if(rand.nextInt(2) == 0){
					left = !left;
					right = facingRight = !right;
					moveTimerXCalculator = 0;
				}
			}
			
			moveTimerY--;
			moveTimerX--;
			
		}else{
			
		}
		
	}
	
	@Override
	public void getNextPosition() {
		
		super.getNextPosition();
		
		if(up && rand.nextInt(50) == 0){
			dy -= moveSpeed;
			if(dy < -maxSpeed)
				dy = -maxSpeed;
		}else if (down && rand.nextInt(50) == 0){
			dy += moveSpeed;
			if(dy > maxSpeed)
				dy = maxSpeed;
		}
	}
}
