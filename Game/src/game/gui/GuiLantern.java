package game.gui;

import game.content.Images;
import game.entity.inventory.IInventory;
import game.entity.living.player.Player;
import game.item.ItemLantern;
import game.item.ItemStack;
import game.item.Items;
import game.util.Util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import base.main.GamePanel;
import base.main.keyhandler.KeyHandler;

public class GuiLantern extends GuiContainer {

	ItemLantern lantern;
	BufferedImage img = Images.loadImage("/gui/lantern.png");

	public GuiLantern(IInventory inv, Player p) {
		super(inv, p);

	}

	public GuiLantern setLantern(ItemLantern lantern){
		this.lantern = lantern;
		return this;
	}

	@Override
	public void draw(Graphics2D g) {

		g.drawImage(img.getSubimage(0, 0, 100, 108), GamePanel.WIDTH/2 - 150/2, GamePanel.HEIGHT/2 - 75/2 ,null);

		int time = lantern.burnTime/60;
		int minutes = time/60;
		int seconds = time%60;
		String sec = seconds < 10 ? "0"+seconds : ""+seconds;

		g.drawString(minutes+":"+sec, centerX +1, centerY-25);

		if(lantern != null)
			if(lantern.isLit())
				g.drawImage(img.getSubimage(100, 0, 48, 62), GamePanel.WIDTH/2 - 128/2, GamePanel.HEIGHT/2 - 68/2 ,null);

		drawPlayerExtendedContainer(g, 0, 89, 37, 92, 75, -70, img);
		drawPlayerInventoryItems(g, 73, 35);

		ItemStack i = secondairyInventory.getStackInSlot(0);
		if(i != null){
			int x = centerX - 48;
			int y = centerY - 5;
			i.getItem().draw(g, x, y, i);
		}
		super.draw(g);
		lantern.update();
	}

	@Override
	public int getFirstSlotLocationX() {
		return isNotPlayerInventory() ? centerX-49  :centerX - 2 - (72);
	}

	@Override
	public int getFirstSlotLocationY() {
		return isNotPlayerInventory() ? centerY - 6: centerY + 34;
	}

	@Override
	public boolean pausesGame() {
		return true;
	}

	@Override
	public int getSlotSpacingX() {
		return isNotPlayerInventory() ? 50 : 18;
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
				}else{ //slot used to light the lamp
					if(lantern != null){
						if(lantern.getStackInSlot(0) != null){

							lantern.burnTime += lantern.defaultBurnTime;
							Util.decreaseStack(secondairyInventory, 0, 1);

							if(!lantern.isLit())
								lantern.setLit(true);
						}else{ // if lamp stack is empty, it can be toggled on or off !
							if(lantern.isLit()){
								lantern.setLit(false);
								return;
							}
							if(!lantern.isLit() && lantern.burnTime > 0){
								lantern.setLit(true);
								return;
							}
						}
					}
				}
			}else{
				int slot = slotIndex[0]+ (slotIndex[1]*(rowsX()));
				if(playerInventory.getStackInSlot(slot) != null){
					ItemStack stack = playerInventory.getStackInSlot(slot);
					if(stack.getItem().equals(Items.grease)){
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
		return new int[]{275, 183};
	}

}
