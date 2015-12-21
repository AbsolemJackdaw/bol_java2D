package game.item;

import java.util.HashMap;

import engine.image.Images;
import game.block.Blocks;
import game.item.armor.ItemBelt;
import game.item.armor.ItemChest;
import game.item.armor.ItemHelm;
import game.item.block.ItemBlockOven;
import game.item.tool.ItemAxe;
import game.item.tool.ItemPickaxe;
import game.item.tool.ItemSword;
import game.item.tool.ItemTool;
import game.item.tool.ItemTool.EnumMaterial;
import game.item.tool.ItemTool.EnumTools;

public class Items {

	private static HashMap<String, Item> registeredItems = new HashMap<String, Item>();

	public static Item getItemFromUIN(String uin){
		if(registeredItems.containsKey(uin))
			return registeredItems.get(uin);
		else
			System.out.println("[ERROR] The item " + uin + " wasn't recognized.");
		return null;
	}

	public static Item stick = new Item("stick", "Stick").setTexture(Images.loadImage("/items/resources/stick.png"));
	public static Item iron = new Item("iron", "Chunk of Iron").setTexture(Images.loadImage("/items/resources/iron.png")).setCookable();
	public static Item stone = new Item("stone", "Stone").setTexture(Images.loadImage("/items/resources/stone.png"));
	public static Item ingot = new Item("ingot", "Iron Ingot").setTexture(Images.loadImage("/items/resources/ingot.png"));
	public static Item grease = new Item("grease", "Grease").setTexture(Images.loadImage("/items/resources/grease.png"));
	public static Item leather = new Item("leather", "Skin").setTexture(Images.loadImage("/items/resources/skin.png"));
	public static Item leather_fine = new Item("leather_fine", "Leather").setTexture(Images.loadImage("/items/resources/leather.png"));
	public static Item leather_strap = new Item("strap","Leather Strap").setTexture(Images.loadImage("/items/manufactured/leather_strap.png"));
	public static Item woodChip = new Item("woodchip","Wood Chips").setTexture(Images.loadImage("/items/resources/wood_chip.png")).setFuel().setFuelTimer(800);
	public static Item refinedStone = new Item("refinedstone","Smooth Stone").setTexture(Images.loadImage("/items/resources/stone_refined.png"));
	public static Item gem_blue = new Item("bluegem", "Blue Gem").setTexture(Images.loadImage("/items/resources/gem_blue.png"));
	public static Item plate_iron = new Item("plate_iron", "Iron Plate").setTexture(Images.loadImage("/items/manufactured/plate_iron.png"));
	public static Item plate_wood = new Item("plate_wood", "Wooden Plate").setTexture(Images.loadImage("/items/manufactured/plate_wood.png"));
	public static Item plate_leather = new Item("plate_leather", "Leather Plate").setTexture(Images.loadImage("/items/manufactured/plate_leather.png"));
	public static Item gunpowder = new Item("gunpowder", "GunPowder").setTexture(Images.loadImage("/items/resources/gunpowder.png"));

	public static Item whetstone = new ItemWhetstone("whetstone","WhetStone").setTexture(Images.loadImage("/items/manufactured/whetstone.png"));

	public static Item ovenBase = new ItemOvenbase("ovenbase", "Oven Base").setTexture(Images.loadImage("/items/manufactured/oven_base.png"));

	public static Item handle_small = new Item("handle_small","Small Handle").setTexture(Images.loadImage("/items/tools/handle_sword.png"));
	public static Item handle_soft = new Item("handle_soft","Modest Handle").setTexture(Images.loadImage("/items/tools/handle_soft.png"));
	public static Item handle_hard = new Item("handle_hard","Greater Handle").setTexture(Images.loadImage("/items/tools/handle_hard.png"));

	public static ItemFood meat_pig_raw = (ItemFood) new ItemFood("pig_raw", "Raw Pig Meat", 0.5F).setTexture(Images.loadImage("/items/food/meat_pig.png")).setCookable();
	public static ItemFood meat_pig = (ItemFood)new ItemFood("pig_cooked","Pig Meat", 1f).setTexture(Images.loadImage("/items/food/meat_pig_cooked.png"));
	public static ItemFood meat_fish_raw = (ItemFood) new ItemFood("fish_raw", "Raw Fish", 0.5f).setTexture(Images.loadImage("/items/food/meat_fish_raw.png")).setCookable();
	public static ItemFood meat_fish = (ItemFood) new ItemFood("fish_cooked", "Fish Filet", 1f).setTexture(Images.loadImage("/items/food/meat_fish_cooked.png"));

