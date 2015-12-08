package game.gui;

import engine.image.Images;
import engine.keyhandlers.KeyHandler;
import engine.window.GamePanel;
import engine.window.gameAid.Utility;
import game.World;
import game.entity.living.player.Player;
import game.item.Item;
import game.item.ItemArmor;
import game.item.ItemBelt;
import game.item.ItemStack;
import game.item.crafting.Crafting;
import game.item.tool.ItemTool;
import game.util.Constants;
import game.util.Util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class GuiPlayerInventory extends GuiContainer {

	private BufferedImage img;

	private final int[] pos = new int[]{centerX + 72, centerY-4};

	private int extra;

	private ArrayList<String> text;

	public GuiPlayerInventory(World world, Player p) {
		super(world, p);

		img = Images.loadImage("/gui/playerGui.png");

		secondairyInventory = p.invArmor.getInventory();

		calculateBeltSpace();

		text  = new ArrayList<String>();
	}

	private void calculateBeltSpace() {

		if(player.invArmor.getStackInSlot(ItemArmor.EXTRA) != null){
			ItemStack is = player.invArmor.getStackInSlot(ItemArmor.EXTRA);
			Item i = is.getItem();
			if(i instanceof ItemBelt){
				int dex = ((ItemBelt)i).getInventorySlots();
				extra = dex;
			}
		}	else{
			extra = 0;
		}
	}

	@Override
	public void draw(Graphics2D g) {

		g.drawImage(img.getSubimage(35, 3, 150, 75), GamePanel.WIDTH/2 - 150/2, GamePanel.HEIGHT/2 - 75/2 ,null);

		g.drawImage(player.playerSheet.get(0)[0],  GamePanel.WIDTH/2 - 150/2 + 5,  GamePanel.HEIGHT/2 - 75/2 + 8, null);
		g.drawImage(player.playerSheet.get(1)[0],  GamePanel.WIDTH/2 - 150/2 + 5,  GamePanel.HEIGHT/2 - 75/2 + 8, null);
		g.drawImage(player.playerSheet.get(2)[0],  GamePanel.WIDTH/2 - 150/2 + 5,  GamePanel.HEIGHT/2 - 75/2 + 8, null);
		g.drawImage(player.playerSheet.get(3)[0],  GamePanel.WIDTH/2 - 150/2 + 5,  GamePanel.HEIGHT/2 - 75/2 + 8, null);

		if(extra == 5)
			g.drawImage(img.getSubimage(79, 97, 102, 22), GamePanel.WIDTH/2 - 150/2 + 44, GamePanel.HEIGHT/2 - 75/2 +65 ,null);
		if(extra == 10)
			g.drawImage(img.getSubimage(79, 97, 102, 43), GamePanel.WIDTH/2 - 150/2 + 44, GamePanel.HEIGHT/2 - 75/2 +65 ,null);
		else if (extra == 20){
			g.drawImage(img.getSubimage(79, 97, 102, 43), GamePanel.WIDTH/2 - 150/2 + 44, GamePanel.HEIGHT/2 - 75/2 +65 ,null);
			g.drawImage(img.getSubimage(79, 97, 102, 43), GamePanel.WIDTH/2 - 150/2 + 44, GamePanel.HEIGHT/2 - 75/2 +101 ,null);
		}else if (extra == 15){
			g.drawImage(img.getSubimage(79, 97, 102, 43), GamePanel.WIDTH/2 - 150/2 + 44, GamePanel.HEIGHT/2 - 75/2 +65 ,null);
			g.drawImage(img.getSubimage(79, 97, 102, 22), GamePanel.WIDTH/2 - 150/2 + 44, GamePanel.HEIGHT/2 - 75/2 +101 ,null);
		}

		super.draw(g);

		drawPlayerInventoryItems(g, 26, -4);

		for(int slot = 0; slot < secondairyInventory.getMaxSlots(); slot++){
			ItemStack stack = secondairyInventory.getStackInSlot(slot);
			if(stack != null){
				if(!stack.getItem().isStackable() && stack.getDamage() > 0){
					double dmg = (double)stack.getDamage()/(double)stack.getMaxDamage() * 15.0d;

					g.setColor(Color.DARK_GRAY);
					g.drawRect(centerX - 26 + (18*slot),centerY-14, 15, 1);
					g.setColor(Color.GREEN);
					g.drawRect(centerX - 26 + (18*slot), centerY-14, (int)dmg, 1);
				}

				stack.getItem().draw(g, centerX - 26 + (18*slot), centerY-28, stack);
			}
		}

		if(text.isEmpty()){
			text.add(KeyHandler.getKeyName(KeyHandler.JUNK) + ":discard");
			text.add(KeyHandler.getKeyName(KeyHandler.CRAFT) + ":craft");
		}

		if(crafting){
		
			String info = "";
			for(String s : text)
				info += s;
			
			Utility.drawCenteredString(g, info, Constants.FONT_ITEMS, GamePanel.WIDTH/2 - 150/2 + 73, GamePanel.HEIGHT/2 - 75/2 + 5);
			
		}
		else
			Util.drawToolTipText(g, text, new int[]{GamePanel.WIDTH/2 - 150/2 - 1, GamePanel.HEIGHT/2 - 75/2 + 46}, null, null);

	}

	@Override
	public int getFirstSlotLocationX() {
		return currentContainer == 2 ? centerX - 51: GamePanel.WIDTH/2 - 150/2 + 48;
	}

	@Override
	public int getFirstSlotLocationY() {
		return currentContainer == 2 ? centerY + 12 : isNotPlayerInventory() ? GamePanel.HEIGHT/2 - 75/2+ 8 :GamePanel.HEIGHT/2 - 75/2 + 32;
	}

	@Override
	public int getSlotSpacingX() {
		return 18;
	}

	@Override
	public int getSlotSpacingY() {
		return 18;
	}

	@Override
	public int rowsX() {
		return isNotPlayerInventory() ? 4 : 5;
	}

	@Override
	public int rowsY() {
		return isNotPlayerInventory() ? 1 : 2+getExtraSlots();
	}

	private boolean crafting;
	private ItemStack[] craftables = new ItemStack[2];
	private int[] craftSlots = new int[2];

	@Override
	public void handleGuiKeyInput() {

		if(crafting && !isPlayerInventory()){
			crafting = false;
			text.clear();
			text.add(KeyHandler.getKeyName(KeyHandler.JUNK) + ":discard");
			text.add(KeyHandler.getKeyName(KeyHandler.CRAFT) + ":craft");
		}

		if(KeyHandler.isPressed(KeyHandler.CRAFT) && isPlayerInventory()){

			if(player.getStackInSlot(slot_index) != null && !crafting){

				crafting = true;

				text.clear();
				text.add("Combine "+ player.getStackInSlot(slot_index).getItem().getDisplayName() + " with ");
				text.add(player.getStackInSlot(slot_index).getItem().getDisplayName());
				
				craftables[0] = player.getStackInSlot(slot_index).copy();
				craftSlots[0] = slot_index;
			}

			else if(player.getStackInSlot(slot_index) != null && crafting){

				crafting = false;

				text.clear();
				text.add(KeyHandler.getKeyName(KeyHandler.JUNK) + ":discard");
				text.add(KeyHandler.getKeyName(KeyHandler.CRAFT) + ":craft");

				craftables[1] = player.getStackInSlot(slot_index).copy();
				craftSlots[1] = slot_index;

				craft();
			}

			else if(player.getStackInSlot(slot_index) == null && crafting){

				craftables = new ItemStack[2];
				craftSlots = new int[2];

				text.clear();
				text.add(KeyHandler.getKeyName(KeyHandler.JUNK) + ":discard");
				text.add(KeyHandler.getKeyName(KeyHandler.CRAFT) + ":craft");

				crafting = false;
			}
		}


		if(currentContainer != 2){
			super.handleGuiKeyInput();

			if(currentContainer == PLAYER_INVENTORY){

				if(playerInventory.getStackInSlot(slot_index) != null)
					for(int key : Constants.hotBarKeys)
						if(KeyHandler.isPressed(key)){

							int key_slot = key - KeyHandler.ONE;

							//switch items
							if(slot_index != key_slot){
								if(playerInventory.getStackInSlot(key_slot) != null){

									ItemStack copyOfHotBar = playerInventory.getStackInSlot(key_slot).copy();
									ItemStack copyOfSlot = playerInventory.getStackInSlot(slot_index).copy();

									playerInventory.setStackInSlot(key_slot, null);
									playerInventory.setStackInSlot(slot_index, null);

									player.setStackInSlot(key_slot, copyOfSlot);
									player.setStackInSlot(slot_index, copyOfHotBar);

								}else{
									player.setStackInSlot(key_slot, playerInventory.getStackInSlot(slot_index).copy());
									playerInventory.setStackInSlot(slot_index, null);
								}
							}else{
								if(player.invArmor.getExtra() != null){
									ItemStack hotBarCopy = playerInventory.getStackInSlot(key_slot).copy();

									for(int i = 10; i < player.getInventory().getMaxSlots(); i++){
										if(player.getStackInSlot(i) == null){
											player.setStackInSlot(i, hotBarCopy);
											player.setStackInSlot(key_slot, null);
											break;
										}
									}
								}
							}
						}
			}
		}
	}

	@Override
	protected void containerItemSwappingLogic() {

		if(secondairyInventory != null)
			if(KeyHandler.isValidationKeyPressed())

				//switch armor to player inventory
				if(isNotPlayerInventory() && secondairyInventory != null){ //armor inventory
					if(slot_index != 2){ // dont remove belts !
						if(secondairyInventory.getStackInSlot(slot_index) != null)
							if(playerInventory.setStackInNextAvailableSlot(secondairyInventory.getStackInSlot(slot_index)))
								secondairyInventory.setStackInSlot(slot_index, null);
					}
				}

		//inventory to armor logic
				else{
					int slot = slotIndex[0]+ (slotIndex[1]*(rowsX()));

					if(playerInventory.getStackInSlot(slot) != null){
						Item item = playerInventory.getStackInSlot(slot).getItem();
						ItemStack stack = playerInventory.getStackInSlot(slot);

						if(item.hasInventoryCallBack(player))
							item.inventoryCallBack(slot_index, player);

						else if(item instanceof ItemTool){
							if(secondairyInventory.getStackInSlot(3) == null){//3 is weapon slot
								secondairyInventory.setStackInSlot(3, stack);
								playerInventory.setStackInSlot(slot, null);
							}else{
								ItemStack a = playerInventory.getStackInSlot(slot);
								ItemStack b = secondairyInventory.getStackInSlot(3);

								playerInventory.removeStack(slot);
								secondairyInventory.removeStack(3);

								if(playerInventory.setStackInNextAvailableSlot(b))//set tool to next available slot to occupy any spacing
									secondairyInventory.setStackInSlot(3, a);
								else{//switch out items
									playerInventory.setStackInSlot(slot, b);
									secondairyInventory.setStackInSlot(3, a);
								}

							}
						}else if(item instanceof ItemArmor){
							ItemArmor armor = (ItemArmor)item;
							int index = armor.getIndex();

							if(secondairyInventory.getStackInSlot(index) == null){//set item to corresponding armor slot
								secondairyInventory.setStackInSlot(index, stack);
								playerInventory.setStackInSlot(slot, null);//set moved stack in player inventory to null
							}else{
								ItemStack a = playerInventory.getStackInSlot(slot);
								ItemStack b = secondairyInventory.getStackInSlot(index);

								playerInventory.removeStack(slot);
								secondairyInventory.removeStack(index);

								if(playerInventory.setStackInNextAvailableSlot(b))//set tool to next available slot to occupy any spacing
									secondairyInventory.setStackInSlot(index, a);
								else{//switch out items
									playerInventory.setStackInSlot(slot, b);
									secondairyInventory.setStackInSlot(index, a);
								}
							}
						}
					}
				}
		calculateBeltSpace();
	}

	@Override
	public int[] getToolTipWindowPosition() {
		return pos;
	}

	private void craft(){
		Crafting.craft(player, craftables, craftSlots);
	}

	@Override
	public void update() {

		for(int i = 0; i < player.getInventory().getItems().length; i++){
			ItemStack stack = player.getInventory().getItems()[i];
			if(stack != null){
				Item item = stack.getItem();
				if(item.isUpdateAble())
					item.update(player, stack, i);
			}
		}
		
		if(crafting){
			if(playerInventory.getStackInSlot(slot_index) != null){
				String name = playerInventory.getStackInSlot(slot_index).getItem().getDisplayName();
				text.set(1, name);
			}else{
				text.set(1, "...");
			}
		}
	}
}