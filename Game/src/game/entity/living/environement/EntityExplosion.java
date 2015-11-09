package game.entity.living.environement;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import engine.image.Images;
import engine.imaging.Animation;
import engine.map.TileMap;
import game.World;
import game.entity.Entity;
import game.entity.living.player.Player;

public class EntityExplosion extends EntityDeathAnim {

	BufferedImage[] img = Images.loadMultiImage("/entity/explosion.png", 32, 0, 11);
	
	public EntityExplosion(TileMap tm, World world) {
		this(tm, world, 32);
	}
	
	private int size;
	
	public EntityExplosion(TileMap tm, World world, int explosionRadius) {
		super(tm, world, Entity.DEATHANIM_EXPLOSION);
		
		size = explosionRadius;

		getAnimation().setFrames(img);
		getAnimation().setDelay(50);
	}
	

	//keep onEntityHit methods empty. 
	//prevents them from getting hurt
	@Override
	public void onEntityHit(float damage) {
	}
	
	@Override
	public void onEntityHit(Player p) {
	}
	
	@Override
	public boolean canPlayDeathAnimation() {
		return false;
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
}