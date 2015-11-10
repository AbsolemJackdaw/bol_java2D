package game.gui;

import engine.image.Images;
import engine.keyhandlers.KeyHandler;
import game.entity.block.breakable.BlockLight;
import game.entity.inventory.IInventory;
import game.entity.living.player.Player;
import game.item.ItemStack;
import game.util.Util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class GuiFire extends GuiContainer {

	private BlockLight fire;
	private BufferedImage img = Images.loadImage("/gui/camp_fire.png");

	public GuiFire(IInventory inv, Player p) {
		super(inv, p);

	}

	public GuiFire setBlock(BlockLight light){
		this.fire = light;
		return this;
	}

	@Override
	public void draw(Graphics2D g) {

		g.drawImage(img.getSubimage(0, 0, 100, 108), centerX - 50, centerY - 54 ,null);

		int time = fire.timer/60;
		int minutes = time/60;
		int seconds = time%60;
		String sec = seconds < 10 ? "0"+seconds : ""+seconds;

		g.drawString(minutes+":"+sec, centerX +26, centerY- 43);

		if(fire != null)
			if(fire.isLit())
				g.drawImage(img.getSubimage(100, 0, 50, 64), centerX - 46, centerY - 49 ,null);

		drawPlayerExtendedContainer(g, 0, 88, 22, 96, 50, -52, img);
		drawPlayerInventoryItems(g, 48, 18);

		ItemStack i = secondairyInventory.getStackInSlot(0);
		if(i != null){
			int x = centerX - 22;
			int y = centerY - 22;
			i.getItem().draw(g, x, y, i);
		}
		super.draw(g);
		fire.update();
	}

	@Override
	public int getFirstSlotLocationX() {
		return isNotPlayerInventory() ? centerX - 23 :centerX - 49;
	}

	@Override
	public int getFirstSlotLocationY() {
		return isNotPlayerInventory() ? centerY - 23: centerY + 17;
	}

	@Override
	public int getSlotSpacingX() {
		return isNotPlayerInventory() ? 49 : 18;
	}

	@Override
	public int getSlotSpacingY() {
		return isNotPlayerInventory() ? 0 : 18;
	}

	@Override
	public int rowsX() {
		return isNotPlayerInventory() ? 2 : 5;
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

		if(KeyHandler.isValidationKeyPressed()){
			if(isNotPlayerInventory()){
				if(slot_index == 0){
					if(secondairyInventory.getStackInSlot(0) != null){

						if(KeyHandler.shiftIsHeld()){
							playerInventory.setStackInNextAvailableSlot(secondairyInventory.getStackInSlot(0).copy());
							secondairyInventory.setStackInSlot(0, null);
						}else{
							ItemStack stack = secondairyInventory.getStackInSlot(0);
							ItemStack newStack = new ItemStack(stack.getItem(),1);
							playerInventory.setStackInNextAvailableSlot(newStack);
							Util.decreaseStack(secondairyInventory, 0, 1);
						}
					}
				}else{ // click the slot that is a stub for lighting logs
					if(fire != null){
						if(secondairyInventory.getStackInSlot(0) != null){
							ItemStack stack = secondairyInventory.getStackInSlot(0);
							fire.timer += stack.getItem().getFuelTimer();
							Util.decreaseStack(secondairyInventory, 0, 1);
						}
					}
				}
			}else{
				int slot = slotIndex[0]+ (slotIndex[1]*(rowsX()));
				if(playerInventory.getStackInSlot(slot) != null){
					ItemStack stack = playerInventory.getStackInSlot(slot);
					if(stack.getItem().isFuel()){
						
						if(KeyHandler.shiftIsHeld()){
							secondairyInventory.setStackInSlot(0, stack.copy());
							playerInventory.setStackInSlot(slot, null);
						}else{
							ItemStack newStack = new ItemStack(stack.getItem(), 1);
							secondairyInventory.setStackInSlot(0, newStack);
							Util.decreaseStack(playerInventory, slot, 1);
						}
					}
				}
			}
		}
	}

	@Override
	public int[] getToolTipWindowPosition() {
		return new int[]{centerX + 43, centerY + 15};
	}
}
