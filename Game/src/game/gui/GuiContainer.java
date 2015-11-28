package game.gui;

import engine.keyhandlers.KeyHandler;
import game.World;
import game.entity.inventory.IInventory;
import game.entity.living.player.Player;
import game.gui.container.Container;
import game.item.Item;
import game.item.ItemArmor;
import game.item.ItemBelt;
import game.item.ItemStack;
import game.util.Util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class GuiContainer extends Gui implements Container{

	protected IInventory secondairyInventory;
	protected IInventory playerInventory;

	protected static final int PLAYER_INVENTORY = 0;
	protected static final int BLOCK_INVENTORY = 1;

	protected int currentContainer = PLAYER_INVENTORY;

	/**Location the "cursor" currently is at, where 0 = x && 1 = y*/
	protected int[] slotIndex = new int[2];

	/**the little yellow square that indicates what slot is selected*/
	public static final Rectangle slotSelected = new Rectangle(17, 17);

	/**returns an integer => 0 && < getMaxSlots(). this is used to know what slot we have selected*/
	public int slot_index;

	public GuiContainer(IInventory blockInventory, Player p) {
		super(p.getWorld(), p);
		this.secondairyInventory = blockInventory;
		playerInventory = p;

		slotSelected.x = getFirstSlotLocationX();
		slotSelected.y = getFirstSlotLocationY();
	}

	public GuiContainer(World world, Player p) {
		super(world, p);

		secondairyInventory = null;
		playerInventory = p;

		slotSelected.x = getFirstSlotLocationX();
		slotSelected.y = getFirstSlotLocationY();
	}

	@Override
	public void draw(Graphics2D g){

		g.setColor(Color.yellow);
		g.draw(slotSelected);

		//updating function. doesnt draw anything
		slot_index = slotIndex[0]+ (slotIndex[1]*(rowsX()));

		drawToolTip(g);

	}

	protected void drawToolTip(Graphics2D g) {

		if(currentContainer == PLAYER_INVENTORY)
			if(playerInventory.getStackInSlot(slot_index) != null){
				ItemStack stack = playerInventory.getStackInSlot(slot_index);
				Util.drawToolTipWindow(g, getToolTipWindowPosition(), stack.getToolTip());
				Util.drawToolTipText(g, stack, getToolTipWindowPosition());
			}

		if(currentContainer == BLOCK_INVENTORY)
			if(secondairyInventory.getStackInSlot(slot_index) != null){
				ItemStack stack = secondairyInventory.getStackInSlot(slot_index);
				Util.drawToolTipWindow(g, getToolTipWindowPosition(), stack.getToolTip());

				Util.drawToolTipText(g, stack, getToolTipWindowPosition());
			}
	}

	@Override
	public int getFirstSlotLocationX() {
		return 0;
	}

	@Override
	public int getFirstSlotLocationY() {
		return 0;
	}

	@Override
	public int getSlotSpacingX() {
		return 0;
	}

	@Override
	public int getSlotSpacingY() {
		return 0;
	}

	@Override
	public void handleGuiKeyInput() {
		super.handleGuiKeyInput();

		if(KeyHandler.isLeftKeyHit()){
			if(slotIndex[0] > 0){
				slotSelected.x -=getSlotSpacingX();
				slotIndex[0]--;
			}
		}

		else if(KeyHandler.isRightKeyHit()){
			if(slotIndex[0] < (rowsX()-1)){
				slotSelected.x +=getSlotSpacingX();
				slotIndex[0]++;
			}
		}


		else if(KeyHandler.isUpKeyHit()){
			if(slotIndex[1] > 0){
				slotSelected.y -=getSlotSpacingY();
				slotIndex[1]--;
			}else if((slotIndex[1] == 0) && !isNotPlayerInventory()){
				currentContainer = BLOCK_INVENTORY;
				slotSelected.y = getFirstSlotLocationY();
				slotSelected.x = getFirstSlotLocationX();
				slotIndex[1] = slotIndex[0] = 0;
			}
		}

		else if(KeyHandler.isDownKeyHit()){
			if(slotIndex[1] < (rowsY()-1)){
				slotSelected.y +=getSlotSpacingY();
				slotIndex[1]++;
			}else if((slotIndex[1] == (rowsY()-1)) && isNotPlayerInventory()){
				currentContainer = PLAYER_INVENTORY;
				slotSelected.y = getFirstSlotLocationY();
				slotSelected.x = getFirstSlotLocationX();
				slotIndex[1] = slotIndex[0] = 0;

			}
		}
		else if (KeyHandler.isPressed(KeyHandler.JUNK)){
			if(isNotPlayerInventory() && secondairyInventory != null){
				if(secondairyInventory.getStackInSlot(slot_index) != null)
					secondairyInventory.setStackInSlot(slot_index, null);
			}else{
				int slot = slotIndex[0]+ (slotIndex[1]*(rowsX()));
				if(playerInventory.getStackInSlot(slot) != null)
					playerInventory.setStackInSlot(slot, null);
			}
		}
		containerItemSwappingLogic();
	}

	protected void containerItemSwappingLogic(){
		//put stack from one inventory to another
		if(KeyHandler.isValidationKeyPressed())
			if(isNotPlayerInventory() && secondairyInventory != null){
				if(secondairyInventory.getStackInSlot(slot_index) != null)
					
					if(KeyHandler.prevKeyState[KeyHandler.SHIFT]){
						
					}else{
						if(playerInventory.setStackInNextAvailableSlot(secondairyInventory.getStackInSlot(slot_index)))
							secondairyInventory.setStackInSlot(slot_index, null);
					}
					
					
			}else{
				int slot = slotIndex[0]+ (slotIndex[1]*(rowsX()));
				if(playerInventory.getStackInSlot(slot) != null)
					if(secondairyInventory.setStackInNextAvailableSlot(playerInventory.getStackInSlot(slot)))
						playerInventory.setStackInSlot(slot, null);
			}
	}

	protected boolean isNotPlayerInventory(){
		return currentContainer == BLOCK_INVENTORY;
	}

	@Override
	public int rowsX() {
		return 0;
	}

	@Override
	public int rowsY() {
		return 0;
	}

	protected void drawPlayerInventoryItems(Graphics2D g, int offsetX, int offsetY){
		int extra = 0;
		if(player.invArmor.getStackInSlot(ItemArmor.EXTRA) != null){
			ItemStack is = player.invArmor.getStackInSlot(ItemArmor.EXTRA);
			Item i = is.getItem();
			if(i instanceof ItemBelt){
				int dex = ((ItemBelt)i).getInventorySlots();
				extra = dex;
			}
		}
		for(int slot = 0; slot < 10 + extra; slot++){
			ItemStack i = player.getStackInSlot(slot);
			if(i != null){
				int x = slot < 5 ? (centerX - offsetX) + (slot*18) :
					slot >= 10 && slot < 15 ? (centerX - offsetX) + ((slot-10)*18):
						slot >= 15 && slot < 20 ? (centerX - offsetX) + ((slot-15)*18):
							slot >= 20 && slot < 25 ? (centerX - offsetX) + ((slot-20)*18):
								slot >= 25 && slot < 30 ? (centerX - offsetX) + ((slot-25)*18):
									(centerX - offsetX) + ((slot-5)*18);

								int y = slot < 5 ? centerY + offsetY :
									slot >= 10 && slot < 15 ? centerY + offsetY + 18*2:
										slot >= 15 && slot < 20 ? centerY + offsetY + 18*3:
											slot >= 20 && slot < 25 ? centerY + offsetY + 18*4:
												slot >= 25 && slot < 30 ? centerY + offsetY + 18*5:
													centerY + offsetY + 18;


												if(!i.getItem().isStackable() && i.getDamage() > 0){
													double dmg = (double)i.getDamage()/100.0d * 15.0d;
													g.setColor(Color.DARK_GRAY);
													g.drawRect(x,y+14, 15, 1);
													g.setColor(Color.GREEN);
													g.drawRect(x, y+14, (int)dmg, 1);
												}

												i.getItem().draw(g, x, y, i);
			}
		}
	}

	protected void drawPlayerExtendedContainer(Graphics2D g, int offsetX, int offsetY, int subX, int subY, int posX, int posY, BufferedImage img){

		int extra = 0;
		if(player.invArmor.getStackInSlot(ItemArmor.EXTRA) != null){
			ItemStack is = player.invArmor.getStackInSlot(ItemArmor.EXTRA);
			Item i = is.getItem();
			if(i instanceof ItemBelt){
				int dex = ((ItemBelt)i).getInventorySlots();
				extra = dex/5;
			}
		}

		for(int i = 0; i < extra; i++){
			g.drawImage(img.getSubimage(offsetX, offsetY, subY, subX), centerX - posX, centerY - posY + i*18 , null);
		}
	}

	protected int getExtraSlots(){

		if(player.invArmor.getStackInSlot(ItemArmor.EXTRA) != null){
			ItemStack is = player.invArmor.getStackInSlot(ItemArmor.EXTRA);
			Item i = is.getItem();
			if(i instanceof ItemBelt){
				int dex = ((ItemBelt)i).getInventorySlots();
				return dex / 5 ;

			}
		}
		return 0;
	}
}