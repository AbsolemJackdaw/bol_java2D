package game.item;

public class ItemBelt extends ItemArmor{

	/**the amount of extra slots it adds to the player's inventory*/
	private int inventorySlots;

	public ItemBelt(String uin) {
		super(uin, EXTRA);
	}
	
	/**the amount of extra slots it adds to the player's inventory*/
	public ItemBelt setInventorySlots(int nr){
		inventorySlots = nr;
		return this;
	}
	
	public int getInventorySlots(){
		return inventorySlots;
	}

}
