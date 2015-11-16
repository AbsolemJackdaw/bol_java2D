package game.entity.block.environement;

import engine.image.Images;
import engine.save.DataList;
import engine.save.DataTag;
import game.World;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BlockInfoPane extends BlockEnvironement {

	private List<String> text = new ArrayList<>();
	
	public BlockInfoPane(World world, String uin) {
		super(world, uin);
		
	}
	
	@Override
	public boolean hasAnimation(){
		return false;
	}
	
	@Override
	public BufferedImage getEntityTexture() {
		return Images.loadImage("/blocks/sign.png");
	}

	public void setText(List<String> text){
		this.text = text;
	}
	
	@Override
	public List<String> getBlockInfo(){
		return text;
	}
	
	@Override
	public void readFromSave(DataTag data) {
		super.readFromSave(data);
		
		DataList list = data.readList("list");
		for(int i = 0; i < list.data().size(); i++){
			DataTag tag = list.readArray(i);
			text.add(tag.readString("text"));
		}
		
	}
	
	@Override
	public void writeToSave(DataTag data) {
		super.writeToSave(data);
		
		DataList list = new DataList();
		
		for(String s : text){
			DataTag tag = new DataTag();
			tag.writeString("text", s);
			list.write(tag);
		}
		
		data.writeList("list", list);
	}
}
