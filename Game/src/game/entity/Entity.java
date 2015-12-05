package game.entity;

import engine.game.GameWorld;
import game.World;
import game.entity.living.EntityBomb;
import game.entity.living.EntityLiving;
import game.entity.living.EntityPig;
import game.entity.living.EntityWorm;
import game.entity.living.enemy.EntityBat;
import game.entity.living.enemy.EntityWarfBat;
import game.entity.living.environement.EntityDeathAnim;
import game.entity.living.environement.EntityExplosion;
import game.entity.living.water.EntityFish;

public class Entity {

	public static final String PIG = "pig";
	public static final String SAMBAT = "sambat";
	public static final String WARFBAT = "warfbat";
	public static final String DEATHANIM_MEAT = "meat_death";
	public static final String DEATHANIM_EXPLOSION = "boom_death";
	public static final String WORM = "worm";
	public static final String BOMB = "bomb";
	public static final String FISH = "fish";

	public static EntityLiving createEntityFromUIN(String s, GameWorld world){

		switch (s) {
		case PIG:
			return new EntityPig((World)world, PIG);
		case DEATHANIM_MEAT :
			return new EntityDeathAnim((World)world, DEATHANIM_MEAT);
		case SAMBAT :
			return new EntityBat((World)world, SAMBAT);
		case WARFBAT :
			return new EntityWarfBat((World)world, WARFBAT);
		case WORM :
			return new EntityWorm((World)world, WORM);
		case BOMB:
			return new EntityBomb((World) world, BOMB);
		case DEATHANIM_EXPLOSION :
			return new EntityExplosion((World)world);
		case FISH:
			return new EntityFish((World)world, FISH);
		}

		return null;
	}
}
