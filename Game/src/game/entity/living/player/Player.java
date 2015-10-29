
package game.entity.living.player;

import game.Loading;
import game.World;
import game.content.Images;
import game.content.save.DataList;
import game.content.save.DataTag;
import game.entity.Animation;
import game.entity.MapObject;
import game.entity.block.BlockBreakable;
import game.entity.inventory.IInventory;
import game.entity.living.EntityLiving;
import game.entity.living.enemy.EntityEnemy;
import game.item.Item;
import game.item.ItemArmor;
import game.item.ItemBelt;
import game.item.ItemBlock;
import game.item.ItemStack;
import game.item.ItemTool;
import game.item.Items;
import game.util.Constants;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import base.main.keyhandler.KeyHandler;
import base.main.keyhandler.XboxController;
import base.tilemap.TileMap;

public class Player extends EntityLiving implements IInventory{

	private boolean flinching;
	private long flinchTimer;

	private boolean attacking;

	private static final int BODY_IDLE = 0;
	private static final int HEAD_IDLE = 1;
	private static final int ARMS_IDLE = 2;
	private static final int LEGS_IDLE = 3;

	private static final int HEAD_JUMP = 4;
	private static final int ARM_JUMP = 5; //same as leg, but 1st frame
	private static final int LEG_JUMP = 5; //same as arm, but 2nd frame

	private static final int ARMS_RUN = 6;
	private static final int BODY_RUN = 7;
	private static final int LEGS_RUN = 8;

	private static final int ARMS_WEAPON = 9;

	private static final int ARMS_ATTACK = 10;
	private static final int LEGS_ATTACK = 11; //frame 0
	private static final int HEAD_ATTACK = 11; //frame 1
	private static final int BODY_ATTACK = 11; //frame 2

	private static final int ARMOR_EXTRA_IDLE = 0;
	private static final int ARMOR_EXTRA_ATTACK = 1;
	private static final int ARMOR_EXTRA_JUMP = 2;
	private static final int ARMOR_EXTRA_RUN = 3;

	private static final int ARMOR_HEAD_IDLE = 0;
	private static final int ARMOR_TORSO_IDLE = 1;

	private static final int ARMOR_HEAD_RUN = 3;
	private static final int ARMOR_TORSO_RUN = 4;

	private static final int ARMOR_HEAD_ATTACK = 6;
	private static final int ARMOR_TORSO_ATTACK = 7;

	private static final int ACTION_ATTACK = 0;
	private static final int ACTION_WALK = 1;
	private static final int ACTION_JUMPING = 2;
	private static final int ACTION_FALLING = 3;
	private static final int ACTION_IDLE = 4;

	private Animation head = new Animation();
	private Animation arms = new Animation();
	private Animation legs = new Animation();

	private Animation armor_head = new Animation();
	private Animation armor_arms = new Animation();
	private Animation armor_extra = new Animation();

	// for every animation, add the number of frames here
	// sample : 2,5,8,4 (2 for idle 0, 5 for walking 1, etc.
	//public static final int[] numFrames = {20, 10, 1, 1, 3 };

	private List<MapObject> collidingEntities = new ArrayList<MapObject>();

	public static final int[] playerSprites = {
		/*body (general)*/1,
		/*idle*/1,1,8,
		/*falling/jump*/2,2,
		/*running*/9,1,10,
		/*weapon arms*/1,
		/*fight arms*/3,
		/*fight body parts*/3
	};

	public static final int[] beltAnim = {
		/*body idle*/1,
		/*body attack*/1,
		/*falling/jump*/1,
		/*running*/10,
	};

	public ArrayList<BufferedImage[]> playerSheet = Images.loadMultiAnimation(playerSprites, 32, 32, "/player/player_2.png");
	public ArrayList<BufferedImage[]> beltSheet = Images.loadMultiAnimation(beltAnim, 32, 32, "/player/belt_anim.png");


