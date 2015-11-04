package game.entity.living.enemy;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import engine.game.MapObject;
import engine.game.entity.EntityLiving;
import engine.map.TileMap;
import engine.music.Music;
import game.World;
import game.entity.EntityAI;
import game.entity.living.player.Player;
import game.item.ItemTool;


public class EntityEnemy extends EntityLiving {

	BufferedImage[] entityTexture;

	private boolean flinching;
	private int flinchTimer = 100;

	private int attackTimer;

	/**set when attacked*/
	private int followTimer;

	EntityAI ai = new EntityAI();

	public EntityEnemy(TileMap tm, World world, String uin) {
		super(tm, world, uin);

		moveSpeed = 0.2; // inital walking speed. you speed up as you walk
		maxSpeed = 0.5; // change to jump farther and walk faster
		stopSpeed = 0.4;
		fallSpeed = 0.085; // affects falling and jumping
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		width = 32;
		height = 32;
		entitySizeX = 20;
		entitySizeY = 32;
		right = true;
	}

	public EntityEnemy setTexture(BufferedImage[] texture){
		entityTexture = texture;
		return this;
	}

	@Override
	public void draw(Graphics2D g) {
		if(flinching)
			flinchTimer++;
		if(flinchTimer > 50){
			flinchTimer = 0;
			flinching = false;
		}
		if(flinchTimer % 5 == 0)
		{
			super.draw(g);
		}
	}

	@Override
	public void onEntityHit(Player player, MapObject mo){

		Player p = (Player)player;
		
		int bonus = 0;
		if(p.invArmor.getWeapon() != null && p.invArmor.getWeapon().getItem() instanceof ItemTool){
			ItemTool weapon = (ItemTool) p.invArmor.getStackInSlot(3).getItem();
			bonus = weapon.getAttackDamage();
			p.invArmor.getWeapon().damageStack(1);
			flinching = true;
		}

		health -= p.getAttackDamage() + bonus;

	}

	@Override
	public void update() {
		super.update();

		if(followPlayer())
			attackTimer++;

		if(attackTimer % 300 == 0){ //5 seconds
			Player p = this.getWorld().getPlayer();
			if(getRectangle().intersects(p.getRectangle())){
				p.onEntityHit(p, this);
			}
		}

		if(!getWorld().isNightTime()){
			if(rand.nextInt(100) == 0)
				if(!flinching){
					health--;
					Music.play(getEntityHitSound());
					flinching = true;
				}
		}

		if(followTimer > 0){
			ai.setPathToPlayer(this);
			followTimer--;
		}

		if(health <= 0){
			this.remove = true;
		}
	}

	public boolean isAgressive(){
		return false;
	}

	protected boolean followPlayer(){
		return followTimer > 0;
	}

	protected void setFollowing(int followTimer){
		this.followTimer = followTimer;
	}

	protected void setFlinching() {
		flinching = true;
	}
	
	public int getAttackDamage(){
		return 1;
	}
	
	public World getWorld(){
		return (World)this.world;
	}
}
