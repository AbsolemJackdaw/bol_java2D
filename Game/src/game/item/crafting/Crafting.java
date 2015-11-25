package game.item.crafting;

import game.entity.living.player.Player;
import game.item.ItemStack;
import game.item.Items;
import game.util.Util;

public class Crafting {

	//basic crafting
	public static final int Sticks = 0;
	public static final int CraftTable = 1;
	public static final int Oven = 2;
	public static final int CampFire = 3;
	public static final int Pickaxe = 4;
	public static final int Sword = 5;
	public static final int Axe = 6;
	public static final int Belt = 7;
	public static final int Pouch = 8;
	public static final int CraftAdvanced = 9;


	//ADVANCED CRAFTING
	public static final int Lantern = 0;

	public static ItemStack[] getRecipe(int i){
		ItemStack[] is = new ItemStack[2];

		switch(i){
		case Sticks:
			is[0] = new ItemStack(Items.woodChip, 1);
			is[1] = new ItemStack(Items.woodChip, 1);
			break;
		case Sword:
			is[0] = new ItemStack(Items.stick);
			is[1] = new ItemStack(Items.rock);
			break;
			
//		case Pickaxe:
//			is[0] = new ItemStack(Items.stick, 2);
//			is[1] = new ItemStack(Items.rock, 6);
//			break;
//		case Sword:
//			is[0] = new ItemStack(Items.stick, 2);
//			is[1] = new ItemStack(Items.woodChip, 1);
//			is[2] = new ItemStack(Items.rock, 8);
//			break;
//		case Axe:
//			is[0] = new ItemStack (Items.stick , 2);
//			is[1] = new ItemStack(Items.rock, 3);
//			break;
//		case Oven:
//			is[0] = new ItemStack (Items.rock , 20);
//			break;
//		case Belt:
//			is[0] = new ItemStack(Items.leather, 12);
//			break;
//		case Pouch:
//			is[0] = new ItemStack(Items.leather, 5);
//			is[1] = new ItemStack(Items.ingot, 1);
//			is[2] = new ItemStack(Items.stick, 10);
//			break;
//		case CraftAdvanced:
//			is[0] = new ItemStack(Items.ingot,10);
//			is[1] = new ItemStack(Items.woodChip, 20);
//			is[2] = new ItemStack(Items.leather, 10);
//			is[3] = new ItemStack(Items.rock, 25);
//			break;
		}
		return is;
	}

	public static ItemStack result(int i){

		ItemStack st = null;

		switch (i) {

		case Sticks:
			st = new ItemStack(Items.stick, 2);
			break;
		case CraftTable:
			st = new ItemStack(Items.craftTable, 1);
			break;
		case Pickaxe: 
			st = new ItemStack(Items.pickaxe, 1, Items.pickaxe.getItemDamage());
			break;
		case CampFire:
			st = new ItemStack(Items.campfire, 1);
			break;
		case Sword :
			st = new ItemStack(Items.sword, 1, Items.sword.getItemDamage());
			break;
		case Axe :
			st = new ItemStack(Items.axe, 1 ,Items.axe.getItemDamage());
			break;
		case Oven:
			st = new ItemStack(Items.oven, 1);
			break;
		case Belt : 
			st = new ItemStack(Items.belt,1);
			break;
		case Pouch:
			st = new ItemStack(Items.pouch,1);
			break;
		case CraftAdvanced:
			st = new ItemStack(Items.advancedCraftTable,1);
			break;
		}
		return st;
	}

	public static ItemStack resultAdvanced(int i){
		ItemStack st = null;
		switch (i) {
		case Lantern:
			st = new ItemStack(Items.lantern, 1);
			break;
		}
		return st;
	}

	public static ItemStack[] getRecipeAdvanced(int i){
		ItemStack[] is = new ItemStack[10];
		switch(i){
		case Lantern:
			is[0] = new ItemStack(Items.ingot, 10);
			break;
		}
		return is;
	}

	public static void craft(Player player, ItemStack[] stacks, int[] slots){

		if(stacks[0] == null || stacks[1] == null)
			return;
		
		if(stacks[0].equals(stacks[1]) && slots[0] == slots[1]){
			if(stacks[0].stackSize <= 1){
				
				System.out.println("Cannot combine one item with itself !");
				
				return;	
				//if both objects are the same, and come from the same slot,
				//their stacksize has to be more then one to be able to be combined !
			}
		}

		for(int i = 0 ; i < 10; i++){

			ItemStack[] compare = getRecipe(i);

			if(compare[0] == null || compare[1] == null)
				continue;
			
			if(compare.length == 2){
				//any match. does not allow for order based crafting
				if(compare[0].equals(stacks[0]) && compare[1].equals(stacks[1]) || 
						compare[1].equals(stacks[0]) && compare[0].equals(stacks[1])){

					System.out.println("Recipe " + i + " was a match");
					
					boolean flag = false;
					
					for(int content = 0; content < player.getInventory().getMaxSlots(); content++){

						if(player.getStackInSlot(content) == null ||
								player.getStackInSlot(content) != null && player.getStackInSlot(content).getItem().getUIN().equals(result(i).getItem().getUIN())){

							Util.decreaseStack(player, slots[0], 1);
							Util.decreaseStack(player, slots[1], 1);
							flag = true;
							break;
						}
					}
					
					if(flag)
						player.setStackInNextAvailableSlot(result(i));

					
				}else{
					System.out.println("Recipe " + i + " did not match");
				}
			}else{
				System.out.println("Recipe was longer then 2 !");
				return;
			}
		}
	}

	public static void craft(Player player, int recipe, boolean advanced){

		ItemStack[] input = null;
		ItemStack result = null;

		if(!advanced){
			input = getRecipe(recipe);
			result = result(recipe);
		}else{
			input = getRecipeAdvanced(recipe);
			result = resultAdvanced(recipe);
		}

		if(input == null)
			return;
		if(result == null)
			return;


		int index = 0;

		for(ItemStack st: input){
			if(st != null)
				index++;
		}

		boolean flag[] = new boolean[index];

		for(int it = 0; it < input.length; it++){
			for(int i = 0; i < player.getInventory().getItems().length; i++){
				if(player.getStackInSlot(i) != null){
					if(input[it] != null){
						if(player.getStackInSlot(i).getItem().equals(input[it].getItem())){
							if(player.getStackInSlot(i).stackSize >= input[it].stackSize){
								flag[it] = true;
							}
						}
					}
				}
			}
		}

		for(boolean b : flag)
			if(!b){
				System.out.println("Not all components are aquiered.");
				return;
			}

		//Remove items before crafting the item
		for(int a = 0; a < input.length; a++){
			if(input[a] != null){
				int i = player.getInventory().getSlotForStack(input[a]);
				if(i == -1)
				{
					System.out.println("components requiered cannot be reached !");
					return;
				}
				player.getItems()[i].stackSize -= input[a].stackSize;
				if(player.getItems()[i].stackSize == 0)
					player.getItems()[i] = null;
			}
		}

		player.setStackInNextAvailableSlot(result);

	}
}