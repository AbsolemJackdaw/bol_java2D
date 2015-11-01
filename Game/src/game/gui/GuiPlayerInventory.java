package game.gui;

import engine.image.Images;
import engine.keyhandlers.KeyHandler;
import engine.window.GamePanel;
import game.World;
import game.entity.living.player.Player;
import game.item.Item;
import game.item.ItemArmor;
import game.item.ItemBelt;
import game.item.ItemStack;
import game.item.ItemTool;
import game.item.Items;
import game.item.crafting.Crafting;
import game.util.Constants;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class GuiPlayerInventory extends GuiContainer {

	private BufferedImage img;

	private final int[] pos = new int[]{centerX + 72, centerY-4};

	public GuiPlayerInventory(World world, Player p) {
		super(world, p);

		img = Images.loadImage("/gui/playerGui.png");

		secondairyInventory = p.invArmor.getInventory();

		buttonList.add(new Button(centerX - 50, centerY + 13, Items.craftTable.getTexture()));
		buttonList.add(new Button(centerX - 18 - 50, centerY + 13, Items.stick.getTexture()));
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(img.getSubimage(35, 3, 150, 75), GamePanel.WIDTH/2 - 150/2, GamePanel.HEIGHT/2 - 75/2 ,null);

		g.drawImage(player.playerSheet.get(0)[0],  GamePanel.WIDTH/2 - 150/2 + 5,  GamePanel.HEIGHT/2 - 75/2 + 8, null);
		g.drawImage(player.playerSheet.get(1)[0],  GamePanel.WIDTH/2 - 150/2 + 5,  GamePanel.HEIGHT/2 - 75/2 + 8, null);
		g.drawImage(player.playerSheet.get(2)[0],  GamePanel.WIDTH/2 - 150/2 + 5,  GamePanel.HEIGHT/2 - 75/2 + 8, null);
		g.drawImage(player.playerSheet.get(3)[0],  GamePanel.WIDTH/2 - 150/2 + 5,  GamePanel.HEIGHT/2 - 75/2 + 8, null);

		int extra = 0;
		if(player.invArmor.getStackInSlot(ItemArmor.EXTRA) != null){
			ItemStack is = player.invArmor.getStackInSlot(ItemArmor.EXTRA);
			Item i = is.getItem();
			if(i instanceof ItemBelt){
				int dex = ((ItemBelt)i).getInventorySlots();
				extra = dex;
			}
		}
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
					double dmg = (double)stack.getDamage()/100.0d * 15.0d;

					g.setColor(Color.DARK_GRAY);
					g.drawRect(centerX - 26 + (18*slot),centerY-14, 15, 1);
					g.setColor(Color.GREEN);
					g.drawRect(centerX - 26 + (18*slot), centerY-14, (int)dmg, 1);
				}

				stack.getItem().draw(g, centerX - 26 + (18*slot), centerY-28, stack);
			}
		}

		for(Button b : buttonList){
			b.draw(g);
		}

		if(currentContainer == 2){
			int i = 0;
			for(ItemStack stack : Crafting.getRecipe(slot_index)){
				if(stack != null){
					stack.getItem().draw(g, centerX + 75 + ((i%3)*getSlotSpacingX()), centerY - 27 + ((i/3)*getSlotSpacingY()), stack);
					i++;
				}
			}
		}
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

	@Override
	public void handleGuiKeyInput() {

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

//			if(KeyHandler.isPressed(KeyHandler.PLACE) && XboxController.controller != null || KeyHandler.isValidationKeyPressed()){
//
//				if(player.getStackInSlot(slot_index) != null){
//
//					if(player.getStackInSlot(slot_index).getItem() != null){
//						//place down blocks
//						if(player.getStackInSlot(slot_index).getItem() instanceof ItemBlock){
//							ItemBlock ib = (ItemBlock)player.getStackInSlot(slot_index).getItem();
//							ib.placeBlock(player.getWorld().tileMap, player.getWorld(), player);
//
//							Util.decreaseStack(playerInventory, slot_index, 1);
//						}
//					}
//				}
//			}
		}

		if(slot_index == 5 && currentContainer != 2){
			if(!isNotPlayerInventory() ){
				if(KeyHandler.isLeftKeyHit()){
					currentContainer = 2;
					slotSelected.y = getFirstSlotLocationY();
					slotSelected.x = getFirstSlotLocationX();
					slotIndex[1] = 0;
					slotIndex[0] = 1;
				}
			}
		}

		else if(currentContainer == 2){
			if(KeyHandler.isPressed(KeyHandler.ESCAPE)){
				world.displayGui(null);
			}

			else if(KeyHandler.isLeftKeyHit()){
				if(slotIndex[0] == 1){
					slotSelected.x -=getSlotSpacingX();
					slotIndex[0]--;
				}
			}

			else if(KeyHandler.isRightKeyHit()){
				if(slotIndex[0] == 0){
					slotSelected.x +=getSlotSpacingX();
					slotIndex[0]++;
				}

				else if(slotIndex[0] == 1){
					currentContainer = PLAYER_INVENTORY;
					slotSelected.y = getFirstSlotLocationY() + getSlotSpacingY();
					slotSelected.x = getFirstSlotLocationX();
					slotIndex[0] = 0;
					slotIndex[1] = 1;

				}
			}

			else if(KeyHandler.isValidationKeyPressed())
				buttonClicked(slot_index);
		}
	}

	@Override
	protected void containerItemSwappingLogic() {

		if(secondairyInventory != null)
			if(KeyHandler.isValidationKeyPressed())

				//switch armor to player inventory
				if(isNotPlayerInventory() && secondairyInventory != null){ //armor inventory

					if(slot_index !=2){ // dont remove belts !
						if(secondairyInventory.getStackInSlot(slot_index) != null)
							if(playerInventory.setStackInNextAvailableSlot(secondairyInventory.getStackInSlot(slot_index)))
								secondairyInventory.setStackInSlot(slot_index, null);
					}else{
						//check if any space is left within the reachable inventory
						for(int i = 0; i < 10; i++){
							if(playerInventory.getStackInSlot(i) == null){
								player.setStackInSlot(i, secondairyInventory.getStackInSlot(slot_index).copy());
								secondairyInventory.setStackInSlot(slot_index, null);
								break;
							}
						}
					}
				}

		//inventory to armor logic
				else{
					int slot = slotIndex[0]+ (slotIndex[1]*(rowsX()));

					if(playerInventory.getStackInSlot(slot) != null){
						Item item = playerInventory.getStackInSlot(slot).getItem();
						ItemStack stack = playerInventory.getStackInSlot(slot);

						if(item.hasInventoryCallBack())
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
	}

	private void buttonClicked(int id){
		if(id == 0){
			Crafting.craft(player, Crafting.Sticks, false);
		}
		if(id == 1){
			Crafting.craft(player, Crafting.CraftTable, false);
		}
	}

	@Override
	public int[] getToolTipWindowPosition() {
		return pos;
	}
}
