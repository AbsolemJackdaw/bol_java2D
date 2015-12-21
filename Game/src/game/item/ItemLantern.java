package game.item;

import engine.map.TileMap;
import engine.save.DataTag;
import game.World;
import game.entity.inventory.IInventory;
import game.entity.living.player.Player;

public class ItemLantern extends Item implements IInventory {

	public final int defaultBurnTime = -(60*60);

	public ItemLantern(String uin, String displayName) {
		super(uin, displayName);
		
		this.info.add("A small light in the darkness ... ");
		setStackDamage(defaultBurnTime);
	}

	@Override
	public void useItem(ItemStack item, TileMap map, World world, Player player,	int key) {

//		GuiLantern gui = new GuiLantern(this, player).setLantern(this);
//		world.displayGui(gui);

	}

	//***********************INVENTORY****************************//

	ItemStack[] inventory = new ItemStack[1];

	@Override
	public ItemStack[] getItems() {
		return inventory;
	}

	@Override
	public boolean hasStack(ItemStack stack) {
		return stack.getItem().equals(inventory[0].getItem());
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[0];
	}

	@Override
	public int getMaxSlots() {
		return 2;
	}

	@Override
	public boolean setStackInNextAvailableSlot(ItemStack item) {
		if(item.getItem().equals(Items.grease)){
			setStackInSlot(0, item);
			return true;
		}
		return false;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		if(stack != null)
			if(!stack.getItem().equals(Items.grease))
				return;

		if(inventory[0] == null)
			inventory[0] = stack;

		else if (stack == null && inventory[0] != null)
			inventory[0] = null;

		else if(inventory[0].getItem().equals(stack.getItem()))
			inventory[slot].stackSize += stack.stackSize;
		else
			System.out.println("something tried to replace the existing item" + inventory[slot].getItem().getUIN() +
					" by "+ stack.getItem().getUIN() );

	}

	@Override
	public void removeStack(int slot) {
		inventory[0] = null;
	}

	@Override
	public boolean hasStackInSlot(int slot) {
		return inventory[0] == null ? false : true;
	}

	@Override
	public IInventory getInventory() {
		return this;
	}

	@Override
	public int getSlotForStack(ItemStack stack) {
		return 0;
	}

	@Override
	public void writeToSave(DataTag tag) {
		super.writeToSave(tag);
	}

	@Override
	public void readFromSave(DataTag tag) {
		super.readFromSave(tag);
	}

	public boolean isLit(ItemStack stack) {
		return stack.getDamage() < 0;
	}

	public void setLit(ItemStack stack, boolean isLit) {
		stack.setDamage(0);
	}

	@Override
	public void update(Player player, ItemStack stack, int slot){
		
		//TODO change this ! 
		//logic should be handled in itemstack ...? or so. idk.
		//this is bound to affect all other instances of itemLantern as well
		
		if(stack.getDamage() < 0)
			if(isLit(stack))
				stack.setDamage(stack.getDamage()+1);
			else
				;
		else
			if(isLit(stack))
				setLit(stack, false);

	}

	@Override
	public boolean isUpdateAble() {
		return true;
	}
}
