package game.entity.block.environement;

import engine.image.Images;
import engine.keyhandlers.KeyHandler;
import engine.save.DataList;
import engine.save.DataTag;
import game.World;
import game.entity.living.player.Player;
import game.item.ItemStack;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BlockDroppedItem extends BlockEnvironement {

	private ItemStack stack;

	public BlockDroppedItem(World world, String uin) {
		super(world, uin);
		width = height = entitySizeX = entitySizeY = 16;
	}

	public BlockDroppedItem(World world, String uin, ItemStack stack) {
		this(world, uin);
		setItemStack(stack);
	}

	public void setItemStack(ItemStack stack){
		this.stack = stack.copy();
	}

	@Override
	public List<String> getBlockInfo() {

		blockInfo = new ArrayList<String>();

		blockInfo.add(KeyHandler.getKeyName(KeyHandler.INTERACT) + " to pickup");
		if(stack != null)
			blockInfo.add(stack.getItem().getDisplayName());
		else
			blockInfo.add("nothing. This sack is empty !");

		return blockInfo;
	}

	@Override
	public void writeToSave(DataTag data) {
		super.writeToSave(data);

		if(stack != null){
			DataTag tag = new DataTag();
			stack.writeToSave(tag);

			DataList list = new DataList();
			list.write(tag);

			data.writeList("stack", list);
		}
	}

	@Override
	public void readFromSave(DataTag data) {
		super.readFromSave(data);
		if(data.hasTag("stack"))
			setItemStack(ItemStack.createFromSave(data.readList("stack").readArray(0)));
	}

	@Override
	public void interact(Player p) {

		if(stack != null)
			if(p.setStackInNextAvailableSlot(stack)){
				remove = true;
			}
	}
	
	@Override
	protected BufferedImage getEntityTexture() {
		return Images.loadImage("/blocks/itempouch_small.png");
	}
}
