package game.entity.block.breakable;

import static engine.keyhandlers.KeyHandler.INTERACT;
import static engine.keyhandlers.KeyHandler.getKeyName;

import java.awt.image.BufferedImage;

import engine.image.Images;
import engine.imaging.Animation;
import engine.map.TileMap;
import game.World;
import game.entity.living.player.Player;
import game.gui.GuiCrafting;
import game.item.ItemStack;
import game.item.ItemTool;
import game.item.Items;


public class BlockCraftingTable extends BlockBreakable {

	private boolean advanced;
	
	public BlockCraftingTable(TileMap tm, World world, int health, boolean advanced, String name) {
		super(tm, world, name, ItemTool.NOTHING);
		setHealth(health);
		this.advanced = advanced;
		
		if(advanced){
			animation = new Animation();
			BufferedImage[] bi = new BufferedImage[]{Images.loadImage("/blocks/advanced_workbench.png")};
			animation.setFrames(bi);
			animation.setDelay(Animation.NONE);
		}
			
		blockInfo.add(getKeyName(INTERACT) + " to interact");
	}
	
	@Override
	public BufferedImage getEntityTexture() {
		return Images.loadImage("/blocks/workbench.png") ;
	}
	
	@Override
	public boolean hasAnimation() {
		return false;
	}
	
	@Override
	public boolean isStackable() {
		return false;
	}
	
	@Override
	public void interact(Player p) {
		getWorld().displayGui(new GuiCrafting(getWorld(), (Player)p, advanced));
	}
	
	@Override
	public ItemStack getDrop() {
		return !advanced ?  new ItemStack(Items.craftTable, 1) : new ItemStack(Items.advancedCraftTable, 1) ;
	}
	
}
