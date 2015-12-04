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
		
		if(component.getItem().equals(Items.stone) && type == EnumTool.ROCK){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 5, ToolModifier.DMG, 1));
			base.damageStack(-5);
		}
		else if(component.getItem().equals(Items.refinedStone) && type == EnumTool.STONE){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 10, ToolModifier.DMG, 2));
			base.damageStack(-10);
		}
		else if(component.getItem().equals(Items.iron) && type == EnumTool.IRON){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 25, ToolModifier.EFF, 6));
			base.damageStack(-25);
		}
	}
}
