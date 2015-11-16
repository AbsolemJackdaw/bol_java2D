package game.entity.living.environement;

import engine.game.GameWorld;
import game.entity.EntityLiving;
import game.entity.living.player.Player;

public class EntitySign extends EntityLiving {

	public EntitySign(GameWorld world, String uin) {
		super(world, uin);
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
