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
		
		if(component.getItem().equals(Items.woodChip) && getMaterial() == EnumMaterial.WOOD){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 16, ToolModifier.DMG, 1));
			base.damageStack(-16);
		}
		else if(component.getItem().equals(Items.refinedStone) && getMaterial() == EnumMaterial.STONE){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 32, ToolModifier.DMG, 1));
			base.damageStack(-32);
		}
		else if(component.getItem().equals(Items.ingot) && getMaterial() == EnumMaterial.IRON){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 40, ToolModifier.DMG, 2));
			base.damageStack(-40);
			
		}else if(component.getItem().equals(Items.whetstone)){
			base.addModifier(new ToolModifier(ToolModifier.DMG, 4, ToolModifier.EFF, 1));
		}
	}
}
