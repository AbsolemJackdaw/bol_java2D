package game.entity;

import engine.game.GameWorld;
import engine.map.TileMap;
import game.World;
import game.entity.living.EntityBat;
import game.entity.living.EntityBomb;
import game.entity.living.EntityPig;
import game.entity.living.EntityWorm;
import game.entity.living.environement.EntityDeathAnim;
import game.entity.living.environement.EntityExplosion;

public class Entity {

	public static final String PIG = "pig";
	public static final String BAT = "bat";
	public static final String DEATHANIM_MEAT = "meat_death";
	public static final String DEATHANIM_EXPLOSION = "boom_death";
	public static final String WORM = "worm";
	public static final String BOMB = "bomb";

	public static EntityLiving createEntityFromUIN(String s, TileMap tm, GameWorld world){

		switch (s) {
		case PIG:
			return new EntityPig(tm, (World)world, PIG);
		case DEATHANIM_MEAT :
			return new EntityDeathAnim(tm, (World)world, DEATHANIM_MEAT);
		case BAT :
			return new EntityBat(tm, (World)world, BAT);
		case WORM :
			return new EntityWorm(tm, (World)world, WORM);
		case BOMB:
			return new EntityBomb(tm, (World) world, BOMB);
		case DEATHANIM_EXPLOSION :
			return new EntityExplosion(tm, (World)world);
		}

		return null;
	}
}
