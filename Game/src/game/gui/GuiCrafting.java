package game.gui;

import engine.image.Images;
import engine.keyhandlers.KeyHandler;
import game.World;
import game.entity.living.player.Player;
import game.item.ItemStack;
import game.item.crafting.Crafting;
import game.util.Util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class GuiCrafting extends GuiContainer {

	private boolean advanced;

	private final int[] pos = new int[]{centerX + 50, centerY+18};

	public GuiCrafting(World world, Player p, boolean advanced) {
		super(world, p);
		this.advanced = advanced;

		BufferedImage[] textures = new BufferedImage[15];
		for(int i = 0; i < 15; i ++){
			if(!advanced){
				if(Crafting.result(i) != null)
					textures[i] = Crafting.result(i).getItem().getTexture();
			}else
				if(Crafting.resultAdvanced(i) != null)
					textures[i] = Crafting.resultAdvanced(i).getItem().getTexture();
		}

		for(int i = 0; i < rowsX(); i ++){
			for(int j = 0; j < rowsY(); j++){
				buttonList.add(new Button(centerX - 40 + i*getSlotSpacingX(), centerY - 45 + j*getSlotSpacingY(), textures[i+ (j*(rowsX()))]));
			}
		}
	}

	private void buttonClicked(int id){
		Crafting.craft(player, id, advanced);
	}

	@Override
	public void draw(Graphics2D g) {
		BufferedImage img = Images.loadImage("/gui/crafting.png");

		g.drawImage(img.getSubimage(0, 0, 96, 106), centerX - (96/2), centerY - (106/2),96,106,null);

		for(Button b : buttonList){
			b.draw(g);
		}

		drawPlayerExtendedContainer(g, 8, 72, 18, 92, 40, -55, img);
		drawPlayerInventoryItems(g, 40, 19);

		int i = 0;

		if(!advanced)
			for(ItemStack stack : Crafting.getRecipe(slot_index)){
				if(stack != null){
					stack.getItem().draw(g, centerX + 51 + ((i%3)*getSlotSpacingX()), centerY - 45 + ((i/3)*getSlotSpacingY()), stack);
					i++;
				}
			}
		else
			for(ItemStack stack : Crafting.getRecipeAdvanced(slot_index)){
				if(stack != null){
					stack.getItem().draw(g, centerX + 51 + ((i%3)*getSlotSpacingX()), centerY - 45 + ((i/3)*getSlotSpacingY()), stack);
					i++;
				}
			}

		super.draw(g);
	}

	@Override
	public int getFirstSlotLocationX() {
		return centerX - 41;
	}

	@Override
	public int getFirstSlotLocationY() {
		return centerY - 46;
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
	public void handleGuiKeyInput() {
		if(KeyHandler.isPressed(KeyHandler.ESCAPE)){
			world.displayGui(null);
		}

		if(KeyHandler.isLeftKeyHit())
			if(slotIndex[0] > 0){
				slotSelected.x -=getSlotSpacingX();
				slotIndex[0]--;
			}

		if(KeyHandler.isRightKeyHit())
			if(slotIndex[0] < (rowsX()-1)){
				slotSelected.x +=getSlotSpacingX();
				slotIndex[0]++;
			}

		if(KeyHandler.isUpKeyHit())
			if(slotIndex[1] > 0){
				slotSelected.y -=getSlotSpacingY();
				slotIndex[1]--;
			}

		if(KeyHandler.isDownKeyHit())
			if(slotIndex[1] < (rowsY()-1)){
				slotSelected.y +=getSlotSpacingY();
				slotIndex[1]++;
			}

		if(KeyHandler.isValidationKeyPressed())
			buttonClicked(slot_index);

	}

	@Override
	public int rowsX() {
		return 5;
	}

	@Override
	public int rowsY() {
		return 3;
	}
	
	@Override
	protected void drawToolTip(Graphics2D g) {
		if(Crafting.result(slot_index) != null){
			ItemStack stack = Crafting.result(slot_index);
			Util.drawToolTipWindow(g, getToolTipWindowPosition(), stack.getItem().getToolTip(stack));
			Util.drawToolTipText(g, stack, getToolTipWindowPosition());
		}
	}
	
	@Override
	public int[] getToolTipWindowPosition() {
		return pos ;
	}

}
