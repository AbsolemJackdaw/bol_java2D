package game.item.tool;

import game.item.ItemStack;
import game.item.Items;

public class ItemSword extends ItemTool{

	public ItemSword(String uin, String displayName, EnumTool type) {
		super(uin, displayName, type);
	}

	@Override
	public void craftingCallBack(ItemStack component, ItemStack base) {

		if(!base.canAddModifier())
			return;
		
		if(component.getItem().equals(Items.rock) && type == EnumTool.ROCK){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 15, ToolModifier.DMG, 2));
		}
		else if(component.getItem().equals(Items.iron) && type == EnumTool.IRON){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 50, ToolModifier.EFF, 6));
		}
	}
}
