package game.item;

import engine.map.TileMap;
import engine.music.Music;
import game.World;
import game.entity.living.player.Player;
import game.gui.GuiPlayerInventory;
import game.util.Util;

import java.util.Random;

public class ItemFood extends Item{

	protected float healingfactor;

	public ItemFood(String uin, String displayName, float healing) {
		super(uin, displayName);
		healingfactor = healing;
	}

	@Override
	public boolean hasInventoryCallBack(Player player) {
		return (player.getWorld().guiDisplaying instanceof GuiPlayerInventory);
	}

	@Override
	public void inventoryCallBack(int slot, Player player) {

		if(player.getHealth() < player.getMaxHealth()){
			player.heal(healingfactor);
			Util.decreaseStack(player, slot, 1);
			Music.play("crunch_" + (new Random().nextInt(5)+1));
		}
		
	}
	
	@Override
	public void useItem(Item item, TileMap map, World world, Player player,int key) {
		this.inventoryCallBack(key, player);
	}

	public float getHealingFactor(){
		return healingfactor;
	}

}
