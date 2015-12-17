package game.util;

import game.item.ItemStack;

public class WeightedStack {
	
	private double weightedChance;
	private ItemStack stack;
	
	/**chance must be between 0.0 and 1.0*/
	public WeightedStack(ItemStack stack, double chance) {
		this.stack = stack;
		weightedChance = chance;
	}
	
	public double getWeight(){
		return weightedChance;
	}
	
	public ItemStack getStack(){
		return stack;
	}

}
