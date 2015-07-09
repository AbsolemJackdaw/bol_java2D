package game.entity;

import game.World;
import game.entity.living.EntityBat;
import game.entity.living.EntityLiving;
import game.entity.living.EntityPig;
import game.entity.living.environement.EntityDeathAnim;
import base.tilemap.TileMap;

public class Entity {

	public static final String PIG = "pig";
	public static final String BAT = "bat";
	public static final String DEATHANIM = "da";
	
	public static EntityLiving createEntityFromUIN(String s, TileMap tm, World world){
		
		switch (s) {
		case PIG:
			return new EntityPig(tm, world, PIG);
		case DEATHANIM :
			return new EntityDeathAnim(tm, world);
		case BAT :
			return new EntityBat(tm, world, BAT);
			
		}
		return null;
	}
}
