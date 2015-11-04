package game.entity.living;

import engine.game.MapObject;
import engine.map.TileMap;
import engine.save.DataTag;
import game.World;

public class EntityLiving extends MapObject{

	protected float health = 1;
	protected float maxHealth = 2;
	
	public EntityLiving(TileMap tm, World world, String uin) {
		super(tm, world, uin);

		health = maxHealth;
	}

	public void hurtEntity(float f){
		health -= f;
		if(health <=0)
			this.remove=true;
		if(health > maxHealth)
			health = maxHealth;
		
	
	}
	
	public EntityLiving initHealth(float health){
		this.health = health;
		maxHealth = health;
		return this;
	}

	@Override
	public void writeToSave(DataTag data) {
		super.writeToSave(data);

		data.writeFloat("health", health);
		data.writeFloat("maxHealth", maxHealth);

	}

	@Override
	public void readFromSave(DataTag data) {
		super.readFromSave(data);

		health = data.readFloat("health");
		maxHealth = data.readFloat("maxHealth");
	}

	public float getHealth(){
		return health;
	}
	
	public float getMaxHealth(){
		return maxHealth;
	}
	
	public boolean canPlayDeathAnimation(){
		return true;
	}
	
	public String getEntityHitSound(){
		return null;
	}
	
	public String getEntityIdleSound(){
		return null;
	}
}
