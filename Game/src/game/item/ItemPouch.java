package game.item;

import game.entity.living.player.Player;
import game.util.Util;

import java.awt.event.KeyEvent;

import base.main.keyhandler.KeyHandler;

public class ItemPouch extends Item {

	public ItemPouch(String uin,String displayName) {
		super(uin, displayName);

		tooltipList.add("Used to upgrade your Belt.");
		tooltipList.add(KeyEvent.getKeyText(KeyHandler.registeredKeys[KeyHandler.SPACE]) + " to use");

	}

	@Override
	public boolean hasInventoryCallBack() {
		return true;
	}

	@Override
	public void inventoryCallBack(int slot, Player player) {

		ItemStack stack = player.invArmor.getStackInSlot(2);

		if(stack != null){
			if(stack.getItem() instanceof ItemBelt){

				ItemBelt belt = (ItemBelt)stack.getItem();

				int slots = belt.getInventorySlots();

				if(slots == Items.belt.getInventorySlots()){

					player.invArmor.setStackInSlot(2, null);
					player.invArmor.setStackInSlot(2, new ItemStack(Items.belt_s));
					Util.decreaseStack(player.getInventory(), slot, 1);
					return;

				}else if(slots == Items.belt_s.getInventorySlots()){

					player.invArmor.setStackInSlot(2, null);
					player.invArmor.setStackInSlot(2, new ItemStack(Items.belt_m));
					Util.decreaseStack(player.getInventory(), slot, 1);
					return;

				}else if(slots == Items.belt_m.getInventorySlots()){

					player.invArmor.setStackInSlot(2, null);
					player.invArmor.setStackInSlot(2, new ItemStack(Items.belt_l));
					Util.decreaseStack(player.getInventory(), slot, 1);
					return;
				}
			}
		}
	}
}
