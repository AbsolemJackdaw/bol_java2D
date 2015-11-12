package game.entity.block.breakable;

import engine.image.Images;
import game.World;
import game.entity.block.Blocks;
import game.item.ItemStack;
import game.item.ItemTool;
import game.item.Items;

import java.awt.image.BufferedImage;
import java.util.Random;


public class BlockIron extends BlockBreakable {

	public BlockIron(World world) {
		super(world, Blocks.IRON, ItemTool.PICKAXE);
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
