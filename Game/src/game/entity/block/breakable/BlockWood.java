package game.entity.block.breakable;

import engine.game.MapObject;
import engine.image.Images;
import engine.save.DataTag;
import game.World;
import game.entity.block.Blocks;
import game.entity.living.player.Player;
import game.item.ItemStack;
import game.item.Items;
import game.item.tool.ItemTool;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;


public class BlockWood extends BlockBreakable{

	private boolean isEndBlock;

	public BlockWood(World world, boolean b) {
		this(world);
		setEndBlock(b);
	}

	public BlockWood(World world) {
		super(world, Blocks.VINE, ItemTool.AXE);
		setHealth(5);

		moveSpeed = 0; 
		stopSpeed = 0;
		fallSpeed = 0.0;
		maxFallSpeed = 0;
	}

	@Override
	public BufferedImage getEntityTexture() {
		if(isEndBlock)
			return Images.loadImage("/blocks/vine_end.png");
		else
			return Images.loadImage("/blocks/vine.png");
	}

	@Override
	public boolean hasAnimation() {
		return false;
	}

	@Override
	public ItemStack getDrop() {
		return new ItemStack(Items.woodChip, 1);
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
	}

	public void setEndBlock(boolean isEndBlock) {
		this.isEndBlock = isEndBlock;
		reloadTexture();
	}

	@Override
	protected void mine(Player p) {
		super.mine(p);
		informNeighbors();
	}

	public void informNeighbors(){
		if(!isEndBlock){

			Iterator<MapObject> it = world.listWithMapObjects.iterator();
			while(it.hasNext()){
				MapObject mo = it.next();
				if(mo instanceof BlockWood){
					if(mo.currentRow == currentRow){
						if(mo.currentColumn == currentColumn+1){
							mo.fallSpeed = 0.02f;
							mo.maxFallSpeed = 2f;
							((BlockWood) mo).informNeighbors();
						}
					}
				}
			}
		}
	}

	@Override
	public void writeToSave(DataTag data) {
		super.writeToSave(data);
		data.writeBoolean("endBlock", isEndBlock);
	}

	@Override
	public void readFromSave(DataTag data) {
		super.readFromSave(data);
		setEndBlock(data.readBoolean("endBlock"));
	}
}
