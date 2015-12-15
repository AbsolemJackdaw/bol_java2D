package game.entity.block.breakable;

import game.World;
import game.block.Blocks;
import game.item.Items;


public class BlockGem extends BlockOre {

	public BlockGem(World world) {
		super(world, Blocks.GEM);
		setHealth(75);
		setOre(Items.gem_blue);
		setDropAmmount(rand.nextInt(2) + 1);
	}
}
