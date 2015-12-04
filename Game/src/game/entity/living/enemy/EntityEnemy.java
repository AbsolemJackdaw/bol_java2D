package game.entity.living.enemy;

import game.World;
import game.entity.living.EntityLiving;
import game.entity.living.player.Player;

import java.awt.Graphics2D;


public class EntityEnemy extends EntityLiving {

	private int attackTimer;

	protected boolean isHit;

	protected int endAgressionChance;

	public EntityEnemy(World world, String uin) {
		super(world, uin);

		endAgressionChance = 200;
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
	}

	@Override
	public void onEntityHit(Player player){
		if(endAgressionChance > 0)
			isHit = true;

		super.onEntityHit(player);
	}

	@Override
	public void update() {
		super.update();

		if(isHit)
			attackTimer++;
		else
			attackTimer = 0;

		if(isAgressive()){

			if(maxSpeed != getDefMaxSpeed()*1.5d)
				maxSpeed = getDefMaxSpeed()*1.5d;

			AI.setPathToPlayer(this);

			if(attackTimer % 120 == 0){ //2seconds
				Player p = getWorld().getPlayer();
				if(getRectangle().intersects(p.getRectangle())){
					p.hurtEntity(getAttackDamage(), null);
				}
			}
		}
	}

	public boolean isAgressive(){
		return isHit;
	}

	public float getAttackDamage(){
		return 0f;
	}
}
