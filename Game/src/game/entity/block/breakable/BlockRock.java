package game.entity.block.breakable;

import engine.image.Images;
import game.World;
import game.entity.block.Blocks;
import game.item.ItemStack;
import game.item.Items;
import game.item.tool.ItemTool.EnumTools;

import java.awt.image.BufferedImage;
import java.util.Random;


public class BlockRock extends BlockBreakable{

	public BlockRock( World world) {
		super(world, Blocks.ROCK, EnumTools.PICKAXE);
		setHealth(15);
	}
	
	@Override
	public ItemStack getDrop() {
		return new ItemStack(Items.stone, new Random().nextInt(2)+1);
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
