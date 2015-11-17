package game.entity.living.water;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import engine.game.GameWorld;
import engine.image.Images;
import engine.save.DataTag;
import game.item.ItemStack;
import game.item.Items;

public class EntityFish extends EntityWaterCreature {

	private int meta;

	public EntityFish(GameWorld world, String uin) {
		super(world, uin);

		meta = rand.nextInt(5);

		entitySizeX = width = getEntitySize().width;
		entitySizeY = height = getEntitySize().height;

		//reload texture as the meta is set afterwards
		BufferedImage[] img = new BufferedImage[]{getEntityTexture()};
		animation.setFrames(img);
		
		initHealth(3);

	}

	@Override
	protected BufferedImage getEntityTexture() {

		BufferedImage img = null;
		
		if(meta == 0)
			img = Images.loadImage("/entity/fish/honorfish.png");
		else
			img = Images.loadImage("/entity/fish/fish_"+meta+".png");



		return img;
	}

	private Dimension getEntitySize(){

		switch (meta) {
		case 0:
			return new Dimension(23, 11);
		case 1:
			return new Dimension(26, 21);
		case 2:
			return new Dimension(25, 12);
		case 3:
			return new Dimension(26, 16);
		case 4:
			return new Dimension(21, 11);
		}

		return new Dimension(16, 16);
	}

	@Override
	public void readFromSave(DataTag data) {
		super.readFromSave(data);
		meta = data.readInt("meta");
	}

	@Override
	public void writeToSave(DataTag data) {
		super.writeToSave(data);
		data.writeInt("meta", meta);
	}
	
	@Override
	public ItemStack[] getDrops() {
		return new ItemStack[]{new ItemStack(Items.meat_fish_raw, 1)};
	}
}