	public static ItemPouch pouch = (ItemPouch) new ItemPouch("pouch","Leather Pouch").setTexture(Images.loadImage("/items/manufactured/pouch.png"));

	public static ItemBlockOven oven = (ItemBlockOven) new ItemBlockOven(Blocks.OVEN, "Oven").setTexture(Images.loadImage("/blocks/oven.png"));

	public static ItemTool wood_pickaxe =         (ItemTool) new ItemPickaxe("pickaxe", "Wooden Pickaxe", EnumMaterial.WOOD).setBaseAttack(2).setEffectiveness(EnumTools.PICKAXE).setEffectiveDamage(2).setStackDamage(16).setTexture(Images.loadImage("/items/tools/wood_pickaxe.png"));
	public static ItemTool stone_pickaxe= (ItemTool) new ItemPickaxe("stone_pickaxe", "Smooth Pickaxe", EnumMaterial.STONE).setBaseAttack(2).setEffectiveness(EnumTools.PICKAXE).setEffectiveDamage(2).setStackDamage(16).setTexture(Images.loadImage("/items/tools/smooth_pickaxe.png"));
	public static ItemTool iron_pickaxe =     (ItemTool) new ItemPickaxe("iron_pickaxe", "Iron Pickaxe", EnumMaterial.IRON).setBaseAttack(2).setEffectiveness(EnumTools.PICKAXE).setEffectiveDamage(2).setStackDamage(32).setTexture(Images.loadImage("/items/tools/iron_pickaxe.png"));

	public static ItemTool wood_sword   =         (ItemTool) new ItemSword("sword", "Wooden Sword", EnumMaterial.WOOD).setBaseAttack(3).setEffectiveness(EnumTools.SWORD).setEffectiveDamage(2).setStackDamage(32).setTexture(Images.loadImage("/items/tools/wood_sword.png"));
	public static ItemTool stone_sword  = (ItemTool) new ItemSword("stone_sword", "Smooth Sword", EnumMaterial.STONE).setBaseAttack(3).setEffectiveness(EnumTools.SWORD).setEffectiveDamage(2).setStackDamage(48).setTexture(Images.loadImage("/items/tools/smooth_sword.png"));
	public static ItemTool iron_sword   =     (ItemTool) new ItemSword("iron_sword", "Iron Sword", EnumMaterial.IRON).setBaseAttack(3).setEffectiveness(EnumTools.SWORD).setEffectiveDamage(2).setStackDamage(64).setTexture(Images.loadImage("/items/tools/iron_sword.png"));

	public static ItemTool wood_axe   =         (ItemTool) new ItemAxe("axe", "Wooden Axe", EnumMaterial.WOOD).setBaseAttack(2).setEffectiveness(EnumTools.AXE).setEffectiveDamage(2).setStackDamage(32).setTexture(Images.loadImage("/items/tools/wood_axe.png"));
	public static ItemTool stone_axe  = (ItemTool) new ItemAxe("stone_axe", "Smooth Axe", EnumMaterial.STONE).setBaseAttack(2).setEffectiveness(EnumTools.AXE).setEffectiveDamage(2).setStackDamage(32).setTexture(Images.loadImage("/items/tools/smooth_axe.png"));
	public static ItemTool iron_axe   =     (ItemTool) new ItemAxe("iron_axe", "Iron Axe", EnumMaterial.IRON).setBaseAttack(2).setEffectiveness(EnumTools.AXE).setEffectiveDamage(2).setStackDamage(48).setTexture(Images.loadImage("/items/tools/iron_axe.png"));

	public static ItemLantern lantern = (ItemLantern) new ItemLantern("lantern", "Lantern").setTexture(Images.loadImage("/items/lantern.png"));		

	public static ItemBelt belt = (ItemBelt) new ItemBelt("belt","Belt", "belt").setInventorySlots(5).setTexture(Images.loadImage("/items/manufactured/belt.png"));
	public static ItemBelt belt_s = (ItemBelt) new ItemBelt("belt_s", "Belt", "belt").setInventorySlots(10).setTexture(Images.loadImage("/items/manufactured/belt_small.png"));
	public static ItemBelt belt_m = (ItemBelt) new ItemBelt("belt_m", "Belt", "belt").setInventorySlots(15).setTexture(Images.loadImage("/items/manufactured/belt_med.png"));
	public static ItemBelt belt_l = (ItemBelt) new ItemBelt("belt_l", "Belt", "belt").setInventorySlots(20).setTexture(Images.loadImage("/items/manufactured/belt_large.png"));

