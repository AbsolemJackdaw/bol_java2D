package game.entity.living.environement;

import engine.game.GameWorld;
import engine.game.entity.EntityLiving;
import engine.map.TileMap;
import game.entity.living.player.Player;

public class EntitySign extends EntityLiving {

	public EntitySign(TileMap tm, GameWorld world, String uin) {
		super(tm, world, uin);
	}

	//keep onEntityHit methods empty. 
	//prevents them from getting hurt
	@Override
	public void onEntityHit(float damage) {
	}

	@Override
	public void onEntityHit(Player p) {
	}

	@Override
	public boolean canPlayDeathAnimation() {
		return false;
	}
}
