package game.item.armor;

import game.item.ItemStack;
import game.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ItemBelt extends ItemArmor{

	public ItemBelt(String uin, String displayName, String armorTexture) {
		super(uin, displayName, armorTexture, EXTRA);
		
		animationLengthArray = new int[]{
				/*body idle*/1,
				/*body attack*/1,
				/*falling/jump*/1,
				/*running*/10,
			};
	}

	/**the amount of extra slots it adds to the player's inventory*/
	private int inventorySlots;

	/**the amount of extra slots it adds to the player's inventory*/
	public ItemBelt setInventorySlots(int nr){
		inventorySlots = nr;
		return this;
	}

	public int getInventorySlots(){
		return inventorySlots;
	}
	
	@Override
	public List<String> getInfo(ItemStack stack) {

		info = new ArrayList<String>();
		
		if(stack.getItem().equals(Items.belt_s))
			info.add("1/3 upgrades added");
		
		else if(stack.getItem().equals(Items.belt_l))
			info.add("3/3 upgrades added");
		
		else if(stack.getItem().equals(Items.belt_m))
			info.add("2/3 upgrades added");
		
		else
			info.add("0/3 upgrades added");
		
		return info;
	}
}
