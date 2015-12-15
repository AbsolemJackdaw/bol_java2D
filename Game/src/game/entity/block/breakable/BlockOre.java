package game.entity.block.breakable;

import game.World;
import game.item.Item;
import game.item.ItemStack;
import game.item.tool.ItemTool.EnumTools;

public class BlockOre extends BlockBreakable{

	private Item ore = null;
	private int drop = 1;
	
	public BlockOre(World world, String uin) {
		super(world, uin, EnumTools.PICKAXE);
		
		blockInfo.add("pickaxe required");
	}
	
	public BlockOre setOre(Item ore) {
		this.ore = ore;
		return this;
	}
	
	@Override
	public boolean needsToolToMine() {
		return true;
	}
	
	@Override
	public boolean hasAnimation() {
		return false;
	}
	
	@Override
	public boolean isStackable() {
		return false;
	}
	
	public void setDropAmmount(int quant){
		drop = quant;
	}
	
	@Override
	public ItemStack getDrop() {
		return new ItemStack(ore, drop);
	}

}
