package game.item.armor;


public class ItemChest extends ItemArmor {

	public ItemChest(String uin, String displayName, String armorTexture) {
		super(uin, displayName, armorTexture, BODY);

		animationLengthArray = new int[]{
				/*body idle*/1,
				/*attack*/1,
				/*any other*/1,
				
		};
	}

	public ItemChest(String uin, String displayName) {
		this(uin, displayName, uin);
	}
}
