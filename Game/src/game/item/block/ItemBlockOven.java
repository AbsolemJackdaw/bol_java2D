package game.item.block;

public class ItemBlockOven extends ItemBlock{

	public ItemBlockOven(String s, String displayName) {
		super(s, displayName);
	}
	
	@Override
	public boolean isStackable() {
		return false;
	}

}
