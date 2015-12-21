package game.entity.living.enemy;

import engine.image.Images;
import game.World;
import game.item.ItemStack;
import game.item.Items;
import game.util.WeightedStack;

public class EntityWorm extends EntityEnemyBlock {

	public EntityWorm(World world, String uin) {
		super(world, uin);

		initHealth(15f);

		getAnimation().setFrames(Images.loadMultiImage("/entity/worm/greaseworm.png", 32, 0, 3));
		getAnimation().setDelay(150);

		entitySizeX = entitySizeY = width = height = 32;

		initMaxSpeed(0.05 + rand.nextDouble());
		initMoveSpeed(0.5 + rand.nextDouble());

		stopSpeed = 0.1;
		fallSpeed = 0.15; // affects falling and jumping
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		attackDelay = 75;
	}
	
	@Override
	public boolean isAgressive() {
		return true;
	}
	
	@Override
	public float getAttackDamage() {
		return 0.75f;
	}

	@Override
	public WeightedStack[] getDrops(){
		return new WeightedStack[]{
				new WeightedStack(new ItemStack(Items.grease,1), 1.0)
		};
	}

}
