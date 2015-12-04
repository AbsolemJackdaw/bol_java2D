package game.item;

import java.util.ArrayList;
import java.util.List;

public class ItemBelt extends ItemArmor{

	/**the amount of extra slots it adds to the player's inventory*/
	private int inventorySlots;

	public ItemBelt(String uin,String displayName) {
		super(uin, displayName, EXTRA);
	}

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
