package game.entity.block.breakable;

import game.World;
import game.block.Blocks;
import game.item.Items;


public class BlockIron extends BlockOre {

	public BlockIron(World world) {
		super(world, Blocks.IRON);
		setHealth(50);
		setOre(Items.iron);
		setDropAmmount(rand.nextInt(2) + 1);
	}
}
