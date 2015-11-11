package game.entity.block.breakable;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import engine.game.MapObject;
import engine.image.Images;
import engine.map.TileMap;
import game.World;
import game.entity.block.Blocks;
import game.entity.living.player.Player;
import game.item.ItemStack;
import game.item.ItemTool;
import game.item.Items;


public class BlockWood extends BlockBreakable{

	private boolean isEndBlock;

	public BlockWood(TileMap tm, World world) {
		super(tm, world, Blocks.VINE, ItemTool.AXE);
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
		//update texture as it is set only once
		getAnimation().setFrames(new BufferedImage[]{getEntityTexture()});
	}

	@Override
	protected void mine(Player p) {
		super.mine(p);
		informNeighbors();
	}

	public void informNeighbors(){
		if(!isEndBlock){

			for(MapObject mo : world.listWithMapObjects){
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
}
