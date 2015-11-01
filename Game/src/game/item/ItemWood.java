package game.item;

import engine.image.Images;
import game.entity.block.Blocks;

import java.awt.image.BufferedImage;

public class ItemWood extends ItemBlock{

	public ItemWood() {
		super(Blocks.LOG, "Log");
	}
	
	@Override
	public BufferedImage getTexture() {
		return Images.loadImage("/blocks/log.png");
	}

}
