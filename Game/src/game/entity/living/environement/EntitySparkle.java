package game.entity.living.environement;

import java.awt.Color;

import engine.game.GameWorld;
import engine.image.Images;

public class EntitySparkle extends EntityEnvironement {

	private int timer = 180;
	
	public EntitySparkle(GameWorld world, String uin) {
		super(world, uin);
		
		setEntityAnimation(Images.loadColoredMultiImage("/entity/sparkle.png", 8, 0, 4, new Color(0.3f, 0.4f, 0.9f)), 250);
		entitySizeX = entitySizeY = height = width = 8;
		
		fallSpeed = 0; //.00008;
		maxFallSpeed = 0; //.0008;
	}
	
	@Override
	public boolean hasAnimation() {
		return true;
	}
	
	@Override
	public void update() {
		super.update();
	
		if(animation.hasPlayedOnce())
			remove = true;
		
		timer --;
		if(timer <= 0){
			remove = true;
		}
	}
	
}
