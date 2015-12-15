package game.item.crafting;

import game.entity.living.player.Player;
import game.item.ItemStack;

public interface ICraftingItem {

	/**
	 * @param slot : the slot the 'crafted' is in
	 * @param crafted : the stack that is crafted from
	 */
	public void craft(Player player, ItemStack crafted, int slot);
	
	public boolean canCraft(ItemStack crafted);
}
