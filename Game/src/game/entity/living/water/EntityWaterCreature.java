package game.entity.living.water;

import engine.game.GameWorld;
import game.entity.living.EntityLiving;

public class EntityWaterCreature extends EntityLiving {

	public EntityWaterCreature(GameWorld world, String uin) {
		super(world, uin);
		
		initMoveSpeed(0.07 + rand.nextDouble()/10);
		initMaxSpeed(0.4 + rand.nextDouble());
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
		
		if(tileMap.getBlockID(currentRow, currentColumn) == 10 || tileMap.getBlockID(currentRow, currentColumn) == 9){
			
			AI.swimOrFly(this);
			
		}else{
			
			//TODO set splashing, jumpstart, and random left or right.
			//being out of the water shouldn't occur though
			
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
	
	@Override
	public void calculateCorners(double x, double y) {

		final int leftTile = (int) (x - (entitySizeX / 2)) / tileSize;
		final int rightTile = (int) ((x + (entitySizeX / 2)) - 1) / tileSize;
		final int topTile =  (int) (y - (entitySizeY / 2)) / tileSize;
		final int bottomTile = (int) ((y + (entitySizeY / 2)) - 1) / tileSize;

		final int tl = tileMap.getBlockID(leftTile, topTile);
		final int tr = tileMap.getBlockID(rightTile, topTile);
		final int bl = tileMap.getBlockID(leftTile, bottomTile);
		final int br = tileMap.getBlockID(rightTile, bottomTile);

		topLeft = tl > 10 || tl < 9;
		topRight = tr < 9 || tr > 10;
		bottomLeft = bl < 9 || bl > 10;
		bottomRight = br < 9 || br > 10;
	}
}
