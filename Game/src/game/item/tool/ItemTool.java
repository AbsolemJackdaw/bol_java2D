package game.item.tool;

import engine.map.TileMap;
import game.World;
import game.entity.living.player.Player;
import game.item.Item;
import game.item.ItemStack;
import game.item.Items;

import java.util.ArrayList;
import java.util.List;


public class ItemTool extends Item {

	public int baseAttack;
	private int effectiveness;
	public int effectiveDamage;

	public EnumTool type;
	
	public static final int NOTHING = 0;
	public static final int PICKAXE = 1;
	public static final int AXE = 2;
	public static final int SWORD = 3;
	
	public static enum EnumTool {
		ROCK,
		IRON
	}

	public ItemTool(String uin, String displayName, EnumTool type){
		super(uin, displayName);
		this.type = type;
		this.modifiers = 3;
	}

	public ItemTool setBaseAttack(int baseAttack) {
		this.baseAttack = baseAttack;

		return this;
	}

	public int getBaseAttack() {
		return baseAttack;
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
	public int getEffectiveDamage(ItemStack stack){
		
		return effectiveDamage + stack.getBonus(ToolModifier.EFF);
	}

	public ItemTool setEffectiveDamage(int i) {
		this.effectiveDamage = i;
		return this;
	}

	@Override
	public void useItem(ItemStack item, TileMap map, World world, Player player,int key) {

		ItemStack invCopy = item;
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
	public List<String> getInfo(ItemStack stack) {

		info = new ArrayList<String>();

		info.add("Dmg : " + getAttackDamage(stack));
		info.add("Eff : " + getEffectiveDamage(stack));

		return info;
	}

	@Override
	public boolean hasModifiers() {
		return true;
	}

	public int getAttackDamage(ItemStack stack){
		return getBaseAttack() + stack.getBonus(ToolModifier.DMG);
	}
	
	@Override
	public void craftingCallBack(ItemStack component, ItemStack base) {
		super.craftingCallBack(component, base);
		
		if(component.getItem().equals(Items.stone) && type == EnumTool.ROCK){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 10, ToolModifier.EFF, 1));
		}
		else if(component.getItem().equals(Items.iron) && type == EnumTool.IRON){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 50, ToolModifier.EFF, 3));
		}
	}
}
