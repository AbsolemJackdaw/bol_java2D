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
	private EnumTools effectiveness;
	public int effectiveDamage;

	private EnumMaterial material;

	public static enum EnumMaterial{
		ROCK(0),
		STONE(1),
		IRON(2);

		private int id;
		EnumMaterial(int id){
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

	public static enum EnumTools {
		NOTHING,
		PICKAXE,
		AXE,
		SWORD
	}

	public ItemTool(String uin, String displayName, EnumMaterial material){
		super(uin, displayName);
		this.material = material;
	}

	public ItemTool setBaseAttack(int baseAttack) {
		this.baseAttack = baseAttack;

		return this;
	}

	public int getBaseAttack() {
		return baseAttack;
	}

	/**Returns the effective type of this tool (pickaxe, axe, sword or none)*/
	public EnumTools getEffectiveness(){
		return effectiveness;
	}

	public ItemTool setEffectiveness(EnumTools tool){
		this.effectiveness = tool;
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

	@Override
	public int getModifiers() {
		return getMaterial() == EnumMaterial.ROCK ? 1 :
			getMaterial() == EnumMaterial.STONE ?  3 :
				getMaterial() == EnumMaterial.IRON ? 5 : 2
						;
	}

	public int getAttackDamage(ItemStack stack){
		return getBaseAttack() + stack.getBonus(ToolModifier.DMG);
	}

	@Override
	public void craftingCallBack(ItemStack component, ItemStack base) {
		super.craftingCallBack(component, base);

		if(component.getItem().equals(Items.stone) && material == EnumMaterial.ROCK){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 20, ToolModifier.EFF, 3));
			base.damageStack(-20);
		}
		else if(component.getItem().equals(Items.refinedStone) && material == EnumMaterial.STONE){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 15, ToolModifier.EFF, 3));
			base.damageStack(-15);
		}
		else if(component.getItem().equals(Items.iron) && material == EnumMaterial.IRON){
			base.addModifier(new ToolModifier(ToolModifier.DUR, 30, ToolModifier.EFF, 3));
			base.damageStack(-30);
		}
	}

	public EnumMaterial getMaterial() {
		return material;
	}
}
