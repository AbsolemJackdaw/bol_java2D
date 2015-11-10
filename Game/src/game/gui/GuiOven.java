package game.gui;

import engine.image.Images;
import engine.keyhandlers.KeyHandler;
import game.entity.block.breakable.BlockOven;
import game.entity.inventory.IInventory;
import game.entity.living.player.Player;
import game.item.ItemStack;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class GuiOven extends GuiContainer {

	private BlockOven oven;
	private BufferedImage gui = Images.loadImage("/gui/oven.png");

	public GuiOven(IInventory blockInv, Player p) {
		super(blockInv, p);
	}

	public void setOven(BlockOven oven ){
		this.oven = oven;	
	}

	@Override
	public void draw(Graphics2D g) {

		g.drawImage(gui.getSubimage(0, 0, 125, 90), centerX - 150/2, centerY - 75/2 ,null);

		drawPlayerExtendedContainer(g, 30, 69, 19, 92, 45, -50, gui);
		drawPlayerInventoryItems(g, 43, 15);

		for(int slot = 0; slot < secondairyInventory.getInventory().getItems().length; slot++){
			ItemStack i = secondairyInventory.getStackInSlot(slot);
			if(i != null){

				int x = slot < 5 ? (centerX - 53) + (slot*32) : (centerX - 43) + ((slot-5)*18);
				int y = slot < 5 ? centerY - 18 : centerY;

				i.getItem().draw(g, x, y, i);
			}
		}



		if(oven.timer > 0){
			int fireTimer = oven.currentFuelTimer;
			float x = (((float)oven.timer/(float)fireTimer)*32f);
			g.drawImage(gui.getSubimage(128, (int)x, 15, 32), centerX-73, centerY+16+(int)x, null);

		}

		super.draw(g);

		oven.update();
	}

	@Override
	public int getFirstSlotLocationX() {
		return isNotPlayerInventory() ? centerX-54  :centerX - 44;
	}

	@Override
	public int getFirstSlotLocationY() {
		return isNotPlayerInventory() ? centerY - 19 : centerY + 14;
	}

	@Override
	public int getSlotSpacingX() {
		return isNotPlayerInventory() ? 32 : 18;
	}

	@Override
	public int getSlotSpacingY() {
		return isNotPlayerInventory() ? 32 : 18;
	}

	@Override
	public int rowsX() {
		return isNotPlayerInventory() ? 3 : 5;
	}

	@Override
	public int rowsY() {
		return isNotPlayerInventory() ? 1 : 2 + getExtraSlots();
	}


	@Override
	public void handleGuiKeyInput() {
		super.handleGuiKeyInput();
	}

	@Override
	protected void containerItemSwappingLogic() {

		if(secondairyInventory != null)
			if(KeyHandler.isValidationKeyPressed())
				if(isNotPlayerInventory() && secondairyInventory != null){
					if(secondairyInventory.getStackInSlot(slot_index) != null){

						if(secondairyInventory.getStackInSlot(slot_index).stackSize > 1){

							if(KeyHandler.shiftIsHeld()){
								if(player.setStackInNextAvailableSlot(secondairyInventory.getStackInSlot(slot_index)))
									secondairyInventory.setStackInSlot(slot_index, null);
							}else{
								ItemStack stack = secondairyInventory.getStackInSlot(slot_index);
								stack.stackSize--;
								ItemStack oneStack = new ItemStack(stack.getItem(), 1);
								playerInventory.setStackInNextAvailableSlot(oneStack);
							}
						}else{
							if(playerInventory.setStackInNextAvailableSlot(secondairyInventory.getStackInSlot(slot_index)))
								secondairyInventory.setStackInSlot(slot_index, null);
						}
					}
				}else{
					int slot = slotIndex[0]+ (slotIndex[1]*(rowsX()));

					if(playerInventory.getStackInSlot(slot) != null){
						ItemStack stack = playerInventory.getStackInSlot(slot);

						if(stack.getItem().isCookable() || stack.getItem().isFuel()){

							if(KeyHandler.shiftIsHeld()){
								if(secondairyInventory.setStackInNextAvailableSlot(stack.copy()))
									playerInventory.setStackInSlot(slot, null);
							}else{
								ItemStack newStack = new ItemStack(stack.getItem(), 1);
								if(secondairyInventory.setStackInNextAvailableSlot(newStack)){
									stack.stackSize--;
									if(stack.stackSize <= 0){
										playerInventory.setStackInSlot(slot, null);
									}
								}
							}
						}
					}
				}
	}

	@Override
	public int[] getToolTipWindowPosition() {
		return new int[]{centerX + 48, centerY + 13};
	}
}
