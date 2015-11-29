package game.item;

import java.util.ArrayList;
import java.util.List;

import game.entity.living.player.Player;
import game.item.tool.ToolModifier;

public class ItemOvenbase extends Item {

	public ItemOvenbase(String uin, String displayName) {
		super(uin, displayName);
		modifiers = 5;
	}

	@Override
	public boolean isStackable() {
		return false;
	}
	@Override
	public boolean hasModifiers() {
		return false;
	}

	@Override
	public boolean isUpdateAble() {
		return true;
	}
	
	@Override
	public List<String> getInfo(ItemStack stack) {

		info = new ArrayList<String>();

		info.add("Stones : " + getUpgrades(stack) +"/5");
		
		return info;
	}
	
	private int getUpgrades(ItemStack stack){
		return stack.getBonus(ToolModifier.UPG);
	}
	
	@Override
	public void update(Player player , ItemStack stack, int slot) {
		
		if(stack.getBonus(ToolModifier.UPG) == 5)
		{
			ItemStack is = new ItemStack(Items.oven);

			player.setStackInSlot(slot, null);
			player.setStackInSlot(slot, is);
			
		}
	}
	
	@Override
	public void craftingCallBack(ItemStack component, ItemStack base) {
		super.craftingCallBack(component, base);
		
		if(component.getItem().equals(Items.refinedStone))
			base.addModifier(new ToolModifier(ToolModifier.UPG, 1));

	}

}