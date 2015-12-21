package game.entity;

import game.World;
import game.entity.living.EntityBomb;
import game.entity.living.EntityLiving;
import game.entity.living.EntityPig;
import game.entity.living.enemy.EntityBat;
import game.entity.living.enemy.EntityPyrius;
import game.entity.living.enemy.EntityWarfBat;
import game.entity.living.enemy.EntityWorm;
import game.entity.living.environement.EntityBlockBreak;
import game.entity.living.environement.EntityDeathAnim;
import game.entity.living.environement.EntityDeathParticle;
import game.entity.living.environement.EntityExplosion;
import game.entity.living.environement.EntitySparkle;
import game.entity.living.water.EntityFish;

public class Entity {

	public static final String PIG = "pig";
	public static final String SAMBAT = "sambat";
	public static final String WARFBAT = "warfbat";
	public static final String DEATHANIM_MEAT = "meat_death";
	public static final String DEATHANIM_EXPLOSION = "boom_death";
	public static final String BOMB = "bomb";
	public static final String FISH = "fish";
	public static final String DEATHPARTICLE = "death";
	public static final String DEATHBLOCK = "deathblock";
	public static final String SPARKLE = "sparkle";
	public static final String PYRIUS = "pyrius";
	public static final String WORM = "worm";


	public static EntityLiving createEntityFromUIN(String s, World world){

		switch (s) {
		case PIG:
			return new EntityPig(world, PIG);
		case DEATHANIM_MEAT :
			return new EntityDeathAnim(world, DEATHANIM_MEAT);
		case SAMBAT :
			return new EntityBat(world, SAMBAT);
		case WARFBAT :
			return new EntityWarfBat(world, WARFBAT);
		case BOMB:
			return new EntityBomb(world, BOMB);
		case DEATHANIM_EXPLOSION :
			return new EntityExplosion(world);
		case FISH:
			return new EntityFish(world, FISH);
		case DEATHPARTICLE:
			return new EntityDeathParticle(world, DEATHPARTICLE);
		case DEATHBLOCK:
			return new EntityBlockBreak(world, DEATHBLOCK);
		case SPARKLE:
			return new EntitySparkle(world, SPARKLE);
		case PYRIUS:
			return new EntityPyrius(world, PYRIUS);
		case WORM:
			return new EntityWorm(world, WORM);
		}

		return null;
	}
}
