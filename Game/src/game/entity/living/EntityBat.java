package game.entity.living;

import engine.image.Images;
import engine.imaging.Animation;
import game.World;
import game.entity.living.enemy.EntityEnemy;
import game.item.ItemStack;
import game.item.Items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class EntityBat extends EntityEnemy{

	private BufferedImage[] eye_RU;
	private BufferedImage[] eye_RD;
	private Animation eye;

	public EntityBat(World world, String uin) {
		super(world, uin);

		eye_RU = Images.loadMultiImage("/entity/bat/bat_eye_RU.png", 32, 0, 4);
		eye_RD = Images.loadMultiImage("/entity/bat/bat_eye_RD.png", 32, 0, 4);

		eye = new Animation();
		eye.setDelay(50);

		initHealth(12f);

		getAnimation().setFrames(Images.loadMultiImage("/entity/bat/bat.png", 32, 0, 4));
		getAnimation().setDelay(50);

		entitySizeX = 16+8;
		entitySizeY = 16+8;

		width = 32;
		height = 32;

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
	public ItemStack[] getDrops() {
		return new ItemStack[]{new ItemStack(Items.leather, rand.nextInt(3)+1)};

	}

	@Override
	public void update() {
		super.update();

		if(tileMap.getBlockID(currentRow, currentColumn) < 6)
			AI.swimOrFly(this);

		if(up && right || up && left)
			eye.setFrames(eye_RU);
		if(down && right || down && left)
			eye.setFrames(eye_RD);

		eye.setFrame(getAnimation().getFrame());
	}

	@Override
	public void getNextPosition() {
		super.getNextPosition();
		
		AI.getNextPositionFlying(this, 20);
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		
		if(getHurtTimer()%5 == 0)
			super.draw(g, eye);
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

		topLeft = tl > 6 ;
		topRight = tr > 6;
		bottomLeft = bl > 6;
		bottomRight = br > 6;
	}
	
	@Override
	public float getAttackDamage() {
		return 0.5f;
	}
}
