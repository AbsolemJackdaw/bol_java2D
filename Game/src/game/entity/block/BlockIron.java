package game.entity.block;

import java.awt.image.BufferedImage;
import java.util.Random;

import engine.image.Images;
import engine.map.TileMap;
import game.World;
import game.item.ItemStack;
import game.item.ItemTool;
import game.item.Items;


public class BlockIron extends BlockBreakable {

	public BlockIron(TileMap tm, World world) {
		super(tm, world, Blocks.IRON, ItemTool.PICKAXE);
		setHealth(50);
	}

	@Override
	public BufferedImage getEntityTexture() {
		return Images.loadImage("/blocks/rock_iron.png");
	}
	
	@Override
	public boolean hasAnimation() {
		return false;
	}
	
	@Override
	public boolean isStackable() {
		return false;
	}
	
	@Override
	public ItemStack getDrop() {
		return new ItemStack(Items.iron, new Random().nextInt(2)+1);
	}
	
	@Override
	public boolean needsToolToMine() {
		return true;
	}
}
