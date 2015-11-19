package game.entity;

import game.World;
import game.entity.living.EntityLiving;
import game.entity.living.player.Player;


public class EntityMovement {

	/**
	 * This is a helper class and methods can be used to implement some basic entity/player movement.
	 */
	public EntityMovement() {

	}

	/**
	 * Basic water movement for the player.
	 * Implies going slower in water, 
	 */
	public void doPlayerMovement(Player player){
		// movement
		if (player.left) {
			player.dx -= player.moveSpeed;
			if (player.dx < -player.maxSpeed)
				player.dx = -player.maxSpeed;
		} else if (player.right) {
			player.dx += player.moveSpeed;
			if (player.dx >player. maxSpeed)
				player.dx = player.maxSpeed;
		} else if (player.dx > 0) {
			player.dx -= player.stopSpeed;
			if (player.dx < 0)
				player.dx = 0;
		} else if (player.dx < 0) {
			player.dx += player.stopSpeed;
			if (player.dx > 0)
				player.dx = 0;
		}

		// jumping
		if (player.jumping && !player.falling) {
			player.dy = player.jumpStart;
			player.falling = true;
		}

		// falling
		if (player.falling) {
			player.dy += player.fallSpeed;

			if (player.dy > 0)
				player.jumping = false;
			if ((player.dy < 0) && !player.jumping)
				player.dy += player.stopJumpSpeed;

			if (player.dy > player.maxFallSpeed)
				player.dy = player.maxFallSpeed;
		}
	}

	public void doPlayerWaterMovement(Player player){
		if (player.left) {
			player.dx -= player.moveSpeed/2;
			if (player.dx < -player.maxSpeed/2)
				player.dx = -player.maxSpeed/2;
		} else if (player.right) {
			player.dx += player.moveSpeed/2;
			if (player.dx > player.maxSpeed/2)
				player.dx = player.maxSpeed/2;
		} else if (player.dx > 0) {
			player.dx -= player.stopSpeed;
			if (player.dx < 0)
				player.dx = 0;
		} else if (player.dx < 0) {
			player.dx += player.stopSpeed;
			if (player.dx > 0)
				player.dx = 0;
		}

		if (player.up) {
			player.dy -= player.moveSpeed/2;
			if (player.dy < -player.maxSpeed/2)
				player.dy = -player.maxSpeed/2;

		} else if (!player.up && player.falling) {
			player.dy += player.moveSpeed/2;
			if (player.dy > player.maxSpeed/2)
				player.dy = player.maxSpeed/2;
		} else if (player.dy > 0) {
			player.dy -= player.stopSpeed;
			if (player.dy < 0)
				player.dy = 0;
		} else if (player.dy < 0) {
			player.dy += player.stopSpeed;
			if (player.dy > 0)
				player.dy = 0;
		}
	}

	public void setPathToPlayer(EntityLiving el){

		World world = (World)el.getWorld();

		Player p = world.getPlayer();

		if(p.xScreen < el.xScreen)
			el.dx-=el.moveSpeed;
		else
			el.dx+=el.moveSpeed;

		if(p.yScreen < el.yScreen)
			el.dy-=el.fallSpeed;
		else
			el.dy+= el.fallSpeed;

	}
}
