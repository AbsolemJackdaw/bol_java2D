package game.entity.living.environement;

import engine.image.Images;
import engine.imaging.Animation;
import game.World;
import game.util.Constants;
import game.util.Util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class EntityDeathParticle extends EntityDeathAnim{

	protected BufferedImage[] parts;
	protected int partID = -1;
	protected int timer = 180;
	private int rotation = 0;

	public EntityDeathParticle(World world, String uin) {
		super(world, uin);

//		reloadTexture();

		boolean right = rand.nextBoolean();
		this.right = right;
		this.left = !right;
		facingRight = right;

		entitySizeX = 16;
		entitySizeY = 16;

		width = 16;
		height = 16;

		initMoveSpeed(Constants.RANDOM.nextDouble()/5 + 0.2);  // inital walking speed. you speed up as you walk
		initMaxSpeed(Constants.RANDOM.nextDouble()/3 + 0.3); // change to jump farther and walk faster
		stopSpeed = 0.02;
		fallSpeed = 0.09; // affects falling and jumping
		maxFallSpeed = 4.0;
		jumpStart = -5;
		stopJumpSpeed = 0.3;
	}

	@Override
	public void reloadTexture() {
		parts = getParts();
		partID = rand.nextInt(getParts().length);

		if(!hasAnimation()){
			BufferedImage[] bi = new BufferedImage[]{getEntityTexture()};
			animation.setFrames(bi);
			animation.setDelay(Animation.NONE);
		}
	}
	protected BufferedImage[] getParts(){
		return Images.loadMultiImage("/entity/particles/death_anim.png", 16, 0, 8);
	}

	@Override
	public boolean hasAnimation() {
		return false;
	}

	@Override
	protected BufferedImage getEntityTexture() {
		return parts == null ? super.getEntityTexture() : parts[partID];
	}

	@Override
	public void update() {
		super.update();

		timer --;

		if(facingRight)
			rotation-=rand.nextInt(10)+5;
		else
			rotation+=rand.nextInt(10)+5;

		if(timer <= 0 || dy == 0 && !jumping && !falling || dx == 0){
			remove = true;
		}

		if (jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}

		// falling
		if (falling) {
			dy += fallSpeed;

			if (dy > 0)
				jumping = false;
			if ((dy < 0) && !jumping)
				dy += stopJumpSpeed;

			if (dy > maxFallSpeed){
				dy = maxFallSpeed;
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
	}

	@Override
	protected void drawSprite(Graphics2D g, Animation am) {
		setMapPosition();

		// draw

		if (facingRight)
			g.drawImage(Util.rotateImage(am.getImage(), rotation),
					(int) ((xScreen + xmap) - (width)),
					(int) ((yScreen + ymap) - (height)), width*2, height*2, null);
		else
			g.drawImage(Util.rotateImage(am.getImage(), rotation),
					(int) (((xScreen + xmap) - (width)) + width*2),
					(int) ((yScreen + ymap) - (height)), -(width*2), height*2, null);

		if (getWorld().showBoundingBoxes) {
			g.setColor(Color.WHITE);
			g.draw(getRectangle());
		}
	}


	@Override
	public boolean canPlayDeathAnimation() {
		return false;
	}
}
