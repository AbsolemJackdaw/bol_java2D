package game.entity.block;

import game.World;
import game.content.Images;
import game.entity.Animation;
import game.entity.MapObject;
import game.entity.living.player.Player;
import game.gui.GuiCrafting;
import game.item.ItemStack;
import game.item.ItemTool;
import game.item.Items;

import java.awt.image.BufferedImage;

import base.tilemap.TileMap;

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
	public void interact(Player p, MapObject o) {
		getWorld().displayGui(new GuiCrafting(getWorld(), p, advanced));
	}
	
	@Override
	public ItemStack getDrop() {
		return !advanced ?  new ItemStack(Items.craftTable, 1) : new ItemStack(Items.advancedCraftTable, 1) ;
	}
	
}
