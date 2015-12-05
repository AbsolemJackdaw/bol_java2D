package game.entity.block.breakable;

import static engine.keyhandlers.KeyHandler.INTERACT;
import static engine.keyhandlers.KeyHandler.getKeyName;
import engine.image.Images;
import engine.imaging.Animation;
import game.World;
import game.entity.living.player.Player;
import game.gui.GuiCrafting;
import game.item.ItemStack;
import game.item.tool.ItemTool.EnumTools;

import java.awt.image.BufferedImage;


public class BlockCraftingTable extends BlockBreakable {

	private boolean advanced;
	
	public BlockCraftingTable(World world, int health, boolean advanced, String name) {
		super(world, name, EnumTools.NOTHING);
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
		return null; //!advanced ?  new ItemStack(Items.craftTable, 1) : new ItemStack(Items.advancedCraftTable, 1) ;
	}
	
}
