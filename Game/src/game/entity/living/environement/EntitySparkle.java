package game.entity.living.environement;

import java.awt.Color;

import engine.image.Images;
import game.World;

public class EntitySparkle extends EntityEnvironement {

	private int timer = 180;
	
	public EntitySparkle(World world, String uin) {
		this(world, uin, new Color(0.3f, 0.4f, 0.9f));
	}
	
	public EntitySparkle(World world, String uin, Color c) {
		super(world, uin);
		
		setEntityAnimation(Images.loadColoredMultiImage("/entity/particles/sparkle.png", 8, 0, 4, c), 250);
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
