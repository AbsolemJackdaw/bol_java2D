package game.entity.block;

import static engine.keyhandlers.KeyHandler.INTERACT;
import static engine.keyhandlers.KeyHandler.getKeyName;
import engine.image.Images;
import engine.map.TileMap;
import engine.save.DataTag;
import game.World;
import game.entity.Animation;
import game.entity.MapObject;
import game.entity.inventory.IInventory;
import game.entity.living.player.Player;
import game.gui.GuiFire;
import game.item.ItemStack;
import game.item.ItemTool;
import game.item.Items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class BlockLight extends BlockBreakable implements IInventory{

	public int lightRadius;

	public int timer = 0;

	Animation fire = new Animation();

	public BlockLight(TileMap tm, World world, String uin) {
		super(tm, world, uin, ItemTool.NOTHING);
		setHealth(2);
		fire.setFrames(Images.loadMultiImage("/blocks/camp_fire.png", 32, 0, 4));
		fire.setDelay(90);
		
		blockInfo.add(getKeyName(INTERACT) + " to interact");
	}

	public Block setRadius(int rad){
		lightRadius = rad;
		return this;
	}

	public int getRadius(){
		int t = timer == 0 ? 0 : (timer > (800 * 6) && timer < (800*8)) ? 200 : timer > (800*3) && timer < (800*6) ? 175 : timer > 0 && timer < (800*3) ? 75 : 250;
		return  t;
	}

	@Override
	public BufferedImage getEntityTexture() {
		return Items.campfire.getTexture();
	}

	@Override
	public ItemStack getDrop() {
		return new ItemStack(Items.campfire, 1);
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);

		if(timer > 0)
			g.drawImage(fire.getImage(),
					(int) ((xScreen + xmap) - (width / 2)),
					(int) ((yScreen + ymap) - (height / 2)), null);

	}

	@Override
	public void interact(Player p, MapObject o) {
		GuiFire gui = new GuiFire(this, p);
		gui.setBlock(this);
		getWorld().displayGui(gui);
	}

	public boolean isLit(){
		return timer > 0;
	}

	@Override
	public void update() {
		super.update();

		if(timer > 0)
			timer --;

		if(timer > 0){
			fire.update();
		
			if(world.isNightTime())
				if(world.nightAlhpa > 0.8f)
					world.nightAlhpa = 0.8f;
		}
	}

	@Override
	protected void mine(Player p) {
		if(timer <= 0){
			super.mine(p);
		}else{
			System.out.println("Fire can not be recovered while it is burning !");
			resetHealth();
		}
	}

	public void readFromSave(DataTag data) {
		super.readFromSave(data);
		timer = data.readInt("timer");
	}
	
	public void writeToSave(DataTag data) {
		super.writeToSave(data);
		data.writeInt("timer", timer);
		
	}
	
	
	/*===============INVENTORY==============*/

	ItemStack[] inventory = new ItemStack[1];

	@Override
	public ItemStack[] getItems() {
		return inventory;
	}

	@Override
	public boolean hasStack(ItemStack stack) {
		if(inventory[0] != null && stack != null && stack.getItem().equals(inventory[0].getItem()))
			return true;

		return false;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[0];
	}

	@Override
	public int getMaxSlots() {
		return 1;
	}

	@Override
	public boolean setStackInNextAvailableSlot(ItemStack item) {

		if(getStackInSlot(0) == null)
			setStackInSlot(0, item);

		if(item != null && getStackInSlot(0) != null && item.getItem().equals(getStackInSlot(0).getItem())){
			setStackInSlot(0, item);
			return true;
		}

		return false;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		if(inventory[slot] == null)
			inventory[slot] = stack;
		else if (stack == null && inventory[slot] != null)
			inventory[slot] = null;
		else if(inventory[slot].getItem().equals(stack.getItem()))
			inventory[slot].stackSize += stack.stackSize;
		else
			System.out.println("something tried to replace the existing item" + inventory[slot].getItem().getUIN() +
					" by "+ stack.getItem().getUIN() );
	}

	@Override
	public void removeStack(int slot){
		inventory[0] = null;
	}

	@Override
	public boolean hasStackInSlot(int slot) {
		return inventory[slot] != null;
	}

	@Override
	public IInventory getInventory(){
		return this;
	}

	@Override
	public int getSlotForStack(ItemStack stack) {
		return 0;
	}
}
