package game.item;

import game.entity.living.player.Player;
import game.item.crafting.ICraftingItem;
import game.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemGemHammer extends Item implements ICraftingItem{

	private final ArrayList<Item> craftables = new ArrayList<Item>(Arrays.asList(Items.leather_fine, Items.ingot, Items.woodChip));

	public ItemGemHammer(String uin, String displayName) {
		super(uin, displayName);
	}

	@Override
	public List<String> getInfo(ItemStack stack) {
		info = new ArrayList<String>();

		info.add("Compacts 3 identical materials");
		info.add("into a new material.");

		return info;
	}

	@Override
	public void craft(Player player, ItemStack crafted, int slot){

		if(player.setStackInNextAvailableSlot(crafting(crafted.getItem()))){
			Util.decreaseStack(player, slot, 3);
		}else{
			ItemStack stack = player.getStackInSlot(slot).copy();
			Util.decreaseStack(player, slot, 3);
			if(!player.setStackInNextAvailableSlot(crafting(crafted.getItem()))){
				player.setStackInSlot(slot, new ItemStack(stack.getItem(), 3));
			}
		}
	}

	@Override
	public boolean canCraft(ItemStack crafted) {
		return crafted != null && crafted.stackSize >= 3 && craftables.contains(crafted.getItem());
	}

	private ItemStack crafting(Item item){

		if(item.equals(craftables.get(0))){
			return new ItemStack(Items.plate_leather);
		}else if(item.equals(craftables.get(1))){
			return new ItemStack(Items.plate_iron);
		}else if(item.equals(craftables.get(2))){
			return new ItemStack(Items.plate_wood);
		}
		

		return null;
	}
}
