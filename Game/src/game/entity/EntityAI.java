package game.entity;

import game.entity.living.EntityLiving;
import game.util.Constants;


public class EntityAI {

	int entityTimer = 0;
	int entityTimer2 = 0;

	public void walkAroundRandomly(EntityLiving el){
		//start timer if x position is the same for a while
		if(el.dx == 0){
			el.setJumping(true);
		}

		if(el.jumping){
			entityTimer++;
		}

		if(entityTimer > 50){
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
		if( el.tileMap.getBlockID(el.currentRow,el.currentColumn) == 7){
			el.left = false; el.right = true;
		}

		if(el.tileMap.getBlockID(el.currentRow, el.currentColumn) == 6){
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

	/**
	 * AI to make entity walk on walls etc
	 */
	public void crawlAroundEverywhere(EntityLiving el){
	}

	/**makes the entity flutter around*/ //TODO needs improvement
	public void flutterAround(EntityLiving el){

		// turn around when arrow blocks are hit
		if( el.tileMap.getBlockID(el.currentRow,el.currentColumn) == 7){
			el.left = false; el.right = true;
		}

		if(el.tileMap.getBlockID(el.currentRow, el.currentColumn) == 6){
			el.left = true; el.right = false;
		}

		entityTimer++;

		if(entityTimer > 256) 
			entityTimer = 0;

		if(entityTimer % 6 == 0){
			if(entityTimer > 1)
				if(Constants.RANDOM.nextInt(entityTimer) > 10)
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

	public void panic(EntityLiving el) {
		//		if(el.left){
		//			el.setRight(true);
		//		}else
		//			el.setLeft(true);
		//		el.moveSpeed = el.moveSpeed*1.5;
		//		el.maxSpeed = el.maxSpeed*2;
	}

}
