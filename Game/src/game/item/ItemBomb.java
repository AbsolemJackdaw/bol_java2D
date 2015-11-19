package game.item;

import engine.map.TileMap;
import game.World;
import game.entity.Entity;
import game.entity.living.EntityBomb;
import game.entity.living.EntityLiving;
import game.entity.living.player.Player;
import game.util.Util;

public class ItemBomb extends Item {

	private int power;

	public ItemBomb(String uin, String displayName, int power) {
		super(uin, displayName);
		this.power = power;
	}

	@Override
	public void useItem(ItemStack item, TileMap map, World world, Player player, int key) {

		EntityLiving entity = Entity.createEntityFromUIN(Entity.BOMB, world);

		if(entity instanceof EntityBomb){
			EntityBomb bomb = (EntityBomb)entity;

			if(bomb != null){
				bomb.setPosition(player.getScreenXpos(), player.getScreenYpos());
				bomb.setLeft(!player.facingRight);
				bomb.setRight(player.facingRight);
				bomb.maxSpeed = power;
				bomb.setJumping(true);
				bomb.dy = bomb.jumpStart;
				bomb.setBlastPower(power);
				bomb.setBlastRadius(power == 2 ? 32 : power == 4 ? 48 : 80);
				world.listWithMapObjects.add(bomb);
			}

			Util.decreaseStack(player, key, 1);
		}
	}
}
