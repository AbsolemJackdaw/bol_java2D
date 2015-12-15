package game.item.armor;


public class ItemHelm extends ItemArmor{
	
	public ItemHelm(String uin, String displayName, String armorTexture) {
		super(uin, displayName, armorTexture, HEAD);
		
		animationLengthArray = new int[]{
				/*body idle*/1,
				/*body attack*/1,
				/*falling/jump*/1,
				/*running*/1,
			};
	}
	
	public ItemHelm(String uin, String displayName) {
		this(uin, displayName, uin);
	}
}
