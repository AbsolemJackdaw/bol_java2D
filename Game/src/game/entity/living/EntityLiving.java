package game.entity.living;

import java.awt.Graphics2D;

import engine.game.GameWorld;
import engine.game.MapObject;
import engine.music.Music;
import engine.save.DataTag;
import game.World;
import game.entity.EntityMovement;
import game.entity.living.player.Player;
import game.item.ItemStack;
import game.item.tool.ItemTool;
import game.item.tool.ItemTool.EnumTools;

public class EntityLiving extends MapObject{

	protected float health = 1;
	protected float maxHealth = 2;

	private boolean flicker;
	private int flickerTimer = 100;

	private double knockBackStart = 10;

	/**
	 * counter set by knockbackstart that counts down to how long this entity is knocked back.
	 * while this happens, the moveSpeed is replaced by knockBackForce
	 */
	protected double knockBack;
	private boolean knockedBack;

	/**how hard the entity is knocked back*/
	protected double knockBackForce;

	private double defMaxSpeed;
	private double defMoveSpeed;

	protected EntityMovement AI;

	public EntityLiving(GameWorld world, String uin) {
		super(world, uin);

		knockBackForce = 3d;
		AI = new EntityMovement();
	}

	@Override
	public void draw(Graphics2D g) {

		if(flickerTimer % 5 == 0){
			super.draw(g);
		}
	}

	protected int getHurtTimer(){
		return flickerTimer;
	}

	public void hurtEntity(float f, Player player){

		flicker = true;

		Music.play(getEntityHitSound());

		health -= f;
		
		if(health <= 0){
			this.remove=true;
			kill(player);
		}
		if(health > maxHealth)
			health = maxHealth;

		knockBack();

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

	@Override
	public void update() {
		super.update();

		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

		if(flicker)
			flickerTimer++;
		if(flickerTimer > 50){
			flickerTimer = 0;
			flicker = false;
		}

		if(knockedBack){
			knockEntityBack();
			if(knockBack > 0)
				knockBack -= 0.5d;
			else{
				maxSpeed = defMaxSpeed;
				moveSpeed = defMoveSpeed;
				knockedBack = false;
				knockBack = 0;
			}
		}

	}

	public void initMaxSpeed(double speed){
		maxSpeed = defMaxSpeed = speed;
	}

	public void initMoveSpeed(double speed){
		moveSpeed = defMoveSpeed = speed;
	}

	@Override
	public void onEntityHit(float damage) {
		hurtEntity(damage, null);
	}

	@Override
	public void onEntityHit(Player player) {

		Player p = (Player)player;

		int dmg = p.getAttackDamage();

		int wepDmg = 0;
		ItemStack wep = p.invArmor.getWeapon();

		if(wep != null && wep.getItem() instanceof ItemTool){
			ItemTool tool = (ItemTool)wep.getItem();
			if(EnumTools.SWORD == tool.getEffectiveness()){
				wepDmg = ((ItemTool)wep.getItem()).getEffectiveDamage(wep);
			}
			wep.damageStack(1);
		}

		hurtEntity(wepDmg + dmg, p );
	}


	/**
	 * Method called when the entity has no more health left.
	 * Use this method to set any drops in the player inventory.
	 * if no super call is used, do not forget to set this.remove to true !
	 * Player can be null !!
	 */
	public void kill(Player player){

		if(player!= null)
			if(getDrops() != null){
				int index;

				if(getDrops().length == 1)
					index = 0;
				else
					index = rand.nextInt(getDrops().length);

				if(player.setStackInNextAvailableSlot(getDrops()[index])){
					this.remove = true;
				}else{
					health = maxHealth;
				}
			}
			else
				remove = true;
		else
			this.remove = true;
	}

	public ItemStack[] getDrops() {
		return null;
	}

	@Override
	public boolean hasAnimation() {
		return true;
	}

	@Override
	public boolean persistantUpdate() {
		return true;
	}

	private void knockBack(){
		knockedBack = true;
		knockBack = knockBackStart; 
	}

	public boolean isKnockedBack(){
		return knockedBack;
	}

	public World getWorld() {
		return (World)super.getWorld();
	}

	public boolean isFlinching(){
		return flicker;
	}

	protected void knockEntityBack(){
		maxSpeed = knockBackForce;
		moveSpeed = knockBackForce/5;

		Player p = ((World)world).getPlayer();

		// if the player is looking left, knock the entity to the left
		if(p.facingRight){
			setVector(5, -2);
			setRight(true);
			facingRight = true;
		}
		else{ 
			setVector(-5, -2);
			setLeft(true);
			facingRight = false;
		}
	}
	
	public double getDefMaxSpeed() {
		return defMaxSpeed;
	}
	
	public double getDefMoveSpeed() {
		return defMoveSpeed;
	}
}