package game.entity.living.enemy;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import engine.image.Images;
import engine.imaging.Animation;
import game.World;
import game.item.ItemStack;
import game.item.Items;
import game.util.WeightedStack;


public class EntityBat extends EntityEnemy{

	protected BufferedImage[] eye_up;
	protected BufferedImage[] eye_down;
	protected Animation eye;

	public EntityBat(World world, String uin) {
		super(world, uin);

		eye_up = Images.loadMultiImage("/entity/bat/bat_eye_up.png", 32, 0, 4);
		eye_down = Images.loadMultiImage("/entity/bat/bat_eye_down.png", 32, 0, 4);

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
		knockBackForce = 2d;

		up = facingRight;
		down = !facingRight;

	}

	@Override
	public WeightedStack[] getDrops() {
		return new WeightedStack[]{
				new WeightedStack(new ItemStack(Items.leather, 1), 0.95),
				new WeightedStack(new ItemStack(Items.sam_eye, 1),0.05)
		};
	}

	@Override
	public void update() {
		super.update();

		if(tileMap.getBlockID(currentRow, currentColumn) < 8)
			AI.swimOrFly(this);

		if(up && right || up && left)
			eye.setFrames(eye_up);
		if(down && right || down && left)
			eye.setFrames(eye_down);

		eye.setFrame(getAnimation().getFrame());
	}

	@Override
	public void getNextPosition() {
		super.getNextPosition();

		if(!isAgressive())
			AI.getNextPositionFlying(this, 20);
		else
			AI.getNextPositionFlying(this, 2);

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
	
	@Override
	protected void doBasicMovement() {
	}
}
