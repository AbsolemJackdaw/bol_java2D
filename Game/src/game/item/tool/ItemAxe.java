package game.item.tool;

import game.item.ItemStack;
import game.item.Items;

public class ItemAxe extends ItemTool {

	public ItemAxe(String uin, String displayName, EnumMaterial material) {
		super(uin, displayName, material);
	}

	@Override
	public void craftingCallBack(ItemStack component, ItemStack base) {

		super.craftingCallBack(component, base);
		
		if(component.getItem().equals(Items.woodChip) && getMaterial() == EnumMaterial.WOOD){
			mod = 24;
			base.addModifier(new ToolModifier(ToolModifier.DUR, mod, ToolModifier.EFF, 1));
			base.damageStack(-mod);
		}
		else if(component.getItem().equals(Items.refinedStone) && getMaterial() == EnumMaterial.STONE){
			mod=24;
			base.addModifier(new ToolModifier(ToolModifier.DUR, mod, ToolModifier.EFF, 1));
			base.damageStack(-mod);
		}
		else if(component.getItem().equals(Items.ingot) && getMaterial() == EnumMaterial.IRON){
			mod = 32;
			base.addModifier(new ToolModifier(ToolModifier.DUR, mod, ToolModifier.EFF, 2));
			base.damageStack(-mod);
		}
	}

}
