package game.entity.block;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import engine.image.Images;
import engine.map.TileMap;
import game.World;
import game.item.ItemStack;
import game.item.ItemTool;
import game.item.Items;


public class BlockLog extends BlockBreakable{

	public BlockLog(TileMap tm, World world) {
		super(tm, world, Blocks.LOG, ItemTool.AXE);
		setHealth(5);
	}
	
	@Override
	public BufferedImage getEntityTexture() {
		return Images.loadImage("/blocks/log.png");
	}

	@Override
	public boolean hasAnimation() {
		return false;
	}
	
	@Override
	public ItemStack getDrop() {
		return new ItemStack(Items.log, 1);
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
	}

}
