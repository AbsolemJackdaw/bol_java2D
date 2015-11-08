package game.entity.living;

import engine.image.Images;
import engine.map.TileMap;
import engine.music.Music;
import game.World;
import game.entity.EntityAI;
import game.entity.living.enemy.EntityEnemy;
import game.entity.living.player.Player;
import game.item.ItemStack;
import game.item.ItemTool;
import game.item.Items;

import java.awt.Graphics2D;


public class EntityBat extends EntityEnemy {

	public EntityBat(TileMap tm, World world, String uin) {
		super(tm, world, uin);

		initHealth(12f);

		getAnimation().setFrames(Images.loadMultiImage("/entity/bat.png", 32, 0, 4));
		getAnimation().setDelay(50);

		entitySizeX = 16+8;
		entitySizeY = 16+8;

		width = 32;
		height = 32;


		moveSpeed = defMoveSpeed = 0.05 + rand.nextDouble();  // inital walking speed. you speed up as you walk
		maxSpeed = defMaxSpeed = 0.5 + rand.nextDouble(); // change to jump farther and walk faster
		stopSpeed = 0.1;
		fallSpeed = 0.15; // affects falling and jumping
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		boolean b = rand.nextBoolean();
		facingRight = !b;
		right = b;
		left = !b;
		
	}

	private EntityAI ai = new EntityAI();

	private double defMaxSpeed;
	private double defMoveSpeed;

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
	}

	@Override
	public boolean hasAnimation() {
		return true;
	}

	@Override
	public void update() {
		super.update();

		if(!followPlayer())
			ai.flutterAround(this);

		calmDown();
	}

	@Override
	public void onEntityHit(Player player) {

		Player p = (Player)player;

		setFollowing(rand.nextInt(600)+1200);

		setFlinching();

		int dmg = p.getAttackDamage();

		int wepDmg = 0;
		ItemStack wep = p.invArmor.getWeapon();

		if(wep != null && wep.getItem() instanceof ItemTool){
			ItemTool tool = (ItemTool)wep.getItem();
			if(ItemTool.SWORD == tool.getEffectiveness()){
				wepDmg = ((ItemTool)wep.getItem()).getEffectiveDamage();
			}
			wep.damageStack(1);
		}

		health -= wepDmg + dmg;

		Music.play("hitpig_" + (rand.nextInt(5)+1));

		ai.panic(this);

		if(health < 0)
			kill(p);
	}

	private ItemStack[] drops = new ItemStack[3];

	public ItemStack[] getDrops() {
		drops[0] = new ItemStack(Items.meat_pig_raw, rand.nextInt(2)+1);
		drops[1] = new ItemStack(Items.grease, rand.nextInt(2)+1);
		drops[2] = new ItemStack(Items.leather, rand.nextInt(3)+1);
		return drops;

	}

	public void kill(Player p)
	{
		if(p!= null)
			if(p.setStackInNextAvailableSlot(getDrops()[rand.nextInt(drops.length)])){
				this.remove = true;
			}else{
				health = maxHealth;
			}
		else
			super.kill(p);
	}

	/**gets the entity's speed back to normal after it panis (/gets hit by the player)*/
	private void calmDown(){

		if(moveSpeed > defMoveSpeed)
			moveSpeed-=0.1d;
		else if(moveSpeed < defMoveSpeed)
			moveSpeed +=0.1d;

		if(maxSpeed > defMaxSpeed)
			maxSpeed-=0.01d;
		else if(maxSpeed < defMaxSpeed)
			maxSpeed +=0.01d;

	}

	@Override
	public String getEntityHitSound() {
		return "hitpig_" + (rand.nextInt(5)+1);
	}

	@Override
	public int getAttackDamage() {
		return 1;
	}
}