	int weaponRotation = 0;

	private ItemStack[] inventory = new ItemStack[30];
	private ItemStack[] armorItems = new ItemStack[4];
	//ARMOR INFO
	//0 helm
	//1 chest
	//2 extra
	//3 weapon

	public ArmorInventory invArmor = new ArmorInventory();

	public boolean isCollidingWithBlock;

	private boolean inWater;

	public Player(TileMap tm, World world) {
		super(tm, world, "player");

		width = 32;
		height = 32;

		entitySizeX = 20;
		entitySizeY = 30;

		moveSpeed = 0.5; // inital walking speed. you speed up as you walk
		maxSpeed = 3; // change to jump farther and walk faster
		stopSpeed = 0.4;
		fallSpeed = 0.15; // affects falling and jumping
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		facingRight = true;

		setPosition(3*32, 3*32);

	}

	@Override
	public boolean hasAnimation() {
		return true;
	}

	@Override
	public void draw(Graphics2D g) {

		if (flinching) {
			final long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (((elapsed / 100) % 2) == 0)
				g.setColor(Color.RED);
			else
				g.setColor(Color.WHITE);
			//				return;
		}

		//Draw all parts here
		super.draw(g);
		super.draw(g, head);
		super.draw(g, arms);
		super.draw(g, legs);
		//armor parts
		if(armor_extra.hasFrames())
			super.draw(g, armor_extra);

		if(invArmor.getWeapon() != null){
			BufferedImage weapon = invArmor.getWeapon().getItem().getTexture();

			AffineTransform tx = new AffineTransform();
			tx.rotate(Math.toRadians(weaponRotation), weapon.getWidth() / 2, weapon.getHeight() / 2);

			AffineTransformOp op = new AffineTransformOp(tx,
					AffineTransformOp.TYPE_BILINEAR);
			weapon = op.filter(weapon, null);

			if (facingRight)
				g.drawImage(weapon, posX()+10, posY()+5, 24, 24, null);
			else
				g.drawImage(weapon, posX()+10, posY()+5, -24, 24, null);

		}

	}

	@Override
	public void getNextPosition() {

		if(!inWater){
			// movement
			if (left) {
				dx -= moveSpeed;
				if (dx < -maxSpeed)
					dx = -maxSpeed;
			} else if (right) {
				dx += moveSpeed;
				if (dx > maxSpeed)
					dx = maxSpeed;
			} else if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0)
					dx = 0;
			} else if (dx < 0) {
				dx += stopSpeed;
				if (dx > 0)
					dx = 0;
			}

			// cannot move while attacking, except in air
			if ((currentAction == ACTION_ATTACK) && !(jumping || falling))
				dx = 0;

			// jumping
			if (jumping && !falling) {
				dy = jumpStart;
				falling = true;
				//Music.play("jump_" + (rand.nextInt(5)+1));
			}

