package game.item;

import java.util.HashMap;

import engine.image.Images;
import game.entity.block.Blocks;
import game.item.block.ItemBlockOven;
import game.item.tool.ItemSword;
import game.item.tool.ItemTool;
import game.item.tool.ItemTool.EnumTool;

public class Items {

	private static HashMap<String, Item> registeredItems = new HashMap<String, Item>();

	public static Item getItemFromUIN(String uin){
		if(registeredItems.containsKey(uin))
			return registeredItems.get(uin);
		else
			System.out.println("the item " + uin + " wasn't recognized.");
		return null;
	}

	public static Item stick = new Item("stick", "Stick").setTexture(Images.loadImage("/items/resources/stick.png"));
	public static Item iron = new Item("iron", "Chunk of Iron").setTexture(Images.loadImage("/items/resources/iron.png")).setCookable();
	public static Item stone = new Item("stone", "Stone").setTexture(Images.loadImage("/items/resources/stone.png"));
	public static Item ingot = new Item("ingot", "Iron Ingot").setTexture(Images.loadImage("/items/resources/ingot.png"));
	public static Item grease = new Item("grease", "Grease").setTexture(Images.loadImage("/items/resources/grease.png"));
	public static Item leather = new Item("leather", "Skin").setTexture(Images.loadImage("/items/resources/leather.png"));
	public static Item leather_fine = new Item("leather_fine", "Leather").setTexture(Images.loadImage("/items/resources/leather.png"));
	public static Item woodChip = new Item("woodchip","Wood Chips").setTexture(Images.loadImage("/items/resources/wood_chip.png")).setFuel().setFuelTimer(800);
	public static Item refinedStone = new Item("refinedstone","Smooth Stone").setTexture(Images.loadImage("/items/resources/stone_refined.png"));
	public static Item whetstone = new Item("whetstone","WhetStone").setTexture(Images.loadImage("/items/manufactured/whetstone.png"));
	
	public static Item ovenBase = new ItemOvenbase("ovenbase", "Oven Base").setTexture(Images.loadImage("/items/manufactured/oven_base.png"));
	
	public static Item handle_sword = new Item("handle_sword","Sword Handle").setTexture(Images.loadImage("/items/tools/handle_sword.png"));
	public static Item handle_soft = new Item("handle_soft","Modest Handle").setTexture(Images.loadImage("/items/tools/handle_soft.png"));
	public static Item handle_hard = new Item("handle_hard","Greater Handle").setTexture(Images.loadImage("/items/tools/handle_hard.png"));

	public static ItemFood meat_pig_raw = (ItemFood) new ItemFood("pig_raw", "Raw Pig Meat", 0.5F).setTexture(Images.loadImage("/items/food/meat_pig.png")).setCookable();
	public static ItemFood meat_pig = (ItemFood)new ItemFood("pig_cooked","Pig Meat", 1f).setTexture(Images.loadImage("/items/food/meat_pig_cooked.png"));
	public static ItemFood meat_fish_raw = (ItemFood) new ItemFood("fish_raw", "Raw Fish", 0.75f).setTexture(Images.loadImage("/items/food/meat_fish_raw.png")).setCookable();
	public static ItemFood meat_fish = (ItemFood) new ItemFood("fish_cooked", "Fish Filet", 1.5f).setTexture(Images.loadImage("/items/food/meat_fish_cooked.png"));
	
	public static ItemPouch pouch = (ItemPouch) new ItemPouch("pouch","Leather Pouch").setTexture(Images.loadImage("/items/manufactured/pouch.png"));

//	public static ItemBlock craftTable = (ItemBlock) new ItemBlock(Blocks.CRAFTINGTABLE, "Work Desk").setTexture(Images.loadImage("/blocks/workbench.png"));
//	public static ItemBlock campfire = (ItemBlock) new ItemBlock(Blocks.CAMPFIRE, "Campfire").setTexture(Images.loadImage("/blocks/fire_logs.png"));
//	public static ItemBlock advancedCraftTable = (ItemBlock) new ItemBlock(Blocks.ADVANCEDCRAFT, "Advanced Work Desk").setTexture(Images.loadImage("/blocks/advanced_workbench.png"));

	public static ItemBlockOven oven = (ItemBlockOven) new ItemBlockOven(Blocks.OVEN, "Oven").setTexture(Images.loadImage("/blocks/oven.png"));

	public static ItemTool rock_pickaxe = (ItemTool) new ItemTool("pickaxe", "Stone Pickaxe", EnumTool.ROCK).setBaseAttack(1).setEffectiveness(ItemTool.PICKAXE).setEffectiveDamage(5).setBaseDamage(75).setTexture(Images.loadImage("/items/tools/pickaxe.png"));
	public static ItemTool rock_sword   = (ItemTool) new ItemSword("sword", "Stone Sword", EnumTool.ROCK).setBaseAttack(3).setEffectiveness(ItemTool.SWORD).setEffectiveDamage(2).setBaseDamage(120).setTexture(Images.loadImage("/items/tools/sword.png"));
	public static ItemTool rock_axe   = (ItemTool) new ItemTool("axe", "Stone Axe", EnumTool.ROCK).setBaseAttack(2).setEffectiveness(ItemTool.AXE).setEffectiveDamage(2).setBaseDamage(75).setTexture(Images.loadImage("/items/tools/axe.png"));

	public static ItemTool stone_pickaxe = (ItemTool) new ItemTool("stone_pickaxe", "Smooth Pickaxe", EnumTool.STONE).setBaseAttack(2).setEffectiveness(ItemTool.PICKAXE).setEffectiveDamage(7).setBaseDamage(100).setTexture(Images.loadImage("/items/tools/pickaxe.png"));
	public static ItemTool stone_sword   = (ItemTool) new ItemSword("stone_sword", "Smooth Sword", EnumTool.STONE).setBaseAttack(5).setEffectiveness(ItemTool.SWORD).setEffectiveDamage(3).setBaseDamage(150).setTexture(Images.loadImage("/items/tools/sword.png"));
	public static ItemTool stone_axe   = (ItemTool) new ItemTool("stone_axe", "Smooth Axe", EnumTool.STONE).setBaseAttack(3).setEffectiveness(ItemTool.AXE).setEffectiveDamage(4).setBaseDamage(100).setTexture(Images.loadImage("/items/tools/axe.png"));

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
//			registerItem(craftTable);
			registerItem(woodChip);
			registerItem(iron);
			registerItem(stone);
			registerItem(rock_pickaxe);
//			registerItem(campfire);
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
//			registerItem(advancedCraftTable);
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

	}

	public static void registerItem(Item item){
		registeredItems.put(item.getUIN(), item);
	}
}

