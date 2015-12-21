package game.entity.living.environement;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import engine.image.Images;
import engine.imaging.Animation;
import game.World;
import game.entity.Entity;

public class EntityExplosion extends EntityDeathAnim {

	BufferedImage[] img = Images.loadMultiImage("/entity/particles/explosion.png", 32, 0, 11);
	
	public EntityExplosion(World world) {
		this(world, 32);
	}
	
	private int size;
	
	public EntityExplosion(World world, int explosionRadius) {
		super(world, Entity.DEATHANIM_EXPLOSION);
		
		size = explosionRadius;

		getAnimation().setFrames(img);
		getAnimation().setDelay(50);
	}

	@Override
	protected void drawSprite(Graphics2D g, Animation am) {
		
		setMapPosition();

		// draw

		if (facingRight)
			g.drawImage(am.getImage(),
					(int) ((xScreen + xmap) - (size / 2)),
					(int) ((yScreen + ymap) - (size / 2)), size, size, null);
		else
			g.drawImage(am.getImage(),
					(int) (((xScreen + xmap) - (size / 2)) + size),
					(int) ((yScreen + ymap) - (size / 2)), -size, size, null);

		if (getWorld().showBoundingBoxes) {
			g.setColor(Color.WHITE);
			g.draw(getRectangle());
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		if(getAnimation().hasPlayedOnce())
			remove = true;
	}
}
