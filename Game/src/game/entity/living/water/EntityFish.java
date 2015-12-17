package game.entity.living.water;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import engine.game.GameWorld;
import engine.image.Images;
import engine.save.DataTag;
import game.item.ItemStack;
import game.item.Items;
import game.util.WeightedStack;

public class EntityFish extends EntityWaterCreature {

	private int meta = -1;

	public EntityFish(GameWorld world, String uin) {
		super(world, uin);

		meta = rand.nextInt(7);

		initFish();

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
		case 5:
			return new Dimension(21, 19);
		case 6:
			return new Dimension(32, 13);
		}

		return new Dimension(16, 16);
	}

	@Override
	public void readFromSave(DataTag data) {
		super.readFromSave(data);
		meta = data.readInt("meta");
		initFish();
	}

	@Override
	public void writeToSave(DataTag data) {
		super.writeToSave(data);
		data.writeInt("meta", meta);
	}

	@Override
	public WeightedStack[] getDrops() {
		return new WeightedStack[]{
				new WeightedStack(new ItemStack(Items.meat_fish_raw, 1),1.0)
		};
	}

	private void initFish(){
		entitySizeX = width = getEntitySize().width;
		entitySizeY = height = getEntitySize().height;

		reloadTexture();
	}
}
