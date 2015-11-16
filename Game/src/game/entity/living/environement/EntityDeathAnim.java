package game.entity.living.environement;

import engine.image.Images;
import game.World;


public class EntityDeathAnim extends EntityEnvironement{

	public EntityDeathAnim(World world, String s) {
		super(world, s);
		
		getAnimation().setFrames(Images.loadMultiImage("/entity/deathAnim.png", 32, 0, 8));
		getAnimation().setDelay(50);

	}
}
