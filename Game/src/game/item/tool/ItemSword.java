package game.item.tool;

import game.item.ItemStack;
import game.item.Items;

public class ItemSword extends ItemTool{

	public ItemSword(String uin, String displayName, EnumMaterial type) {
		super(uin, displayName, type);
	}

	@Override
	public void craftingCallBack(ItemStack component, ItemStack base) {
		if(!base.canAddModifier())
			return;
		
		if(component.getItem().equals(Items.stone) && getMaterial() == EnumMaterial.ROCK){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 5, ToolModifier.DMG, 1));
			base.damageStack(-5);
		}
		else if(component.getItem().equals(Items.refinedStone) && getMaterial() == EnumMaterial.STONE){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 10, ToolModifier.DMG, 2));
			base.damageStack(-10);
		}
		else if(component.getItem().equals(Items.iron) && getMaterial() == EnumMaterial.IRON){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 25, ToolModifier.DMG, 3));
			base.damageStack(-25);
			
		}else if(component.getItem().equals(Items.whetstone)){
			base.addModifier(new ToolModifier(ToolModifier.DMG, 5, ToolModifier.EFF, 2));
		}
	}
}
