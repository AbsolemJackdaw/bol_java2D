package game.item;

import engine.map.TileMap;
import engine.save.DataTag;
import game.World;
import game.entity.living.player.Player;
import game.item.tool.ToolModifier;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class Item {

	private final String UIN;
	private final String displayName;

	private BufferedImage texture;

	private boolean cookable;
	private boolean fuel;
	private int fuelTimer;
	private int itemBaseDamage;

	protected List <String> info;

	public Item(String uin, String displayName){
		UIN = uin;
		this.displayName = displayName;
		
		info = new ArrayList<String>();
	}

	public Item setBaseDamage(int i){
		itemBaseDamage = i;
		return this;
	}

	public int getBaseDamage(){
		return itemBaseDamage;
	}
	
	public int getMaxDurability(ItemStack stack){
		return itemBaseDamage + stack.getBonus(ToolModifier.DUR);
	}

	public Item setTexture(BufferedImage img){
		texture = img;
		return this;
	}
	
	public BufferedImage getTexture(){
		return texture;
	}

	Font font = new Font("Century Gothic", Font.PLAIN, 10);

	public void draw(Graphics2D g, int x, int y, ItemStack stack){

		Image tmp = texture.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		g.drawImage(tmp, x, y, null);
		
		g.setFont(font);
		if(stack.stackSize < 10)
		{
			g.setColor(Color.DARK_GRAY);
			g.drawString(stack.stackSize+"", x+11, y+17);
			g.setColor(Color.white);
			g.drawString(stack.stackSize+"", x+10, y+16);
		}
		else
		{
			g.setColor(Color.DARK_GRAY);
			g.drawString(stack.stackSize+"", x+4, y+17);
			g.setColor(Color.white);
			g.drawString(stack.stackSize+"", x+3, y+16);
		}
	}

	public void writeToSave(DataTag tag){
	}

	public void readFromSave(DataTag tag){
	}

	public String getUIN(){
		return UIN;
	}

	public String getDisplayName(){
		return displayName;
	}

	public boolean isCookable(){
		return cookable;
	}

	public Item setCookable(){
		cookable = true;
		return this;
	}

	public boolean isFuel(){
		return fuel;
	}

	public Item setFuelTimer(int fuelTimer) {
		this.fuelTimer = fuelTimer;
		return this;
	}

	public int getFuelTimer() {
		if(isFuel())
			return fuelTimer;
		else
			return -1;
	}

	public Item setFuel(){
		fuel = true;
		return this;
	}

	/**called when the player presses the numbers on the keyboard to use an item*/
	public void useItem(ItemStack stack, TileMap map, World world, Player player, int key){
	}

	/**if the item can handle any logic set to true: it will call the update method*/
	public boolean isUpdateAble(){
		return false;
	}

	public boolean isStackable(){
		return true;
	}

	public void update(Player player, ItemStack stack, int slot){

	}

	public boolean hasInventoryCallBack(Player player){
		return false;
	}

	/**
	 * Called when player uses an item in inventory. this will omit the item from being switched to another container.
	 * implemented for upgrade means.
	 * @param slot : the slot this item is in
	 * @param player : the player
	 */
	public void inventoryCallBack(int slot, Player player){

	}
	
	/**
	 * Called when player crafts an item and no crafting recipe matched the two used stacks.
	 * The first stack is considered the item to be used on the second stack selected.
	 * @param component : the first itemstack selected
	 * @param base : the stack that can be changed
	 */
	public void craftingCallBack(ItemStack component, ItemStack base){
		if(!base.canAddModifier())
			return;
	}

	/**
	 * to read from tooltip, please acced ItemStack's tooltip !
	 */
	public List<String> getInfo(ItemStack stack){
		return null;
	}
	
	public boolean hasModifiers(){
		return false;
	}
	
	public int getModifiers(){
		return hasModifiers() ? 3 : 0;
	}
}
