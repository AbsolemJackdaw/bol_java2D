package game.entity.living;

import engine.image.Images;
import game.World;
import game.entity.EntityAI;
import game.item.ItemStack;
import game.item.Items;
import game.util.WeightedStack;

import java.awt.Graphics2D;


public class EntityPig extends EntityLiving{

	private EntityAI ai = new EntityAI();

	public EntityPig(World world, String uin) {
		super(world, uin);

		initHealth(8f);

		setEntityAnimation(Images.loadMultiImage("/entity/piggy.png", 32, 0, 4), 150);

		entitySizeX = 32;
		entitySizeY = 32;

		width = 32;
		height = 32;

		initMoveSpeed( 0.05 + rand.nextDouble());
		initMaxSpeed(0.5 + rand.nextDouble());

		stopSpeed = 0.1;
		fallSpeed = 0.15; // affects falling and jumping
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		boolean b = rand.nextBoolean();
		facingRight = b;
		right = b;
		left = !b;

	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
	}

	@Override
	public void update() {
		super.update();

		if(knockBack <= 0)
			ai.walkAroundRandomly(this);

		//		calmDown();
	}

	@Override
	public void checkTileMapCollision() {
		super.checkTileMapCollision();
	}

	@Override
	public void getNextPosition() {
		super.getNextPosition();
	}

	private ItemStack[] drops = new ItemStack[2];

	@Override
	public WeightedStack[] getDrops() {
		
		return new WeightedStack[]{
				new WeightedStack(drops[0] = new ItemStack(Items.meat_pig_raw, rand.nextInt(2)+1), 0.4),
				new WeightedStack(new ItemStack(Items.leather, rand.nextInt(3)+1), 0.6)
		};
	}

	@Override
	public String getEntityHitSound() {
		return "hitpig_" + (rand.nextInt(5)+1);
	}
}
