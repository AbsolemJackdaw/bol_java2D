package game.entity.block.breakable;

import java.awt.image.BufferedImage;
import java.util.Random;

import engine.image.Images;
import engine.map.TileMap;
import game.World;
import game.entity.block.Blocks;
import game.item.ItemStack;
import game.item.ItemTool;
import game.item.Items;


public class BlockRock extends BlockBreakable{

	public BlockRock(TileMap tm, World world) {
		super(tm, world, Blocks.ROCK, ItemTool.PICKAXE);
		setHealth(15);
	}
	
	@Override
	public ItemStack getDrop() {
		return new ItemStack(Items.rock, new Random().nextInt(4)+1);
	}
	
	@Override
	public BufferedImage getEntityTexture() {
		return Images.loadImage("/blocks/rock.png");
	}
	
	@Override
	public boolean hasAnimation() {
		return false;
	}

}
