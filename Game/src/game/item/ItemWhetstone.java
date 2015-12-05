package game.item;

import java.util.ArrayList;
import java.util.List;

public class ItemWhetstone extends Item {

	public ItemWhetstone(String uin, String displayName) {
		super(uin, displayName);
	}

	@Override
	public List<String> getInfo(ItemStack stack) {
		
		info = new ArrayList<String>();
		
		info.add("Sharpens Swords");
		
		return info;
	}

}
