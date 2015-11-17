
package game.entity.living.player;

import static game.util.Constants.ACTION_ATTACK;
import static game.util.Constants.ACTION_FALLING;
import static game.util.Constants.ACTION_IDLE;
import static game.util.Constants.ACTION_JUMPING;
import static game.util.Constants.ACTION_WALK;
import static game.util.Constants.ARMOR_EXTRA_ATTACK;
import static game.util.Constants.ARMOR_EXTRA_IDLE;
import static game.util.Constants.ARMOR_EXTRA_JUMP;
import static game.util.Constants.ARMOR_EXTRA_RUN;
import static game.util.Constants.ARMS_ATTACK;
import static game.util.Constants.ARMS_IDLE;
import static game.util.Constants.ARMS_RUN;
import static game.util.Constants.ARMS_WEAPON;
import static game.util.Constants.ARM_JUMP;
import static game.util.Constants.BODY_ATTACK;
import static game.util.Constants.BODY_IDLE;
import static game.util.Constants.BODY_RUN;
import static game.util.Constants.HEAD_ATTACK;
import static game.util.Constants.HEAD_IDLE;
import static game.util.Constants.HEAD_JUMP;
import static game.util.Constants.LEGS_ATTACK;
import static game.util.Constants.LEGS_IDLE;
import static game.util.Constants.LEGS_RUN;
import static game.util.Constants.LEG_JUMP;
import static game.util.Constants.hotBarKeys;
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
import game.entity.EntityLiving;
import game.entity.EntityMovement;
import game.entity.block.breakable.BlockBreakable;
import game.entity.inventory.IInventory;
import game.item.Item;
import game.item.ItemArmor;
import game.item.ItemBelt;
import game.item.ItemBlock;
import game.item.ItemStack;
import game.item.ItemTool;
import game.item.Items;
import game.util.Util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Player extends EntityLiving implements IInventory{

	private boolean flinching;
	private long flinchTimer = 0;

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

	/**Where 0 is Helmet, 1 is Chest, 2 is Extra and 3 is Weapon*/
	private ItemStack[] armorItems = new ItemStack[4];

	public ArmorInventory invArmor = new ArmorInventory();

	private boolean inWater;
	private boolean wasInwater;

	public Player(World world) {
		super(world, "player");

		width = 32;
		height = 32;

		entitySizeX = 24;
		entitySizeY = 30;

		moveSpeed = 0.5;
		maxSpeed = 3; 
		stopSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		facingRight = true;

		setPosition(5, 5);

		initHealth(3f);
	}

	@Override
	public boolean hasAnimation() {
		return true;
	}

	@Override
	public void hurtEntity(float f) {

		if(!flinching){
			super.hurtEntity(f);
			flinching = true;
		}
	}

	public void heal(float f){
		health+=f;

		if(health > getMaxHealth())
			health = getMaxHealth();

		if(health <=0)
			this.remove=true;
	}

	@Override
	public void draw(Graphics2D g) {

		if(flinchTimer % 5 == 0)
		{
			//Draw all parts here
			super.draw(g);
			super.draw(g, head);
			super.draw(g, arms);
			super.draw(g, legs);
			//armor parts
			if(armor_extra.hasFrames())
				super.draw(g, armor_extra);
		}

		if(invArmor.getWeapon() != null){
			
			BufferedImage weapon = invArmor.getWeapon().getItem().getTexture();

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
			//TODO implement a x tick delay ?
			if(KeyHandler.isHeldDown(KeyHandler.SPACE)){

				if(delay <= 0){
					setAttacking();
					delay = 20;
				}
				delay --;
			}
		}


		if(KeyHandler.isPressed(KeyHandler.INTERACT))
			for(MapObject o : getWorld().listWithMapObjects)
				if(o.intersects(this))
					o.interact(this);

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
							if(invArmor.getWeapon() == null){
								invArmor.setWeapon(getStackInSlot(keyPressed).copy());
								setStackInSlot(keyPressed, null);
							}else
								item.useItem(getStackInSlot(keyPressed), tileMap, getWorld(), this, keyPressed);
						}
						//any other item
						else
							item.useItem(getStackInSlot(keyPressed), tileMap, getWorld(), this, keyPressed);
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

		super.update();

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

				// entitySizeX += 5; obsolete. entities that are colliding are set somewhere else
				matchTool(obj);
				if(obj.getScreenXpos() > getScreenXpos() && facingRight)
					obj.onEntityHit(this);
				else if( obj.getScreenXpos() < getScreenXpos() && !facingRight)
					obj.onEntityHit(this);

				// entitySizeX -= 5;
			}


		}

		//remove weapon if broken after attacking
		if(invArmor.getWeapon() != null)
			if(invArmor.getWeapon().getDamage() < 0){
				invArmor.setWeapon(null);
				//TODO break sound and or animation	
			}

		if(flinching)
			flinchTimer++;
		if(flinchTimer > 50){
			flinchTimer = 0;
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
		if(inventory[slot] == null)
			inventory[slot] = stack.copy();
		else if (stack == null ){ //implies && inventory[slot] != null, because its an else statement
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
}