	public static ItemHelm helm_wood = (ItemHelm) new ItemHelm("helm_wood","Wooden Helm").setDamageReduction(0.1f).setStackDamage(25).setTexture(Images.loadImage("/items/manufactured/armor/helm_wood.png"));
	public static ItemHelm helm_iron = (ItemHelm) new ItemHelm("helm_iron","Iron Helm").setDamageReduction(0.2f).setStackDamage(45).setTexture(Images.loadImage("/items/manufactured/armor/helm_iron.png"));
	public static ItemHelm stache = (ItemHelm) new ItemHelm("stache","WarfStache").setDamageReduction(0.12f).setStackDamage(75).setTexture(Images.loadImage("/items/manufactured/armor/stache.png"));
	public static ItemHelm sam_eye = (ItemHelm) new ItemHelm("septiceye","Sceptic Eye").setDamageReduction(0.12f).setStackDamage(75).setTexture(Images.loadImage("/items/manufactured/armor/septiceye.png"));

	public static ItemChest chest_wood = (ItemChest) new ItemChest("armor_wood", "Wooden ChestPlate").setDamageReduction(0.2f).setStackDamage(30).setTexture(Images.loadImage("/items/manufactured/armor/chest_wood.png"));
	public static ItemChest chest_iron = (ItemChest) new ItemChest("armor_iron", "Iron ChestPlate").setDamageReduction(0.5f).setStackDamage(50).setTexture(Images.loadImage("/items/manufactured/armor/chest_iron.png"));

	public static ItemBomb bomb = (ItemBomb) new ItemBomb("bomb", "Bomb", 2).setTexture(Images.loadImage("/items/bomb.png"));
	public static ItemBomb bomb_better = (ItemBomb) new ItemBomb("bomb_b", "Better Bomb", 4).setTexture(Images.loadImage("/items/bomb.png"));
	public static ItemBomb bomb_ultra = (ItemBomb) new ItemBomb("bomb_u", "Ultra Bomb", 8).setTexture(Images.loadImage("/items/bomb.png"));

	public static ItemGemHammer gem_hammer = (ItemGemHammer) new ItemGemHammer("gemhammer", "Gem Hammer").setTexture(Images.loadImage("/items/manufactured/gem_hammer.png"));

	public static void loadItems(){

		registerItem(stick);
		registerItem(woodChip);
		registerItem(iron);
		registerItem(stone);
		registerItem(wood_pickaxe);
		registerItem(wood_sword);
		registerItem(wood_axe);
		registerItem(oven);
		registerItem(ingot);
		registerItem(meat_pig);
		registerItem(meat_pig_raw);
		registerItem(lantern);
		registerItem(grease);
		registerItem(belt);
		registerItem(belt_s);
		registerItem(belt_m);
		registerItem(belt_l);
		registerItem(leather);
		registerItem(pouch);
		registerItem(bomb);
		registerItem(bomb_better);
		registerItem(bomb_ultra);
		registerItem(meat_fish_raw);
		registerItem(meat_fish);
		registerItem(whetstone);
		registerItem(leather_fine);
		registerItem(handle_small);
		registerItem(handle_soft);
		registerItem(handle_hard);
		registerItem(refinedStone);
		registerItem(ovenBase);
		registerItem(stone_sword);
		registerItem(stone_axe);
		registerItem(stone_pickaxe);
		registerItem(iron_sword);
		registerItem(iron_axe);
		registerItem(iron_pickaxe);
		registerItem(leather_strap);
		registerItem(gem_blue);
		registerItem(gem_hammer);
		registerItem(plate_iron);
		registerItem(plate_leather);
		registerItem(plate_wood);
		registerItem(helm_iron);
		registerItem(helm_wood);
		registerItem(stache);
		registerItem(sam_eye);
		registerItem(chest_wood);
		registerItem(chest_iron);
		registerItem(gunpowder);

	}

	public static void registerItem(Item item){
		registeredItems.put(item.getUIN(), item);
	}
}

