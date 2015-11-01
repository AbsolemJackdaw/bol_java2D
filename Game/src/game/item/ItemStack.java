package game.item;

import engine.save.DataTag;

public class ItemStack
{
	public int stackSize;
	private Item item;
	
	private int maxDamage;
	private int damage; 
	
	public ItemStack(Item i, int size, int damage) {
		item = i;
		stackSize = size;
		setMaxDamage(damage);
	}
	
	public ItemStack(Item i) {
		this(i, 1, 0);
	}
	
	public ItemStack(Item i, int size) {
		this(i, size, 0);
	}
	
	public Item getItem(){
		return item;
	}
	
	public int getMaxDamage(){
		return maxDamage;
	}
	
	public ItemStack setMaxDamage(int i){
		maxDamage = damage = i;
		return this;
	}
	
	public ItemStack setDamage(int i){
		damage = i;
		return this;
	}
	public int getDamage(){
		return damage;
	}
	
	public void damageStack(int i){
		damage-= i;
	}
	
	public static ItemStack createFromSave(DataTag data){
		String s = data.readString("uniqueItemName");
		Item item = Items.getItemFromUIN(s);
		int size = data.readInt("stacksize");
		int dmg = data.readInt("dmg");
		item.readFromSave(data);
		ItemStack is = new ItemStack(item, size, item.getItemDamage()).setDamage(dmg);
		
		return is;
	}

	public void writeToSave(DataTag data){
		//item name aka unique identifier
		data.writeString("uniqueItemName", item.getUIN());
		data.writeInt("stacksize", stackSize);
		data.writeInt("dmg", getDamage());
		item.writeToSave(data);
	}
	
	public ItemStack copy(){
		
		ItemStack stack = new ItemStack(item, stackSize, item.getItemDamage());
		stack.setDamage(damage);
		
		return stack;
	}
}