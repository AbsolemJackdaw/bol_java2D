package game.item.tool;

import game.item.ItemStack;
import game.item.Items;

public class ItemSword extends ItemTool{

	public ItemSword(String uin, String displayName, EnumTool type) {
		super(uin, displayName, type);
	}

	@Override
	public void craftingCallBack(ItemStack component, ItemStack base) {
		super.craftingCallBack(component, base);
		
		if(component.getItem().equals(Items.stone) && type == EnumTool.ROCK){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 15, ToolModifier.DMG, 2));
			base.damageStack(-15);
		}
		else if(component.getItem().equals(Items.refinedStone) && type == EnumTool.STONE){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 20, ToolModifier.DMG, 3));
			base.damageStack(-20);
		}
		else if(component.getItem().equals(Items.iron) && type == EnumTool.IRON){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 50, ToolModifier.EFF, 6));
			base.damageStack(-50);
		}
	}
}
