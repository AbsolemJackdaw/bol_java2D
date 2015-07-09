package game.item;

public class ItemArmor extends Item{

	public static final int HEAD = 0;
	public static final int BODY = 1;
	public static final int EXTRA = 2;
	
	private int bodyPart;
	
	public ItemArmor(String uin, int bodyPart) {
		super(uin);
		this.bodyPart = bodyPart;
	}
	
	public int getIndex(){
		return bodyPart;
	}

}