			// falling
			if (falling) {
				dy += fallSpeed;

				if (dy > 0)
					jumping = false;
				if ((dy < 0) && !jumping)
					dy += stopJumpSpeed;

				if (dy > maxFallSpeed)
					dy = maxFallSpeed;
			}
		}

		else{

			if (left) {
				dx -= moveSpeed/2;
				if (dx < -maxSpeed/2)
					dx = -maxSpeed/2;
			} else if (right) {
				dx += moveSpeed/2;
				if (dx > maxSpeed/2)
					dx = maxSpeed/2;
			} else if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0)
					dx = 0;
			} else if (dx < 0) {
				dx += stopSpeed;
				if (dx > 0)
					dx = 0;
			}

			if (up) {
				dy -= moveSpeed/2;
				if (dy < -maxSpeed/2)
					dy = -maxSpeed/2;
				
				//TODO make a possible better check ? 
				if(world.tileMap.getBlockID(currRow+1, currCol) > 20 || world.tileMap.getBlockID(currRow-1, currCol) > 20){
					dy -= moveSpeed;
					if (dy < -maxSpeed)
						dy = -maxSpeed;
				}
			} else if (!up && falling) {
				dy += moveSpeed/2;
				if (dy > maxSpeed/2)
					dy = maxSpeed/2;
			} else if (dy > 0) {
				dy -= stopSpeed;
				if (dy < 0)
					dy = 0;
			} else if (dy < 0) {
				dy += stopSpeed;
				if (dy > 0)
					dy = 0;
			}
		}
	}

	public void handleInput(){

		setLeft(KeyHandler.isLeftKeyPressed());

		setRight(KeyHandler.isRightKeyPressed());

		setUp(KeyHandler.isUpKeyPressed());

		setDown(KeyHandler.isDownKeyPressed());

		if(XboxController.controller != null){
			setJumping(KeyHandler.keyState[KeyHandler.SPACE]);
			if (KeyHandler.isPressed(KeyHandler.SPACE))
				setAttacking();
		}else{
			setJumping(KeyHandler.isUpKeyPressed());
			if (KeyHandler.isPressed(KeyHandler.SPACE))
				setAttacking();
		}


		if(KeyHandler.isPressed(KeyHandler.INTERACT))
			for(MapObject o : getWorld().listWithMapObjects)
				if(o.intersects(this))
					o.interact(this, o);

		for(int key : Constants.hotBarKeys)
			if(KeyHandler.isPressed(key)){
				int keyPressed = key - KeyHandler.ONE;

				if(getStackInSlot(keyPressed) != null){
					if(getStackInSlot(keyPressed).getItem() != null){
						Item item = getStackInSlot(keyPressed).getItem();
						//place down blocks
						if(item instanceof ItemBlock){
							ItemBlock ib = (ItemBlock)item;
							item.useItem(ib, tileMap, getWorld(),this, keyPressed);
						}
						//equip armor or weapon
						else if(item instanceof ItemTool){
							if(invArmor.getWeapon() == null){
								invArmor.setWeapon(getStackInSlot(keyPressed).copy());
								setStackInSlot(keyPressed, null);
							}else
								item.useItem(getStackInSlot(keyPressed).getItem(), tileMap, getWorld(), this, keyPressed);
						}
						//any other item
						else
							item.useItem(getStackInSlot(keyPressed).getItem(), tileMap, getWorld(), this, keyPressed);
					}
				}else{
					if(invArmor.getWeapon() != null){
						setStackInSlot(keyPressed, invArmor.getWeapon().copy());
						invArmor.setWeapon(null);
					}
				}
			}
	}

	@Override
	public void update() {

		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

		if (currentAction == ACTION_ATTACK){

			if(this.invArmor.getWeapon() != null){
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

				entitySizeX += 5;
				matchTool(obj);
				if(obj.getScreenXpos() > getScreenXpos() && facingRight)
					obj.onEntityHit(this, obj);
				else if( obj.getScreenXpos() < getScreenXpos() && !facingRight)
					obj.onEntityHit(this, obj);

				entitySizeX -= 5;
			}


		}

		//remove weapon if broken after attacking
		if(invArmor.getWeapon() != null)
			if(invArmor.getWeapon().getDamage() < 0){
				invArmor.setWeapon(null);
				//TODO break sound and or animation	
			}

		//check to stop flinching, and ready to get hurt again
		if (flinching) {
			final long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 1200)
				flinching = false;
		}

		updatePlayerAnimation();

		// set direction
		if (currentAction != ACTION_ATTACK) {
			if (right)
				facingRight = true;
			if (left)
				facingRight = false;
		}

		for(ItemStack stack : getInventory().getItems()){
			if(stack != null){
				Item item = stack.getItem();
				if(item.isUpdateAble())
					item.update();
			}
		}



	}

	/**equipes the player with the needed tool when hitting an entity, if the tool is acquired.*/
	private void matchTool(MapObject o) {

		//player needs a belt to auto - match tool !
		if(invArmor.getStackInSlot(ItemArmor.EXTRA) == null)
			return;
		if(!(invArmor.getStackInSlot(ItemArmor.EXTRA).getItem() instanceof ItemBelt))
			return;

		if(o instanceof BlockBreakable){
			BlockBreakable bl = (BlockBreakable)o;

			if(bl.getEffectiveTool() == ItemTool.AXE){
				if(invArmor.getWeapon() == null){
					if(getInventory().hasStack(new ItemStack(Items.axe,1))){
						int i = getSlotForStack(new ItemStack(Items.axe, 1));
						invArmor.setWeapon(getStackInSlot(i).copy());
						getInventory().setStackInSlot(i, null);
					}
				}
				else if(!invArmor.getWeapon().getItem().equals(Items.axe)){
					if(getInventory().hasStack(new ItemStack(Items.axe,1))){
						int i = getSlotForStack(new ItemStack(Items.axe, 1));

						ItemStack heldTool = invArmor.getWeapon().copy();
						ItemStack newTool = getStackInSlot(i).copy();

						invArmor.setWeapon(newTool);
						setStackInSlot(i, null);
						setStackInSlot(i, heldTool.copy());
					}
				}
			}else if(bl.getEffectiveTool() == ItemTool.PICKAXE){
				if(invArmor.getWeapon() == null){
					if(getInventory().hasStack(new ItemStack(Items.pickaxe,1))){
						int i = getSlotForStack(new ItemStack(Items.pickaxe, 1));
						invArmor.setWeapon(getStackInSlot(i).copy());
						getInventory().setStackInSlot(i, null);
					}
				}
				else if(!invArmor.getWeapon().getItem().equals(Items.pickaxe)){
					if(getInventory().hasStack(new ItemStack(Items.pickaxe,1))){
						int i = getSlotForStack(new ItemStack(Items.pickaxe, 1));
						ItemStack heldTool = invArmor.getWeapon().copy();
						ItemStack newTool = getStackInSlot(i).copy();

						invArmor.setWeapon(newTool);
						setStackInSlot(i, null);
						setStackInSlot(i, heldTool);
					}
				}
			}else if(bl.getEffectiveTool() == ItemTool.SWORD){
				if(invArmor.getWeapon() == null){
					if(getInventory().hasStack(new ItemStack(Items.sword,1))){
						int i = getSlotForStack(new ItemStack(Items.sword, 1));
						invArmor.setWeapon(getStackInSlot(i).copy());
						getInventory().setStackInSlot(i, null);
					}
				}
				else if(!invArmor.getWeapon().getItem().equals(Items.sword)){
					if(getInventory().hasStack(new ItemStack(Items.sword,1))){
						int i = getSlotForStack(new ItemStack(Items.sword, 1));
						ItemStack heldTool = invArmor.getWeapon().copy();
						ItemStack newTool = getStackInSlot(i).copy();

						invArmor.setWeapon(newTool);
						setStackInSlot(i, null);
						setStackInSlot(i, heldTool);
					}
				}
			}
		}
		else if (o instanceof EntityLiving){
			if(invArmor.getWeapon() == null){
				if(getInventory().hasStack(new ItemStack(Items.sword,1))){
					int i = getSlotForStack(new ItemStack(Items.sword, 1));
					invArmor.setWeapon(getStackInSlot(i).copy());
					getInventory().setStackInSlot(i, null);
				}
			}
			else if(!invArmor.getWeapon().getItem().equals(Items.sword)){
				if(getInventory().hasStack(new ItemStack(Items.sword,1))){
					int i = getSlotForStack(new ItemStack(Items.sword, 1));
					ItemStack heldTool = invArmor.getWeapon().copy();
					ItemStack newTool = getStackInSlot(i).copy();

					invArmor.setWeapon(newTool);
					setStackInSlot(i, null);
					setStackInSlot(i, heldTool);
				}
			}
		}
	}

	public void setAttacking() {
		attacking = true;
	}

	/**return an array of images that conceals the body parts*/
	private BufferedImage[] getBodyPart(int i){
		return playerSheet.get(i);
	}

	private BufferedImage[] getArmorExtra(int i){
		ItemStack extra = invArmor.getStackInSlot(2);

		if(extra != null)
			if(extra.getItem() instanceof ItemBelt)
				return beltSheet.get(i);

		return null;
	}

	private void updatePlayerAnimation() {
		// set animation
		if (attacking) {
			if (currentAction != ACTION_ATTACK) {
				currentAction = ACTION_ATTACK;

				if(invArmor.getWeapon() != null){
					//body
					getAnimation().setFrames(getBodyPart(BODY_IDLE));
					head.setFrames(getBodyPart(HEAD_IDLE));
					arms.setFrames(getBodyPart(ARMS_ATTACK));
					legs.setFrames(getBodyPart(LEGS_IDLE));
					legs.setFrame(0);
					legs.setDelay(Animation.NONE);

					armor_extra.setFrames(getArmorExtra(ARMOR_EXTRA_IDLE));

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
				}


			}
		} else if (dy > 0) {
			if (currentAction != ACTION_FALLING) {
				currentAction = ACTION_FALLING;

				getAnimation().setFrames(getBodyPart(BODY_RUN));
				getAnimation().setDelay(100);

				head.setFrames(getBodyPart(HEAD_JUMP));
				head.setFrame(1);
				head.setDelay(Animation.NONE);
				arms.setFrames(getBodyPart(ARM_JUMP));
				arms.setFrame(0);
				arms.setDelay(Animation.NONE);
				legs.setFrames(getBodyPart(LEG_JUMP));
				legs.setFrame(1);
				legs.setDelay(Animation.NONE);

				armor_extra.setFrames(getArmorExtra(ARMOR_EXTRA_RUN));
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
			if(invArmor.getWeapon() != null){
				legs.setFrame(0);
				legs.setDelay(Animation.NONE);
			}

			armor_extra.setFrames(getArmorExtra(ARMOR_EXTRA_IDLE));
		}
		if(invArmor.getWeapon() != null){
			arms.setFrames(getBodyPart(ARMS_WEAPON));
		}

		getAnimation().update();
		head.update();
		arms.update();
		legs.update();

		if(armor_extra.hasFrames())
			armor_extra.update();
	}

	@Override
	public void checkTileMapCollision() {

		if(tileMap.getBlockID(currCol, currRow) == 6){
			Loading.gotoNextLevel(getWorld().gsm);
		}

		if(tileMap.getBlockID(currCol, currRow) == 7){
			Loading.gotoPreviousLevel(getWorld().gsm);
		}

		if(tileMap.getBlockID(currCol, currRow) == 10 || tileMap.getBlockID(currCol, currRow) == 9){
			inWater = true;
		}else
			inWater = false;

		super.checkTileMapCollision();
	}

	public int getAttackDamage(){
		return 1;
	}

	@Override
	public void onEntityHit(Player p, MapObject mo) {

		if(mo instanceof EntityEnemy){
			//			EntityEnemy ee = (EntityEnemy)mo;
			//playerhealth -= ee.getAttackDamage();
			//TODO do particles
		}

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
		if(inventory[slot] == null)
			inventory[slot] = stack;
		else if (stack == null && inventory[slot] != null)
			inventory[slot] = null;
		else if(inventory[slot].getItem().equals(stack.getItem()))
			inventory[slot].stackSize += stack.stackSize;
		else
			System.out.println("something tried to replace the existing item" + inventory[slot].getItem().getUIN() +
					" by "+ stack.getItem().getUIN() );
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
			ItemStack stack = invArmor.getStackInSlot(slot);
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
			invArmor.setStackInSlot(slot, stack);
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
}
