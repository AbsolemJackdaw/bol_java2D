package game.entity.living;

import java.awt.Color;
import java.awt.Graphics2D;

import engine.game.GameWorld;
import engine.image.Images;
import engine.imaging.Animation;
import game.entity.EntityAI;
import game.entity.living.player.Player;
import game.item.ItemStack;
import game.item.Items;

public class EntityWorm extends EntityLiving {

	private boolean flicker;
	private int flickerTimer = 100;

	public EntityWorm(GameWorld world, String uin) {
		super(world, uin);

		initHealth(2f);

		getAnimation().setFrames(Images.loadMultiImage("/entity/greaseworm.png", 32, 0, 3));
		getAnimation().setDelay(150);

		entitySizeX = 32;
		entitySizeY = 32;

		width = 32;
		height = 32;

		moveSpeed = 0.05 + rand.nextDouble();  // inital walking speed. you speed up as you walk
		maxSpeed = 0.5 + rand.nextDouble(); // change to jump farther and walk faster
		stopSpeed = 0.1;
		fallSpeed = 0.15; // affects falling and jumping
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		boolean b = rand.nextBoolean();
		facingRight = b;
		right = b;
		left = !b;

		up = down = false;
	}

	@Override
	public void draw(Graphics2D g) {
		if(flicker)
			flickerTimer++;
		if(flickerTimer > 50){
			flickerTimer = 0;
			flicker = false;
		}
		if(flickerTimer % 5 == 0){
			super.draw(g);
		}

	}

	@Override
	public void kill(Player p)
	{
		if(p != null)
			if(p.setStackInNextAvailableSlot(new ItemStack(Items.grease, rand.nextInt(3)+1))){
				this.remove = true;
			}else{
				health = maxHealth;
			}
		else
			super.kill(p);
	}

	@Override
	public boolean hasAnimation() {
		return true;
	}

	private EntityAI ai = new EntityAI();

	@Override
	public void update() {
		super.update();

		//		ai.walkAroundRandomly(this);
		ai.crawlAroundEverywhere(this);
	}

	@Override
	public void getNextPosition() {

		if(up){
			dy-= fallSpeed;
			if (dx < -fallSpeed)
				dx = -fallSpeed;
		}

		else if(down){
			dy+= fallSpeed;
			if (dx > fallSpeed)
				dx = fallSpeed;
		}

		if(right){
			dx+= moveSpeed;
			if (dx > maxSpeed)
				dx = maxSpeed;
		}

		else if(left){
			dx-= moveSpeed;
			if (dx < -maxSpeed)
				dx = -maxSpeed;
		}

	}

	@Override
	public void calculateCorners(double x, double y) {

		final int leftTile = (int) (x - (entitySizeX / 2)) / tileSize;
		final int rightTile = (int) ((x + (entitySizeX / 2)) - 1) / tileSize;
		final int topTile = (int) (y - (entitySizeY / 2)) / tileSize;
		final int bottomTile = (int) ((y + (entitySizeY / 2)) - 1) / tileSize;

		final int l2 = tileMap.getBlockID(leftTile, topTile);
		final int r2 = tileMap.getBlockID(rightTile, topTile);
		final int t2 = tileMap.getBlockID(leftTile, bottomTile);
		final int b2 = tileMap.getBlockID(rightTile, bottomTile);

		topLeft = l2 != 0;
		topRight = r2 != 0;
		bottomLeft = t2 != 0;
		bottomRight = b2 != 0;


	}

	@Override
	protected void drawSprite(Graphics2D g, Animation am) {

		setMapPosition();

		// draw

		if(up){
			if (facingRight)
				g.drawImage(am.getImage(),
						(int) ((xScreen + xmap) - (width / 2)),
						(int) ((yScreen + ymap) - (height / 2))+height,width, -height, null);
			else
				g.drawImage(am.getImage(),
						(int) (((xScreen + xmap) - (width / 2)) + width),
						(int) ((yScreen + ymap) - (height / 2))+height, -width, -height, null);

		}else{
			if (facingRight)
				g.drawImage(am.getImage(),
						(int) ((xScreen + xmap) - (width / 2)),
						(int) ((yScreen + ymap) - (height / 2)), null);
			else
				g.drawImage(am.getImage(),
						(int) (((xScreen + xmap) - (width / 2)) + width),
						(int) ((yScreen + ymap) - (height / 2)), -width, height, null);

		}

		if (getWorld().showBoundingBoxes) {
			g.setColor(Color.WHITE);
			g.draw(getRectangle());
		}
	}
}
