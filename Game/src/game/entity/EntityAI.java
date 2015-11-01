package game.entity;

import game.entity.living.EntityLiving;
import game.entity.living.player.Player;


public class EntityAI {

	int entityTimer = 0;

	public void walkAroundRandomly(EntityLiving el){
		//start timer if x position is the same for a while
		if(el.dx == 0){
			el.setJumping(true);
		}

		if(el.jumping){
			entityTimer++;
		}

		if(entityTimer > 250){
			if(el.left){
				el.left = false;
				el.right = true;
				el.facingRight = true;
			}
			else if (el.right){
				el.left = true;
				el.right = false;
				el.facingRight = false;
			}
			entityTimer = 0;
		}

		// turn around when arrow blocks are hit
		if( el.tileMap.getBlockID(el.currentRow,el.currentCollumn) == 7){
			el.left = false; el.right = true;
		}

		if(el.tileMap.getBlockID(el.currentRow, el.currentCollumn) == 6){
			el.left = true; el.right = false;
		}

		if (el.right)
			el.facingRight = true;
		if (el.left)
			el.facingRight = false;
		//

		// jumping
		if (el.jumping && !el.falling) {
			el.dy = el.jumpStart;
			el.falling = true;
		}

		// falling
		if (el.falling) {
			el.dy += el.fallSpeed;

			if (el.dy > 0)
				el.jumping = false;
			if ((el.dy < 0) && !el.jumping)
				el.dy += el.stopJumpSpeed;

			if (el.dy > el.maxFallSpeed)
				el.dy = el.maxFallSpeed;

		}
	}

	/**makes the entity flutter around*/ //TODO needs improvement
	public void flutterAround(EntityLiving el){
		entityTimer++;

		if(entityTimer > 256) 
			entityTimer = 0;

		if(entityTimer % 6 == 0){
			if(entityTimer > 1)
				if(el.rand.nextInt(entityTimer) > 10)
					el.dy--;
				else
					;
			else
				el.dy --;
		}


		if(entityTimer % 80 == 0)
			if(el.left){
				el.left = false;
				el.right = true;
				el.facingRight = true;
			}
			else if (el.right){
				el.left = true;
				el.right = false;
				el.facingRight = false;
			}

	}

	public void setPathToPlayer(EntityLiving el){

		Player p = el.getWorld().getPlayer();
		if(p.xScreen < el.xScreen)
			el.dx-=el.moveSpeed*1.5d;
		else
			el.dx+=el.moveSpeed*1.5d;
		
		if(p.yScreen < el.yScreen)
			el.dy-=el.fallSpeed*1.5d;
		else
			el.dy+= el.fallSpeed*1.5d;

	}

	public void panic(EntityLiving el) {
//		if(el.left){
//			el.setRight(true);
//		}else
//			el.setLeft(true);
//		el.moveSpeed = el.moveSpeed*1.5;
//		el.maxSpeed = el.maxSpeed*2;
	}
}
