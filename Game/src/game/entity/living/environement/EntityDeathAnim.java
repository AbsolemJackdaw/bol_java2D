package game.entity.living.environement;

import engine.image.Images;
import engine.map.TileMap;
import game.World;


public class EntityDeathAnim extends EntityEnvironement{

	public EntityDeathAnim(TileMap tm, World world, String s) {
		super(tm, world, s);
		
		getAnimation().setFrames(Images.loadMultiImage("/entity/deathAnim.png", 32, 0, 8));
		getAnimation().setDelay(50);

	}
}
