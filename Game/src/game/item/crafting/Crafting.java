package game.item.crafting;

import game.entity.living.player.Player;
import game.item.Item;
import game.item.ItemStack;
import game.item.Items;
import game.util.Util;

public class Crafting {

	//basic crafting
	public static final int Sticks = 0;

	public static final int SwordHandle = 2;
	public static final int SoftHandle = 3;
	public static final int HardHandle = 4;

	public static final int Pickaxe = 5;
	public static final int Sword = 6;
	public static final int Axe = 7;

	public static final int Belt = 8;
	public static final int Pouch = 9;

	public static final int Whetstone = 10;
	public static final int Leather = 11;

	public static final int RefinedStone = 12; 
	public static final int OvenBase = 13; 
	
	public static final int PickaxeSmooth = 14;
	public static final int AxeSmooth = 15;
	public static final int SwordSmooth = 16;


	//ADVANCED CRAFTING
	public static final int Lantern = 0;

	public static ItemStack[] getRecipe(int i){

		switch(i){
		case Sticks:
			return new ItemStack[]{
					new ItemStack(Items.woodChip, 1),
					new ItemStack(Items.woodChip, 1)
			};

		case SwordHandle:
			return new ItemStack[]{
					new ItemStack(Items.leather, 1),
					new ItemStack(Items.stick, 1)
			};
		case SoftHandle:
			return new ItemStack[]{
					new ItemStack(Items.stick, 1),
					new ItemStack(Items.stick, 1)
			};
		case HardHandle:
			return new ItemStack[]{
					new ItemStack(Items.woodChip, 1),
					new ItemStack(Items.stick, 1)
			};

		case Sword:
			return new ItemStack[]{
					new ItemStack(Items.handle_sword, 1),
					new ItemStack(Items.stone, 1)
			};
		case Axe:
			return new ItemStack[]{
					new ItemStack(Items.handle_soft, 1),
					new ItemStack(Items.stone, 1)
			};
		case Pickaxe:
			return new ItemStack[]{
					new ItemStack(Items.handle_hard, 1),
					new ItemStack(Items.stone, 1)
			};

		case SwordSmooth:
			return new ItemStack[]{
					new ItemStack(Items.handle_sword, 1),
					new ItemStack(Items.refinedStone, 1)
			};
		case AxeSmooth:
			return new ItemStack[]{
					new ItemStack(Items.handle_soft, 1),
					new ItemStack(Items.refinedStone, 1)
			};
		case PickaxeSmooth:
			return new ItemStack[]{
					new ItemStack(Items.handle_hard, 1),
					new ItemStack(Items.refinedStone, 1)
			};
			
		case Belt:
			return new ItemStack[]{
					new ItemStack(Items.leather_fine, 1),
					new ItemStack(Items.leather_fine, 1)
			};
		case Pouch:
			return new ItemStack[]{
					new ItemStack(Items.leather_fine, 1),
					new ItemStack(Items.ingot, 1)
			};

		case Whetstone:
			return new ItemStack[]{
					new ItemStack(Items.leather, 1),
					new ItemStack(Items.stone, 1)
			};

		case Leather:
			return new ItemStack[]{
					new ItemStack(Items.leather, 1),
					new ItemStack(Items.leather, 1)
			};
			
		case RefinedStone:
			return new ItemStack[]{
					new ItemStack(Items.stone, 1),
					new ItemStack(Items.stone, 1)
			};
			
		case OvenBase:
			return new ItemStack[]{
					new ItemStack(Items.refinedStone, 1),
					new ItemStack(Items.refinedStone, 1)
			};
		}
		return null;
	}

	public static ItemStack result(int i){

		switch (i) {

		case Sticks:
			return new ItemStack(Items.stick, 2);

		case Pickaxe: 
			return new ItemStack(Items.rock_pickaxe, 1);
		case Sword :
			return new ItemStack(Items.rock_sword, 1);
		case Axe :
			return new ItemStack(Items.rock_axe, 1);

		case PickaxeSmooth: 
			return new ItemStack(Items.stone_pickaxe, 1);
		case SwordSmooth :
			return new ItemStack(Items.stone_sword, 1);
		case AxeSmooth :
			return new ItemStack(Items.stone_axe, 1);
			
		case SoftHandle:
			return new ItemStack(Items.handle_soft);
		case HardHandle:
			return new ItemStack(Items.handle_hard);
		case SwordHandle:
			return new ItemStack(Items.handle_sword);

		case Belt : 
			return new ItemStack(Items.belt);
		case Pouch:
			return new ItemStack(Items.pouch);

		case Whetstone:
			return new ItemStack(Items.whetstone);		
		case Leather:
			return new ItemStack(Items.leather_fine);			
		case RefinedStone:
			return new ItemStack(Items.refinedStone);
		case OvenBase:
			return new ItemStack(Items.ovenBase);
		}

		return null;
	}

	public static ItemStack resultAdvanced(int i){
		return null;
	}

	public static ItemStack[] getRecipeAdvanced(int i){
		return null;
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

		boolean callBack = true;

		for(int i = 0 ; i < 20; i++){

			ItemStack[] compare = getRecipe(i);

			if(compare == null || compare[0] == null || compare[1] == null)
				continue;

			if(compare.length == 2){
				//any match. does not allow for order based crafting
				
				if(compare[0].equals(stacks[0]) && compare[1].equals(stacks[1]) || 
						compare[0].equals(stacks[1]) && compare[1].equals(stacks[0])){

					System.out.println("Recipe " + i + " was a match");

					boolean flag = false;

					for(int content = 0; content < player.getInventory().getMaxSlots(); content++){

						if(player.getStackInSlot(content) == null ||
								player.getStackInSlot(content) != null && player.getStackInSlot(content).getItem().getUIN().equals(result(i).getItem().getUIN())){

							Util.decreaseStack(player, slots[0], 1);
							Util.decreaseStack(player, slots[1], 1);
							flag = true;
							callBack = false;
							break;
						}
					}

					if(flag)
						player.setStackInNextAvailableSlot(result(i));
				}
			}else{
				System.out.println("Recipe was longer then 2 !");
				return;
			}
		}

		//no recipe matched, try craftingCallBack
		if(callBack){
			System.out.println("no recipe match found... ressorting to callback");

			ItemStack copy = stacks[1].copy();
			Item item = stacks[1].getItem();
			
			item.craftingCallBack(stacks[0], stacks[1]);
			
			player.setStackInSlot(slots[1], null);
			player.setStackInSlot(slots[1], stacks[1]);
			
			if(!copy.equals(stacks[1])){

				Util.decreaseStack(player, slots[0], 1);
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