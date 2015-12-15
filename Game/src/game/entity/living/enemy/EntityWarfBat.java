package game.entity.living.enemy;

import engine.image.Images;
import game.World;
import game.item.ItemStack;
import game.item.Items;

public class EntityWarfBat extends EntityBat {

	public EntityWarfBat(World world, String uin) {
		super(world, uin);

		eye_up = Images.loadMultiImage("/entity/bat/warfbat_eye_up.png", 32, 0, 4);
		eye_down = Images.loadMultiImage("/entity/bat/warfbat_eye_down.png", 32, 0, 4);

		initHealth(20f);

		getAnimation().setFrames(Images.loadMultiImage("/entity/bat/warfbat.png", 32, 0, 4));
		getAnimation().setDelay(50);

		initMoveSpeed(0.02 + rand.nextDouble()/10);
		initMaxSpeed(0.5 + rand.nextDouble());
		
		attackDelay = 60;
	}
	
	@Override
	public boolean isAgressive() {
		return true;
	}

	@Override
	public ItemStack[] getDrops() {
		return new ItemStack[]{new ItemStack(Items.leather, rand.nextInt(2)+1), new ItemStack(Items.stache)} ;
	}
}
