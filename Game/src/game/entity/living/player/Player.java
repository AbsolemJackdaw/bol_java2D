
package game.entity.living.player;

import static game.util.Constants.*;
import engine.game.MapObject;
import engine.image.Images;
import engine.imaging.Animation;
import engine.keyhandlers.KeyHandler;
import engine.keyhandlers.XboxController;
import engine.music.Music;
import engine.save.DataList;
import engine.save.DataTag;
import game.World;
import game.content.Loading;
import game.content.WorldTask;
import game.entity.EntityMovement;
import game.entity.block.breakable.BlockBreakable;
import game.entity.inventory.IInventory;
import game.entity.living.EntityLiving;
import game.item.Item;
import game.item.ItemStack;
import game.item.Items;
import game.item.armor.ItemArmor;
import game.item.armor.ItemBelt;
import game.item.block.ItemBlock;
import game.item.tool.ItemTool;
import game.item.tool.ItemTool.EnumTools;
import game.util.Util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Player extends EntityLiving implements IInventory{

	private boolean attacking;

	private Animation head = new Animation();
	private Animation arms = new Animation();
	private Animation legs = new Animation();

	private Animation armor_head = new Animation();
	private Animation armor_arms = new Animation();
	private Animation armor_extra = new Animation();

	protected EntityMovement movement = new EntityMovement();

	public boolean isCollidingWithBlock;

	protected List<MapObject> collidingEntities = new ArrayList<MapObject>();

	public boolean flyCheat;

	// for every animation, add the number of frames here
	// sample : 2,5,8,4 (2 for idle 0, 5 for walking 1, etc.
	//public static final int[] numFrames = {20, 10, 1, 1, 3 };

	public static final int[] playerSprites = {
		/*body (general)*/1,
		/*idle*/1,1,8,
		/*falling/jump*/2,2,
		/*running*/10,1,10, 
		/*weapon arms*/1,
		/*fight arms*/3,
		/*fight body parts*/3
	};

	public ArrayList<BufferedImage[]> playerSheet = Images.loadMultiAnimation(playerSprites, 32, 32, "/player/player_2.png");

	int weaponRotation = 0;

	private ItemStack[] inventory = new ItemStack[30];

	/**Where 0 is Helmet, 1 is Chest, 2 is Extra and 3 is Weapon*/
	private ItemStack[] armorItems = new ItemStack[4];

	public ArmorInventory armorInventory = new ArmorInventory();

	private boolean inWater;
	private boolean wasInwater;

	private float bufferHealth;

	private int airSupply = 1000;
	private int maxAirSupply = 1000;

	private int integerToTrackHeldItem = -1;

	public Player(World world) {
		super(world, "player");

		width = 32;
		height = 32;

		entitySizeX = 24;
		entitySizeY = 30;

		initMoveSpeed(0.5);
		initMaxSpeed(3);

		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		facingRight = true;

		setPosition(5, 5);

		initHealth(3f);

		knockBackForce = 2d;
	}

	@Override
	public boolean hasAnimation() {
		return true;
	}

	@Override
	public void hurtEntity(float f, Player p) {

		if(!isFlinching()){

			float reduction = 0f;
			
			if(this.armorInventory.getHelm() != null){
				reduction += ((ItemArmor)armorInventory.getHelm().getItem()).getDamageReduction();
			}
			if(this.armorInventory.getBody() != null){
				reduction += ((ItemArmor)armorInventory.getBody().getItem()).getDamageReduction();
			}
			
			if(reduction < 1f)
				reduction = 1f;
			
			float reduced = f / reduction;
			
			bufferHealth += reduced;
			float deducted = 0f;

			while(bufferHealth >= 0.5f){
				bufferHealth -= 0.5f;
				deducted += 0.5f;
			}

			super.hurtEntity(deducted, p);

		}
	}

	public void heal(float f){
		health+=f;

		if(health > getMaxHealth())
			health = getMaxHealth();
	}

	@Override
	public void draw(Graphics2D g) {

		if(getHurtTimer()%5 == 0){
			//Draw all parts here
			super.draw(g);
			
			super.draw(g, head);

			super.draw(g, arms);
			if(armor_arms.hasFrames())
				super.draw(g,armor_arms);
			
			super.draw(g, legs);
			
			if(armor_head.hasFrames())
				super.draw(g,armor_head);
			
			if(armor_extra.hasFrames())
				super.draw(g, armor_extra);
		}

		if(armorInventory.getWeapon() != null){

			BufferedImage weapon = armorInventory.getWeapon().getItem().getTexture();

			BufferedImage canvas = Util.rotateImage(weapon, weaponRotation);

			if (facingRight)
				g.drawImage(canvas, posX()+5, posY()-15, canvas.getWidth(), canvas.getHeight(), null);
			else
				g.drawImage(canvas, posX()+19, posY()-15, -canvas.getWidth() , canvas.getHeight(), null);
		}

	}

	@Override
	public void getNextPosition() {

		if(!inWater){

			movement.doPlayerMovement(this);

			// cannot move while attacking, except in air
			if ((currentAction == ACTION_ATTACK) && !(jumping || falling))
				dx = 0;

			if(wasInwater){
				setJumping(true);
				dy = jumpStart/2;
			}
			//fly mechanics ! :D
			if(flyCheat)
				if(!jumping && falling)
					if(KeyHandler.isPressed(KeyHandler.UP)){
						dy=jumpStart;
						setJumping(true);
					}
		}

		else{
			movement.doPlayerWaterMovement(this);
		}
	}

	@Override
	public void setRight(boolean b) {
		super.setRight(b);
		if(right)
			facingRight = true;
	}

	@Override
	public void setLeft(boolean b) {
		super.setLeft(b);
		if(left)
			facingRight = false;
	}

	private int delay = 20;

	public void handleInput(){

		setLeft(KeyHandler.isLeftKeyPressed());

		setRight(KeyHandler.isRightKeyPressed());

		setUp(KeyHandler.isUpKeyPressed());

		setDown(KeyHandler.isDownKeyPressed());

		if(XboxController.controller != null){
			setJumping(KeyHandler.isUpKeyPressed());
			if (KeyHandler.isPressed(KeyHandler.SPACE))
				setAttacking();
			if(KeyHandler.isHeldDown(KeyHandler.SPACE)){
				setAttacking();
			}
		}else{
			setJumping(KeyHandler.isUpKeyPressed());
			if (KeyHandler.isPressed(KeyHandler.SPACE))
				setAttacking();

			if(KeyHandler.isHeldDown(KeyHandler.SPACE)){

				if(delay <= 0){
					setAttacking();
					delay = 20;
				}
				delay --;
			}
		}


		if(KeyHandler.isPressed(KeyHandler.INTERACT)){

			for(MapObject mo : getWorld().getWorldEntities()){
				if(mo.intersects(this))
					mo.interact(this);
			}
		}

		for(int key : hotBarKeys)
			if(KeyHandler.isPressed(key)){
				int keyPressed = key - KeyHandler.ONE;

				if(getStackInSlot(keyPressed) != null){
					if(getStackInSlot(keyPressed).getItem() != null){
						Item item = getStackInSlot(keyPressed).getItem();
						//place down blocks
						if(item instanceof ItemBlock){
							item.useItem(getStackInSlot(keyPressed), tileMap, getWorld(),this, keyPressed);
						}
						//equip armor or weapon
						else if(item instanceof ItemTool){
							if(armorInventory.getWeapon() == null){
								armorInventory.setWeapon(getStackInSlot(keyPressed).copy());
								setStackInSlot(keyPressed, null);
							}else
								item.useItem(getStackInSlot(keyPressed), tileMap, getWorld(), this, keyPressed);
						}
						//any other item
						else
							item.useItem(getStackInSlot(keyPressed), tileMap, getWorld(), this, keyPressed);
					}
				}else{
					if(armorInventory.getWeapon() != null){
						setStackInSlot(keyPressed, armorInventory.getWeapon().copy());
						armorInventory.setWeapon(null);
					}
				}
			}
	}

	@Override
	public void update() {

		super.update();

		if(inWater){
			if(airSupply > 0){
				airSupply--;
			}

			else{
				if(-(airSupply) % 20 == 0){
					hurtEntity(0.5f, this);
				}
			}
		}else{
			airSupply = maxAirSupply;
		}

		if (currentAction == ACTION_ATTACK){

			if(this.armorInventory.getWeapon() != null){
				weaponRotation += 30;
				if(weaponRotation >= 360){
					attacking = false;
					weaponRotation = 0;
				}
			}
			else if (arms.hasPlayedOnce()) //arms needs to be looped, as only arms are animated
				attacking = false;
		}

		if(attacking && (currentAction != ACTION_ATTACK)) {

			for(MapObject obj : collidingEntities){

				matchTool(obj);
				if(obj.getPosX() > getPosX() && facingRight)
					obj.onEntityHit(this);
				else if( obj.getPosX() < getPosX() && !facingRight)
					obj.onEntityHit(this);

			}
		}

		//remove weapon if broken after attacking
		if(armorInventory.getWeapon() != null)
			if(armorInventory.getWeapon().getDamage() < 0){
				armorInventory.setWeapon(null);
				//TODO break sound and or animation	
			}

		updatePlayerAnimation();

		for(int i = 0; i < getInventory().getItems().length; i++){
			ItemStack stack = getInventory().getItems()[i];
			if(stack != null){
				Item item = stack.getItem();
				if(item.isUpdateAble())
					item.update(this, stack, i);
			}
		}

		for(WorldTask task : ((World)world).tasks){
			if(!task.isAchieved()){
				if(task.object().equals(WorldTask.JUMP) && KeyHandler.isPressed(KeyHandler.UP))
					task.update(1);
				if(task.object().equals(WorldTask.WALK)){
					if(KeyHandler.isLeftKeyHit() || KeyHandler.isRightKeyHit())
						task.update(1);
				}
				if(task.object().equals(WorldTask.SWIM) && isInWater())
					task.update(1);
			}
		}
	}

	/**equipes the player with the needed tool when hitting an entity, if the tool is acquired.*/
	private void matchTool(MapObject o) {

		//player needs a belt to auto - match tool !
		if(armorInventory.getStackInSlot(ItemArmor.EXTRA) == null)
			return;
		if(!(armorInventory.getStackInSlot(ItemArmor.EXTRA).getItem() instanceof ItemBelt))
			return;

		if(o instanceof BlockBreakable){
			BlockBreakable bl = (BlockBreakable)o;

			if(bl.getEffectiveTool() == EnumTools.AXE){
				if(armorInventory.getWeapon() == null){
					if(hasTool(EnumTools.AXE)){
						armorInventory.setWeapon(getStackInSlot(integerToTrackHeldItem).copy());
						getInventory().setStackInSlot(integerToTrackHeldItem, null);
					}
				}
				else if(!((ItemTool)armorInventory.getWeapon().getItem()).getEffectiveness().equals(EnumTools.AXE)){
					if(hasTool(EnumTools.AXE)){

						ItemStack heldTool = armorInventory.getWeapon().copy();
						ItemStack newTool = getStackInSlot(integerToTrackHeldItem).copy();

						armorInventory.setWeapon(newTool);
						setStackInSlot(integerToTrackHeldItem, null);
						setStackInSlot(integerToTrackHeldItem, heldTool.copy());
					}
				}
			}else if(bl.getEffectiveTool() == EnumTools.PICKAXE){
				if(armorInventory.getWeapon() == null){
					if(hasTool(EnumTools.PICKAXE)){
						armorInventory.setWeapon(getStackInSlot(integerToTrackHeldItem).copy());
						getInventory().setStackInSlot(integerToTrackHeldItem, null);
					}
				}
				else if(!((ItemTool)armorInventory.getWeapon().getItem()).getEffectiveness().equals(EnumTools.PICKAXE)){
					if(hasTool(EnumTools.PICKAXE)){
						ItemStack heldTool = armorInventory.getWeapon().copy();
						ItemStack newTool = getStackInSlot(integerToTrackHeldItem).copy();

						armorInventory.setWeapon(newTool);
						setStackInSlot(integerToTrackHeldItem, null);
						setStackInSlot(integerToTrackHeldItem, heldTool);
					}
				}
			}else if(bl.getEffectiveTool() == EnumTools.SWORD){
				if(armorInventory.getWeapon() == null){
					if(hasTool(EnumTools.SWORD))
						if(getInventory().hasStack(new ItemStack(Items.wood_sword,1))){
							armorInventory.setWeapon(getStackInSlot(integerToTrackHeldItem).copy());
							getInventory().setStackInSlot(integerToTrackHeldItem, null);
						}
				}
				else if(!((ItemTool)armorInventory.getWeapon().getItem()).getEffectiveness().equals(EnumTools.SWORD)){
					if(hasTool(EnumTools.SWORD)){
						ItemStack heldTool = armorInventory.getWeapon().copy();
						ItemStack newTool = getStackInSlot(integerToTrackHeldItem).copy();

						armorInventory.setWeapon(newTool);
						setStackInSlot(integerToTrackHeldItem, null);
						setStackInSlot(integerToTrackHeldItem, heldTool);
					}
				}
			}
		}
		else if (o instanceof EntityLiving){

			if(((EntityLiving)o).canPlayDeathAnimation()){
				if(armorInventory.getWeapon() == null){
					if(hasTool(EnumTools.SWORD)){
						armorInventory.setWeapon(getStackInSlot(integerToTrackHeldItem).copy());
						getInventory().setStackInSlot(integerToTrackHeldItem, null);
					}
				}
				else if(!((ItemTool)armorInventory.getWeapon().getItem()).getEffectiveness().equals(EnumTools.SWORD)){
					if(hasTool(EnumTools.SWORD)){
						ItemStack heldTool = armorInventory.getWeapon().copy();
						ItemStack newTool = getStackInSlot(integerToTrackHeldItem).copy();

						armorInventory.setWeapon(newTool);
						setStackInSlot(integerToTrackHeldItem, null);
						setStackInSlot(integerToTrackHeldItem, heldTool);
					}
				}
			}
		}

		integerToTrackHeldItem = -1; // re init the integer
	}

	/**
	 * checks if the player's inventory has the highest tier of asked tool
	 * @param tool : the tool, in enum, to be searched for
	 */
	private boolean hasTool(EnumTools tool){

		int intBuffer = -1;
		ItemStack stackBuffer = null;

		for(int i = 0; i < getInventory().getMaxSlots(); i ++){
			ItemStack stack = getInventory().getStackInSlot(i);

			if(stack != null){
				if(stack.getItem() instanceof ItemTool){
					ItemTool item = ((ItemTool)stack.getItem());

					if(item.getEffectiveness().equals(tool)){
						if(stackBuffer != null){
							if(item.getMaterial().getId() > ((ItemTool)stackBuffer.getItem()).getMaterial().getId()){
								stackBuffer = stack;
								intBuffer = i;
							}
						}else{
							stackBuffer = stack;
							intBuffer = i;
						}
					}
				}
			}
		}

		if(stackBuffer != null){
			integerToTrackHeldItem = intBuffer;
			return true;
		}

		return false;
	}

	public void setAttacking() {
		attacking = true;
	}

	/**return an array of images that conceals the body parts*/
	private BufferedImage[] getBodyPart(int i){
		return playerSheet.get(i);
	}
	
	private BufferedImage[] getArmorHead(int i){
		ItemStack extra = armorInventory.getStackInSlot(0);
		
		if(extra != null)
				return ((ItemArmor)extra.getItem()).getAnimationSheet().get(i);

		return null;
	}
	
	private BufferedImage[] getArmorBody(int i){
		ItemStack extra = armorInventory.getStackInSlot(1);
		
		if(extra != null)
				return ((ItemArmor)extra.getItem()).getAnimationSheet().get(i);

		return null;
	}

	private BufferedImage[] getArmorExtra(int i){
		ItemStack extra = armorInventory.getStackInSlot(2);
		
		if(extra != null)
				return ((ItemArmor)extra.getItem()).getAnimationSheet().get(i);

		return null;
	}
	
	public void updatePlayerAnimation() {
		// set animation
		if (attacking) {
			if (currentAction != ACTION_ATTACK) {
				currentAction = ACTION_ATTACK;

				if(armorInventory.getWeapon() != null){
					//body
					getAnimation().setFrames(getBodyPart(BODY_IDLE));
					head.setFrames(getBodyPart(HEAD_IDLE));
					arms.setFrames(getBodyPart(ARMS_ATTACK));
					legs.setFrames(getBodyPart(LEGS_IDLE));
					legs.setFrame(0);
					legs.setDelay(Animation.NONE);

					armor_extra.setFrames(getArmorExtra(ARMOR_EXTRA_IDLE));
					armor_head.setFrames(getArmorHead(ARMOR_HEAD_IDLE));

				}else{
					getAnimation().setFrames(getBodyPart(BODY_ATTACK));
					getAnimation().setDelay(Animation.NONE);
					getAnimation().setFrame(2);
					head.setFrames(getBodyPart(HEAD_ATTACK));
					head.setFrame(1);
					head.setDelay(Animation.NONE);
					arms.setFrames(getBodyPart(ARMS_ATTACK));
					arms.setDelay(50);
					legs.setFrames(getBodyPart(LEGS_ATTACK));
					legs.setFrame(0);
					legs.setDelay(Animation.NONE);

					armor_extra.setFrames(getArmorExtra(ARMOR_EXTRA_ATTACK));
					armor_head.setFrames(getArmorHead(ARMOR_HEAD_ATTACK));

				}


			}
		} else if (dy > 0) {
			if (currentAction != ACTION_FALLING) {
				currentAction = ACTION_FALLING;

				getAnimation().setFrames(getBodyPart(BODY_RUN));
				getAnimation().setDelay(75);

				head.setFrames(getBodyPart(HEAD_JUMP));
				head.setFrame(1);
				head.setDelay(Animation.NONE);
				arms.setFrames(getBodyPart(ARM_JUMP));
				arms.setFrame(0);
				arms.setDelay(Animation.NONE);
				legs.setFrames(getBodyPart(LEG_JUMP));
				legs.setFrame(1);
				legs.setDelay(Animation.NONE);

				armor_extra.setFrames(getArmorExtra(ARMOR_EXTRA_JUMP));
				armor_head.setFrames(getArmorHead(ARMOR_HEAD_FALL));

			}
		} else if (dy < 0) {
			if (currentAction != ACTION_JUMPING) {
				currentAction = ACTION_JUMPING;

				getAnimation().setFrames(getBodyPart(BODY_RUN));
				head.setFrames(getBodyPart(HEAD_JUMP));
				head.setFrame(0);
				head.setDelay(Animation.NONE);
				arms.setFrames(getBodyPart(ARM_JUMP));
				arms.setFrame(0);
				arms.setDelay(Animation.NONE);
				legs.setFrames(getBodyPart(LEG_JUMP));
				legs.setFrame(1);
				legs.setDelay(Animation.NONE);

				armor_extra.setFrames(getArmorExtra(ARMOR_EXTRA_JUMP));
				armor_head.setFrames(getArmorHead(ARMOR_HEAD_JUMP));

			}
		} else if (left || right) {
			if (currentAction != ACTION_WALK) {
				currentAction = ACTION_WALK;

				getAnimation().setFrames(getBodyPart(BODY_RUN));
				head.setFrames(getBodyPart(HEAD_IDLE));
				arms.setFrames(getBodyPart(ARMS_RUN));
				arms.setDelay(75);
				legs.setFrames(getBodyPart(LEGS_RUN));
				legs.setDelay(75);

				armor_extra.setFrames(getArmorExtra(ARMOR_EXTRA_RUN));
				armor_extra.setDelay(75);
				
				armor_head.setFrames(getArmorHead(ARMOR_HEAD_IDLE));


			}
		} else if (currentAction != ACTION_IDLE) {
			currentAction = ACTION_IDLE;

			//body
			getAnimation().setFrames(getBodyPart(BODY_IDLE));
			head.setFrames(getBodyPart(HEAD_IDLE));
			//change arm animation from idle in sheet
			arms.setFrames(getBodyPart(ARMS_IDLE));
			arms.setDelay(Animation.NONE);
			legs.setFrames(getBodyPart(LEGS_IDLE));
			legs.setDelay(75);
			if(armorInventory.getWeapon() != null){
				legs.setFrame(0);
				legs.setDelay(Animation.NONE);
			}

			armor_extra.setFrames(getArmorExtra(ARMOR_EXTRA_IDLE));
			armor_head.setFrames(getArmorHead(ARMOR_HEAD_IDLE));

		}
		if(armorInventory.getWeapon() != null){
			arms.setFrames(getBodyPart(ARMS_WEAPON));
		}

		getAnimation().update();
		head.update();
		arms.update();
		legs.update();

		if(armor_extra.hasFrames())
			armor_extra.update();
		if(armor_head.hasFrames())
			armor_head.update();
		if(armor_arms.hasFrames())
			armor_arms.update();
	}

	@Override
	public void checkTileMapCollision() {

		if(tileMap.getBlockID(currentRow, currentColumn) == 6){

			boolean flag = false;

			for(WorldTask task : ((World)world).tasks){
				if(!task.isAchieved()){
					flag = true;
					break;
				}
			}

			if(!flag){
				Loading.gotoNextLevel(getWorld().gsm);
			}else{
				setVector(-4, 0);
			}
		}

		if(tileMap.getBlockID(currentRow, currentColumn) == 7){
			Loading.gotoPreviousLevel(getWorld().gsm);
		}

		if(wasInwater)
			wasInwater = false;

		if(tileMap.getBlockID(currentRow, currentColumn) == 10 || tileMap.getBlockID(currentRow, currentColumn) == 9){

			if(!inWater)
				Music.play("splash");

			inWater = true;
		}else{

			if(inWater)
				wasInwater = true;

			inWater = false;
		}

		super.checkTileMapCollision();
	}

	public boolean isInWater() {
		return inWater;
	}

	public int getAttackDamage(){
		return 1;
	}

	/*======================INVENTORY====================*/

	@Override
	public ItemStack[] getItems() {
		return inventory;
	}

	@Override
	public boolean hasStack(ItemStack stack) {
		int extra = 0;

		if(armorItems[ItemArmor.EXTRA] != null){
			ItemStack is = armorItems[ItemArmor.EXTRA];
			Item i = is.getItem();
			if(i instanceof ItemBelt)
				extra = ((ItemBelt)i).getInventorySlots();
		}

		for(int i = 0; i < 10 + extra; i++)
			if(getStackInSlot(i) != null && getStackInSlot(i).getItem().equals(stack.getItem()))
				return true;

		return false;
	}

	public boolean hasItem(Item item){
		int extra = 0;
		if(armorItems[ItemArmor.EXTRA] != null){
			ItemStack is = armorItems[ItemArmor.EXTRA];
			Item i = is.getItem();
			if(i instanceof ItemBelt)
				extra = ((ItemBelt)i).getInventorySlots();
		}

		for(int i = 0; i < 10+extra; i++)
			if(getStackInSlot(i) != null && getStackInSlot(i).getItem().equals(item))
				return true;
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public int getMaxSlots() {
		int extra = 0;
		if(armorItems[ItemArmor.EXTRA] != null){
			ItemStack is = armorItems[ItemArmor.EXTRA];
			Item i = is.getItem();
			if(i instanceof ItemBelt)
				extra = ((ItemBelt)i).getInventorySlots();
		}

		return 10+extra;
	}

	@Override
	public boolean setStackInNextAvailableSlot(ItemStack item) {

		if(item == null)
			return true;

		int extra = 0;

		if(armorItems[ItemArmor.EXTRA] != null){
			ItemStack is = armorItems[ItemArmor.EXTRA];
			Item i = is.getItem();
			if(i instanceof ItemBelt)
				extra = ((ItemBelt)i).getInventorySlots();
		}

		for(int i = 0; i < 10+extra; i++)
			if(getStackInSlot(i) != null){
				if (inventory[i].getItem().getUIN().equals(item.getItem().getUIN()) && inventory[i].getItem().isStackable()){
					setStackInSlot(i, item);
					return true;
				}
			}

		for(int i = 0; i < 10+extra; i++)
			if(getStackInSlot(i) == null){
				setStackInSlot(i, item);
				return true;
			}

		return false;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {

		if(inventory[slot] == null && stack != null)
			inventory[slot] = stack.copy();
		else if (stack == null ){
			inventory[slot] = null;
			return;
		}
		else if(inventory[slot].getItem().equals(stack.getItem()))
			inventory[slot].stackSize += stack.stackSize;
		else
			System.out.println("something tried to replace the existing item" + inventory[slot].getItem().getUIN() +
					" by "+ stack.getItem().getUIN() );


		if(stack != null || inventory[slot] != null && inventory[slot].getItem().equals(stack.getItem()))
			for(WorldTask task : ((World)world).tasks){
				if(!task.isAchieved()){
					if(task.object().equals(stack.getItem().getDisplayName())){
						task.update(stack.stackSize);
					}
				}
			}
	}

	@Override
	public void removeStack(int slot) {
		inventory[slot] = null;
	}

	@Override
	public boolean hasStackInSlot(int slot) {
		return (inventory[slot] != null);
	}

	@Override
	public IInventory getInventory() {
		return this;
	}

	@Override
	public void writeToSave(DataTag data) {
		super.writeToSave(data);

		DataList list = new DataList();
		for(int slot = 0; slot < inventory.length; slot++){
			ItemStack stack = getStackInSlot(slot);
			if(stack != null){
				DataTag tag = new DataTag();
				stack.writeToSave(tag);
				tag.writeInt("slot", slot);
				list.write(tag);
			}
		}
		data.writeList("items", list);

		DataList armor = new DataList();
		for(int slot = 0; slot < this.armorItems.length; slot++){
			ItemStack stack = armorInventory.getStackInSlot(slot);
			if(stack != null){
				DataTag tag = new DataTag();
				stack.writeToSave(tag);
				tag.writeInt("slot", slot);
				armor.write(tag);
			}
		}
		data.writeList("armorItems", armor);
	}

	@Override
	public void readFromSave(DataTag data) {
		super.readFromSave(data);

		DataList list = data.readList("items");
		for(int i = 0; i < list.data().size(); i++){
			DataTag tag = list.readArray(i);
			int slot = tag.readInt("slot");
			ItemStack stack = ItemStack.createFromSave(tag);
			setStackInSlot(slot, stack);
		}

		DataList armor = data.readList("armorItems");
		for(int i = 0; i < armor.data().size(); i++){
			DataTag tag = armor.readArray(i);
			int slot = tag.readInt("slot");
			ItemStack stack = ItemStack.createFromSave(tag);
			armorInventory.setStackInSlot(slot, stack);
		}
	}

	public class ArmorInventory implements IInventory{

		public ItemStack getWeapon(){
			return armorItems[3];
		}

		public void setWeapon(ItemStack stack){
			armorItems[3] = stack;
		}

		public ItemStack getExtra(){
			return armorItems[2];
		}

		public ItemStack getBody(){
			return armorItems[1];
		}

		public ItemStack getHelm(){
			return armorItems[0];
		}

		@Override
		public ItemStack[] getItems() {
			return armorItems;
		}

		@Override
		public boolean hasStack(ItemStack stack) {
			for(int i = 0; i < armorItems.length; i++)
				if(getStackInSlot(i) != null)
					return true;

			return false;
		}

		@Override
		public ItemStack getStackInSlot(int slot) {
			return armorItems[slot];
		}

		@Override
		public int getMaxSlots() {
			return armorItems.length;
		}

		/**sets the stack in a slot if any available, and return true if it was able to.*/
		@Override
		public boolean setStackInNextAvailableSlot(ItemStack item) {
			for(int i = 0; i < armorItems.length; i++)
				if(getStackInSlot(i) == null){
					setStackInSlot(i, item);
					return true;
				}else{
					if (armorItems[i].getItem().equals(item.getItem())){
						setStackInSlot(i, item);
						return true;
					}
				}
			return false;
		}

		@Override
		public void setStackInSlot(int slot, ItemStack stack) {
			if(armorItems[slot] == null)
				armorItems[slot] = stack;
			else if (stack == null)
				armorItems[slot] = null;
			else if(armorItems[slot].getItem().equals(stack.getItem())){
				armorItems[slot].stackSize += stack.stackSize;
			}
		}

		@Override
		public void removeStack(int slot) {
			armorItems[slot] = null;
		}

		@Override
		public boolean hasStackInSlot(int slot) {
			return (armorItems[slot] != null);
		}

		@Override
		public IInventory getInventory() {
			return this;
		}

		@Override
		public int getSlotForStack(ItemStack stack) {
			if(stack == null)
				return -1;

			int extra = 0;
			if(armorItems[ItemArmor.EXTRA] != null){
				ItemStack is = armorItems[ItemArmor.EXTRA];
				Item i = is.getItem();
				if(i instanceof ItemBelt)
					extra = ((ItemBelt)i).getInventorySlots();
			}

			for(int i = 0; i < 10 + extra; i++){
				if(getStackInSlot(i) != null)
					if(getStackInSlot(i).getItem().equals(stack.getItem()))
						return i;
			}

			return -1;
		}

	}

	/**returns slot index for that item*/
	@Override
	public int getSlotForStack(ItemStack stack) {
		if(stack == null)
			return -1;

		int extra = 0;
		if(armorItems[ItemArmor.EXTRA] != null){
			ItemStack is = armorItems[ItemArmor.EXTRA];
			Item i = is.getItem();
			if(i instanceof ItemBelt)
				extra = ((ItemBelt)i).getInventorySlots();
		}

		for(int i = 0; i < 10 + extra; i++){
			if(getStackInSlot(i) != null)
				if(getStackInSlot(i).getItem().equals(stack.getItem()))
					return i;
		}

		return -1;
	}


	/**used in torchlight for lighting at night*/
	public int getRadius(){
		//if player has torch, get light strenght from torch
		return 200;
	}

	public World getWorld(){
		return (World)this.world;
	}

	/**
	 * Returns list with blocks the player is currently colliding with
	 */
	public List<MapObject> getCollidingMapObjects(){
		return collidingEntities;
	}

	/**
	 *Adds a block to the list of blocks the player is colliding with 
	 */
	public void setCollidingMapObjects(MapObject obj){
		collidingEntities.add(obj);
	}

	@Override
	protected void knockEntityBack() {

		maxSpeed = knockBackForce;
		moveSpeed = knockBackForce/5;

		if(!facingRight){
			setVector(3, -2);
			left = true;
		}
		else{ 
			setVector(-3, -2);
			right = true;
		}
	}

	public boolean isDead(){
		return health <= 0f;
	}

	public int getAirSupply() {
		return airSupply;
	}

	public int getMaxAirSupply() {
		return maxAirSupply;
	}

	public void setMaxAirSupply(int maxAirSupply) {
		this.maxAirSupply = airSupply = maxAirSupply;
	}
}
