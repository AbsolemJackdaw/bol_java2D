package game.item;

import game.content.Images;
import game.entity.block.Blocks;

import java.util.HashMap;

public class Items {

	private static HashMap<String, Item> registeredItems = new HashMap<String, Item>();

	public static Item getItemFromUIN(String uin){
		if(registeredItems.containsKey(uin))
			return registeredItems.get(uin);
		return null;
	}
	
	public static Item stick = new Item("stick", "Stick").setTexture(Images.loadImage("/items/stick.png"));
	public static Item iron = new Item("iron", "Chunk of Iron").setTexture(Images.loadImage("/items/iron.png")).setCookable();
	public static Item rock = new Item("stone", "Rock").setTexture(Images.loadImage("/items/stone.png"));
	public static Item ingot = new Item("ingot", "Iron Ingot").setTexture(Images.loadImage("/items/ingot.png"));
	public static Item meat_pig_raw = new Item("meat_pig_raw", "Raw Pig Meat").setTexture(Images.loadImage("/items/meat_pig.png")).setCookable();
	public static Item meat_pig = new Item("meat_pig_cooked","Pig Meat").setTexture(Images.loadImage("/items/meat_pig_cooked.png"));
	public static Item grease = new Item("grease", "Grease").setTexture(Images.loadImage("/items/grease.png"));
	public static Item leather = new Item("leather", "Leather").setTexture(Images.loadImage("/items/leather.png"));
	public static Item pouch = new ItemPouch("pouch","Leather Pouch").setTexture(Images.loadImage("/items/pouch.png"));
	
	public static ItemBlock log = (ItemBlock) new ItemBlock(Blocks.LOG, "Log").setTexture(Images.loadImage("/blocks/log.png")).setFuel().setFuelTimer(800);
	public static ItemBlock craftTable = (ItemBlock) new ItemBlock(Blocks.CRAFTINGTABLE, "Crafting Table").setTexture(Images.loadImage("/blocks/workbench.png"));
	public static ItemBlock campfire = (ItemBlock) new ItemBlock(Blocks.CAMPFIRE, "Campfire").setTexture(Images.loadImage("/blocks/fire_logs.png"));
	public static ItemBlock oven = (ItemBlock) new ItemBlock(Blocks.OVEN, "Oven").setTexture(Images.loadImage("/blocks/oven.png"));
	public static ItemBlock advancedCraftTable = (ItemBlock) new ItemBlock(Blocks.ADVANCEDCRAFT, "Advanced Crafting Table").setTexture(Images.loadImage("/blocks/advanced_workbench.png"));
	
	public static ItemTool pickaxe = (ItemTool) new ItemTool("pickaxe", "Stone Pickaxe").setAttackDamage(2).setEffectiveness(ItemTool.PICKAXE).setEffectiveDamage(5).setItemDamage(100).setTexture(Images.loadImage("/items/pickaxe.png"));
	public static ItemTool sword   = (ItemTool) new ItemTool("sword", "Stone Sword").setAttackDamage(5).setEffectiveness(ItemTool.SWORD).setEffectiveDamage(2).setItemDamage(100).setTexture(Images.loadImage("/items/sword.png"));
	public static ItemTool axe   = (ItemTool) new ItemTool("axe", "Stone Axe").setAttackDamage(2).setEffectiveness(ItemTool.AXE).setEffectiveDamage(5).setItemDamage(100).setTexture(Images.loadImage("/items/axe.png"));

	public static ItemLantern lantern = (ItemLantern) new ItemLantern("lantern", "Lantern").setTexture(Images.loadImage("/items/lantern.png"));		

	public static ItemBelt belt = (ItemBelt) new ItemBelt("belt","Belt").setInventorySlots(5).setTexture(Images.loadImage("/items/belt.png"));
	public static ItemBelt belt_s = (ItemBelt) new ItemBelt("belt_s", "Belt").setInventorySlots(10).setTexture(Images.loadImage("/items/belt_small.png"));
	public static ItemBelt belt_m = (ItemBelt) new ItemBelt("belt_m", "Belt").setInventorySlots(15).setTexture(Images.loadImage("/items/belt_med.png"));
	public static ItemBelt belt_l = (ItemBelt) new ItemBelt("belt_l", "Belt").setInventorySlots(20).setTexture(Images.loadImage("/items/belt_large.png"));

	public static void loadItems(){
		registerItem(stick);
		registerItem(craftTable);
		registerItem(log);
		registerItem(iron);
		registerItem(rock);
		registerItem(pickaxe);
		registerItem(campfire);
		registerItem(sword);
		registerItem(axe);
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
		registerItem(advancedCraftTable);

	}

	public static void registerItem(Item item){
		registeredItems.put(item.getUIN(), item);
	}
}

