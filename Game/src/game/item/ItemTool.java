package game.item;

import java.util.List;

import engine.map.TileMap;
import game.World;
import game.entity.living.player.Player;


public class ItemTool extends Item {

	private int attackDamage;
	private int effectiveness;
	private int effectiveDamage;

	public static final int NOTHING = 0;
	public static final int PICKAXE = 1;
	public static final int AXE = 2;
	public static final int SWORD = 3;

	public ItemTool(String uin, String displayName){
		super(uin, displayName);
	}

	public ItemTool setAttackDamage(int i){
		attackDamage = i;

		return this;
	}
	/**Used to calculate damage done to entities*/
	public int getAttackDamage(){
		return attackDamage;
	}

	/**Returns the effective type of this tool (pickaxe, axe, sword or none)*/
	public int getEffectiveness(){
		return effectiveness;
	}

	public ItemTool setEffectiveness(int i){
		this.effectiveness = i;
		return this;
	}

	/**Used to calculate damage done to blocks*/
	public int getEffectiveDamage() {
		return effectiveDamage;
	}

	public ItemTool setEffectiveDamage(int i) {
		this.effectiveDamage = i;
		return this;
	}

	@Override
	public void useItem(Item item, TileMap map, World world, Player player,int key) {

		ItemStack invCopy = player.getInventory().getStackInSlot(key);
		ItemStack equippedWeapon = player.invArmor.getWeapon();

		ItemStack newInvItem = equippedWeapon.copy();
		ItemStack newWeapon = invCopy.copy();

		player.invArmor.setWeapon(newWeapon);
		player.getInventory().setStackInSlot(key, null);
		player.getInventory().setStackInSlot(key, newInvItem);

	}

	@Override
	public boolean isStackable() {
		return false;
	}

	@Override
	public List<String> getToolTip(ItemStack stack) {

		if(tooltipList.size()< 3)
			tooltipList.add(stack.getDamage()+"/"+stack.getMaxDamage()) ;
		else
			if(!tooltipList.get(2).equals(stack.getDamage()+"/"+stack.getMaxDamage()))
				tooltipList.set(2, stack.getDamage()+"/"+stack.getMaxDamage());

		if(tooltipList.size() < 2)
			tooltipList.add("Dmg : " + getAttackDamage());
		else
			tooltipList.set(1, "Dmg : " + getAttackDamage());

		return super.getToolTip(stack);
	}
}
