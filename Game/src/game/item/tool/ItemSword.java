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
			base.addModifier(new ToolModifier(ToolModifier.DUR, 20, ToolModifier.DMG, 1));
			base.damageStack(-20);
		}
		else if(component.getItem().equals(Items.refinedStone) && getMaterial() == EnumMaterial.STONE){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 15, ToolModifier.DMG, 1));
			base.damageStack(-15);
		}
		else if(component.getItem().equals(Items.ingot) && getMaterial() == EnumMaterial.IRON){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 30, ToolModifier.DMG, 2));
			base.damageStack(-30);
			
		}else if(component.getItem().equals(Items.whetstone)){
			base.addModifier(new ToolModifier(ToolModifier.DMG, 3, ToolModifier.EFF, 1));
		}
	}
}
