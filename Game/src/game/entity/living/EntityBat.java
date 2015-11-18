package game.entity.living;

import engine.image.Images;
import engine.imaging.Animation;
import game.World;
import game.entity.living.flying.EntityFlying;
import game.item.ItemStack;
import game.item.Items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class EntityBat extends EntityFlying {

	private BufferedImage[] eye_LU;
	private BufferedImage[] eye_LD;
	private BufferedImage[] eye_RU;
	private BufferedImage[] eye_RD;
	private Animation eye;

	public EntityBat(World world, String uin) {
		super(world, uin);

		eye_LU = Images.loadMultiImage("/entity/bat/bat_eye_LU.png", 32, 0, 4);
		eye_LD = Images.loadMultiImage("/entity/bat/bat_eye_LD.png", 32, 0, 4);
		eye_RU = Images.loadMultiImage("/entity/bat/bat_eye_RU.png", 32, 0, 4);
		eye_RD = Images.loadMultiImage("/entity/bat/bat_eye_RD.png", 32, 0, 4);

		eye = new Animation();
		eye.setDelay(50);

		initHealth(12f);

		getAnimation().setFrames(Images.loadMultiImage("/entity/bat/bat.png", 32, 0, 4));
		getAnimation().setDelay(50);

		entitySizeX = 16+8;
		entitySizeY = 16+8;

		width = 32;
		height = 32;

	}

	@Override
	public ItemStack[] getDrops() {
		return new ItemStack[]{new ItemStack(Items.leather, rand.nextInt(3)+1)};

	}

	@Override
	public void update() {
		super.update();

		if(up && left)
			eye.setFrames(eye_LU);
		if(up && right)
			eye.setFrames(eye_RU);
		if(down && left)
			eye.setFrames(eye_LD);
		if(down && right)
			eye.setFrames(eye_RD);

		eye.setFrame(getAnimation().getFrame());
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		super.draw(g, eye);
	}
}
