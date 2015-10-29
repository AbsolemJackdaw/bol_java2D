package game.item;

public class ItemArmor extends Item{

	public static final int HEAD = 0;
	public static final int BODY = 1;
	public static final int EXTRA = 2;
	
	private int bodyPart;
	
	public ItemArmor(String uin, String displayName, int bodyPart) {
		super(uin, displayName);
		this.bodyPart = bodyPart;
		
	}
	
	public int getIndex(){
		return bodyPart;
	}

}
