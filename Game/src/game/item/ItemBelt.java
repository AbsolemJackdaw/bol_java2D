package game.item;

public class ItemBelt extends ItemArmor{

	/**the amount of extra slots it adds to the player's inventory*/
	private int inventorySlots;

	public ItemBelt(String uin,String displayName) {
		super(uin, displayName, EXTRA);
		
		if(uin.contains("_s"))
			info.add("1/3 upgrades added");
		
		else if(uin.contains("_l"))
			info.add("3/3 upgrades added");
		
		else if(uin.contains("_m"))
			info.add("2/3 upgrades added");
		
		else
			info.add("0/3 upgrades added");
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
