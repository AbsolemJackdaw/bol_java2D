package game.entity;

import game.entity.living.EntityLiving;
import game.entity.living.player.Player;
import game.util.Constants;


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

		Player p = el.getWorld().getPlayer();

		if(p.xScreen < el.xScreen){
			el.setLeft(true);
			el.facingRight = false;
		}
		else{
			el.setRight(true);
			el.facingRight = true;
		}

		if(p.yScreen < el.yScreen)
			el.setUp(true);
		else
			el.setDown(true);

//		System.out.println(el.right + " " + el.left + " " + el.up + " " + el.down);

	}

	private int moveTimerY = 0;
	private int moveTimerYCalculator = 0;
	private int moveTimerX = 0;
	private int moveTimerXCalculator = 0;

	/**randomly decides to change entity's movement from left to right, and from down to up*/
	public void swimOrFly(EntityLiving el){
		if(el.dy == 0){
			if(moveTimerYCalculator == 0){
				moveTimerYCalculator = moveTimerY;
			}

			if(moveTimerYCalculator - moveTimerY >= 20){
				el.up = !el.up;
				el.down = !el.down;
				moveTimerYCalculator = 0;
			}
		}

		if(el.dx == 0){
			if(moveTimerXCalculator == 0){
				moveTimerXCalculator = moveTimerX;
			}

			if(moveTimerXCalculator - moveTimerX >= 20){
				el.left = !el.left;
				el.right = el.facingRight = !el.right;
				moveTimerXCalculator = 0;
			}
		}

		if(moveTimerY == 0){
			moveTimerY = Constants.RANDOM.nextInt(300) + 300;

			if(Constants.RANDOM.nextInt(2) == 0){
				el.up = !el.up;
				el.down = !el.down;
				moveTimerYCalculator = 0;
			}
		}

		if(moveTimerX == 0){
			moveTimerX = Constants.RANDOM.nextInt(400) + 200;

			if(Constants.RANDOM.nextInt(2) == 0){
				el.left = !el.left;
				el.right = el.facingRight = !el.right;
				moveTimerXCalculator = 0;
			}
		}

		moveTimerY--;
		moveTimerX--;
	}

	public void getNextPositionFlying(EntityLiving el, int wait){
		System.out.println(el.dy + el.dx);
		if(el.up && Constants.RANDOM.nextInt(wait) == 0){
			el.dy -= el.moveSpeed;
			if(el.dy < -el.maxSpeed)
				el.dy = -el.maxSpeed;
		}else if (el.down && Constants.RANDOM.nextInt(wait) == 0){
			el.dy += el.moveSpeed;
			if(el.dy > el.maxSpeed)
				el.dy = el.maxSpeed;
		}
	}

	public void doBasicLeftRightMovement(EntityLiving el){
		// movement
		if (el.left) {
			el.dx -= el.moveSpeed;
			if (el.dx < -el.maxSpeed)
				el.dx = -el.maxSpeed;
		} else if (el.right) {
			el.dx += el.moveSpeed;
			if (el.dx >el. maxSpeed)
				el.dx = el.maxSpeed;
		} else if (el.dx > 0) {
			el.dx -= el.stopSpeed;
			if (el.dx < 0)
				el.dx = 0;
		} else if (el.dx < 0) {
			el.dx += el.stopSpeed;
			if (el.dx > 0)
				el.dx = 0;
		}
	}
}
