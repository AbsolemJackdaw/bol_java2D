package game.entity.living.environement;

import java.awt.image.BufferedImage;

import engine.game.GameWorld;

public class EntityBlockBreak extends EntityDeathParticle {

	public EntityBlockBreak(GameWorld world, String uin) {
		super(world, uin);
		jumpStart = -3;
		
		width = height = entitySizeX = entitySizeY = rand.nextInt(8)+8;
	}
	
	private BufferedImage[] particles = null;
	
	public void setParticleTexture(BufferedImage img){
		
		particles = new BufferedImage[8];
		
		for(int i = 0; i < 8; i ++){
			particles[i] = img.getSubimage(rand.nextInt(img.getWidth()-width), rand.nextInt(img.getHeight()-height), width, height);
		}
	}
	
	@Override
	protected BufferedImage[] getParts() {
		return particles == null ? super.getParts() : particles;
	}

}
