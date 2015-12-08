package game.item;

import java.util.HashMap;

import engine.image.Images;
import game.entity.block.Blocks;
import game.item.block.ItemBlockOven;
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
	
	public static Item whetstone = new ItemWhetstone("whetstone","WhetStone").setTexture(Images.loadImage("/items/manufactured/whetstone.png"));
	
	public static Item ovenBase = new ItemOvenbase("ovenbase", "Oven Base").setTexture(Images.loadImage("/items/manufactured/oven_base.png"));

	public static Item handle_sword = new Item("handle_sword","Sword Handle").setTexture(Images.loadImage("/items/tools/handle_sword.png"));
	public static Item handle_soft = new Item("handle_soft","Modest Handle").setTexture(Images.loadImage("/items/tools/handle_soft.png"));
	public static Item handle_hard = new Item("handle_hard","Greater Handle").setTexture(Images.loadImage("/items/tools/handle_hard.png"));

	public static ItemFood meat_pig_raw = (ItemFood) new ItemFood("pig_raw", "Raw Pig Meat", 0.5F).setTexture(Images.loadImage("/items/food/meat_pig.png")).setCookable();
	public static ItemFood meat_pig = (ItemFood)new ItemFood("pig_cooked","Pig Meat", 1f).setTexture(Images.loadImage("/items/food/meat_pig_cooked.png"));
	public static ItemFood meat_fish_raw = (ItemFood) new ItemFood("fish_raw", "Raw Fish", 0.5f).setTexture(Images.loadImage("/items/food/meat_fish_raw.png")).setCookable();
	public static ItemFood meat_fish = (ItemFood) new ItemFood("fish_cooked", "Fish Filet", 1f).setTexture(Images.loadImage("/items/food/meat_fish_cooked.png"));

	public static ItemPouch pouch = (ItemPouch) new ItemPouch("pouch","Leather Pouch").setTexture(Images.loadImage("/items/manufactured/pouch.png"));

	public static ItemBlockOven oven = (ItemBlockOven) new ItemBlockOven(Blocks.OVEN, "Oven").setTexture(Images.loadImage("/blocks/oven.png"));

	public static ItemTool rock_pickaxe =         (ItemTool) new ItemTool("pickaxe", "Stone Pickaxe", EnumMaterial.ROCK).setBaseAttack(2).setEffectiveness(EnumTools.PICKAXE).setEffectiveDamage(2).setBaseDamage(75).setTexture(Images.loadImage("/items/tools/rock_pickaxe.png"));
	public static ItemTool stone_pickaxe= (ItemTool) new ItemTool("stone_pickaxe", "Smooth Pickaxe", EnumMaterial.STONE).setBaseAttack(2).setEffectiveness(EnumTools.PICKAXE).setEffectiveDamage(2).setBaseDamage(90).setTexture(Images.loadImage("/items/tools/smooth_pickaxe.png"));
	public static ItemTool iron_pickaxe =     (ItemTool) new ItemTool("iron_pickaxe", "Iron Pickaxe", EnumMaterial.IRON).setBaseAttack(2).setEffectiveness(EnumTools.PICKAXE).setEffectiveDamage(2).setBaseDamage(110).setTexture(Images.loadImage("/items/tools/iron_pickaxe.png"));

	public static ItemTool rock_sword   =         (ItemTool) new ItemSword("sword", "Stone Sword", EnumMaterial.ROCK).setBaseAttack(3).setEffectiveness(EnumTools.SWORD).setEffectiveDamage(2).setBaseDamage(120).setTexture(Images.loadImage("/items/tools/rock_sword.png"));
	public static ItemTool stone_sword  = (ItemTool) new ItemSword("stone_sword", "Smooth Sword", EnumMaterial.STONE).setBaseAttack(3).setEffectiveness(EnumTools.SWORD).setEffectiveDamage(1).setBaseDamage(150).setTexture(Images.loadImage("/items/tools/smooth_sword.png"));
	public static ItemTool iron_sword   =     (ItemTool) new ItemSword("iron_sword", "Iron Sword", EnumMaterial.IRON).setBaseAttack(3).setEffectiveness(EnumTools.SWORD).setEffectiveDamage(1).setBaseDamage(175).setTexture(Images.loadImage("/items/tools/iron_sword.png"));

	public static ItemTool rock_axe   =         (ItemTool) new ItemTool("axe", "Stone Axe", EnumMaterial.ROCK).setBaseAttack(2).setEffectiveness(EnumTools.AXE).setEffectiveDamage(2).setBaseDamage(75).setTexture(Images.loadImage("/items/tools/rock_axe.png"));
	public static ItemTool stone_axe  = (ItemTool) new ItemTool("stone_axe", "Smooth Axe", EnumMaterial.STONE).setBaseAttack(2).setEffectiveness(EnumTools.AXE).setEffectiveDamage(2).setBaseDamage(90).setTexture(Images.loadImage("/items/tools/smooth_axe.png"));
	public static ItemTool iron_axe   =     (ItemTool) new ItemTool("iron_axe", "Iron Axe", EnumMaterial.IRON).setBaseAttack(2).setEffectiveness(EnumTools.AXE).setEffectiveDamage(2).setBaseDamage(110).setTexture(Images.loadImage("/items/tools/iron_axe.png"));

	public static ItemLantern lantern = (ItemLantern) new ItemLantern("lantern", "Lantern").setTexture(Images.loadImage("/items/lantern.png"));		

	public static ItemBelt belt = (ItemBelt) new ItemBelt("belt","Belt").setInventorySlots(5).setTexture(Images.loadImage("/items/manufactured/belt.png"));
	public static ItemBelt belt_s = (ItemBelt) new ItemBelt("belt_s", "Belt").setInventorySlots(10).setTexture(Images.loadImage("/items/manufactured/belt_small.png"));
	public static ItemBelt belt_m = (ItemBelt) new ItemBelt("belt_m", "Belt").setInventorySlots(15).setTexture(Images.loadImage("/items/manufactured/belt_med.png"));
	public static ItemBelt belt_l = (ItemBelt) new ItemBelt("belt_l", "Belt").setInventorySlots(20).setTexture(Images.loadImage("/items/manufactured/belt_large.png"));

	public static ItemBomb bomb = (ItemBomb) new ItemBomb("bomb", "Bomb", 2).setTexture(Images.loadImage("/items/bomb.png"));
	public static ItemBomb bomb_better = (ItemBomb) new ItemBomb("bomb_b", "Better Bomb", 4).setTexture(Images.loadImage("/items/bomb.png"));
	public static ItemBomb bomb_ultra = (ItemBomb) new ItemBomb("bomb_u", "Ultra Bomb", 8).setTexture(Images.loadImage("/items/bomb.png"));

	public static void loadItems(){

		registerItem(stick);
		registerItem(woodChip);
		registerItem(iron);
		registerItem(stone);
		registerItem(rock_pickaxe);
		registerItem(rock_sword);
		registerItem(rock_axe);
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
		registerItem(handle_sword);
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

	}

	public static void registerItem(Item item){
		registeredItems.put(item.getUIN(), item);
	}
}

