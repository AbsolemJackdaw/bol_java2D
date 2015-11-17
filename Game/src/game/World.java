package game;

import static engine.window.GamePanel.HEIGHT;
import static engine.window.GamePanel.WIDTH;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import engine.game.GameWorld;
import engine.game.MapObject;
import engine.gamestate.GameStateManagerBase;
import engine.imaging.Background;
import engine.keyhandlers.KeyHandler;
import engine.keyhandlers.XboxController;
import engine.map.TileMap;
import engine.music.Music;
import engine.save.DataList;
import engine.save.DataTag;
import engine.window.GamePanel;
import engine.window.gameAid.Time;
import engine.window.gameAid.Utility;
import engine.window.gameAid.Window;
import game.content.Loading;
import game.content.SpawningLogic;
import game.content.WorldTask;
import game.content.save.Save;
import game.entity.Entity;
import game.entity.EntityLiving;
import game.entity.block.Blocks;
import game.entity.block.breakable.BlockBreakable;
import game.entity.block.breakable.BlockLight;
import game.entity.block.breakable.BlockOven;
import game.entity.living.environement.EntityDeathAnim;
import game.entity.living.player.Player;
import game.gui.Gui;
import game.gui.GuiHud;
import game.gui.GuiPause;
import game.gui.GuiPlayerInventory;
import game.item.Item;
import game.item.ItemLantern;
import game.item.ItemStack;
import game.item.Items;
import game.util.Constants;
import game.util.Util;


public class World extends GameWorld{

	protected Player player;

	public boolean isDisplayingGui;
	public Gui guiDisplaying;

	public Time gametime = new Time(18000, 2, 1);

	protected ArrayList<Background> backGrounds;

	public float nightAlhpa = 0;

	private boolean displaySaveMessage;
	private float displayMessageAlpha = 1.0F;

	public boolean hasCreaturesSpawned;

	public ArrayList<WorldTask> tasks = new ArrayList<WorldTask>();

	public World(GameStateManagerBase gsm){
		super(gsm);

		tileMap = new TileMap(32);

		listWithMapObjects = new ArrayList<MapObject>();

	}

	/**
	 * Method has to be called after world instance has been called 
	 * and save data has been read;
	 */
	public void init(){

		super.init();

		this.player = new Player(this);

		//init if no saves are found, and the player wants to play the tutorial
		if(Save.getWorldData(0) == null)
			if(resourceMapPath.equals("/maps/tutorial_island")){
				Loading.loadTutorialLevel(gsm);
			}else{
				Loading.loadFirstLevel(gsm);
			}

		if(Save.getPlayerData() != null)
			player.readFromSave(Save.getPlayerData());

		displayGui(new GuiHud(this, player));

		int mapHeight = (int)(tileMap.getYRows() * 32);

		backGrounds = new ArrayList<Background>();

		backGrounds.add(new Background(Util.generateStalactiteBackGround(mapHeight, 30), 2, 9, false, 10));
		backGrounds.add(new Background(Util.generateStalactiteBackGround(mapHeight, 20), 5, 9, false, 10));
		backGrounds.add(new Background(Util.generateStalactiteBackGround(mapHeight, 0), 9, 9, false, 10));
	}

	BufferedImage lighting = null;

	Graphics2D gbi = null; 

	@Override
	public void draw(Graphics2D g){

		g.setColor(Color.gray.darker());
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setFont(Constants.FONT_ITEMS);

		//draw backgrounds first. all other pictures will be drawn in front of it
		if(backGrounds != null && !backGrounds.isEmpty())
			for(Background bg : backGrounds)
				bg.draw(g);

		//draw the tile map
		tileMap.draw(g, 
				(int)player.getScreenXpos()/32, 
				(int)player.getScreenYpos()/32,  1);

		//set night shade if it's night
		if(nightAlhpa > 0.1f){
			lighting = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
			gbi = lighting.createGraphics();

			gbi.setColor(new Color(0f, 0f, 0.07f, nightAlhpa));
			gbi.fillRect(0, 0, WIDTH, HEIGHT);
		}

		//Draw all objects/entities in the map here
		for(MapObject obj : listWithMapObjects){
			//do not draw entities outside of the player's range
			if(isOutOfBounds(obj))
				continue;

			obj.draw(g);

			if(nightAlhpa > 0.1f && gbi != null)
				cutoutLight(obj, gbi);
		}

		//draw the player. draw it before the night shade !
		player.draw(g);

		tileMap.draw(g, (int)player.getScreenXpos()/32, (int)player.getScreenYpos()/32,  2);

		//draw night shade after all visible in the world
		if(nightAlhpa > 0.1f)
			g.drawImage(lighting, 0,0, null);

		//draw gui if any
		if(isDisplayingGui && guiDisplaying != null){
			guiDisplaying.draw(g);
		}

		//draw improvized console window
		if(isConsoleDisplayed){
			g.setColor(new Color(0f,0f,0f,0.5f));
			g.fillRect(10, 10, 250, 25);
			g.setColor(Color.white);
			g.drawString(consolePrint, 25,25);
		}

		if(!isConsoleDisplayed && guiDisplaying instanceof GuiHud){
			g.setFont(Constants.FONT_ITEMS);
			int i = 0;
			for(WorldTask task : tasks){

				if(!task.isAchieved())
					Utility.drawStringWithShadow(task.toString(), 10, 10+i, g, Color.red.darker(), Color.black);
				else
					Utility.drawStringWithShadow(task.toString(), 10, 10+i, g, Color.green.darker().darker(), Color.black);

				i+=12;
			}

		}

		if(displaySaveMessage){	
			//set the opacity
			g.setFont(Constants.FONT_HEADER);
			g.setColor(new Color(1f, 1f, 1f, displayMessageAlpha));
			g.drawString("Successfully saved.", WIDTH / 2 - (WIDTH / 4), HEIGHT / 2);
			displayMessageAlpha -= 0.01f;

			//increase the opacity and repaint
			if (displayMessageAlpha <= 0.0F)
				displayMessageAlpha = 0.0F;

			if(displayMessageAlpha == 0.0F){
				displaySaveMessage = false;
				displayMessageAlpha = 1.0f;
			}
		}
	}

	/**returns true if the currentime is bigger then the registered getDawn() time in the time handler*/
	public boolean isNightTime(){
		return gametime.getCurrentTime() > gametime.getDawn();
	}

	double offset = 0;
	boolean shake;
	double shakeTimer = 50;
	double shakeTimerMax = 50;

	@Override
	public void update(){

		double randOff = 0;

		if(offset > 0){
			if(Constants.RANDOM.nextInt(3) == 0){
				randOff = offset;

				if(Constants.RANDOM.nextInt(2)==0){
					randOff /= -1;
				}
			}
		}

		//move tilemap around 
		tileMap.setPosition(((WIDTH / 2) - player.getScreenXpos()) + offset, ((HEIGHT / 2) - player.getScreenYpos()+randOff));

		//update backgrounds to move them around
		if(backGrounds != null && !backGrounds.isEmpty())
			for(Background bg : backGrounds){

				if (!bg.isStatic)
					bg.setPosition(tileMap.getx()/bg.speed , tileMap.gety()/bg.speed);
			}

		//process key input
		handleInput();

		//update game logics only if displaying gui is HUD, or the gui doesnt pause the game aka, in world
		if(guiDisplaying instanceof GuiHud || guiDisplaying != null && !guiDisplaying.pausesGame()){

			if(shake){

				offset = (-0.5d * Math.sin((0.5d* Math.PI*(shakeTimer/4d))))*100;

				shakeTimer--;

			}

			if(shakeTimer <= 0){
				shake = false;
				shakeTimer = shakeTimerMax;
				offset = 0;
			}

			//TODO game time re implement
			//gametime.updateTime();

			if(hasCreaturesSpawned)
				if(!isNightTime())
					hasCreaturesSpawned = false;

			SpawningLogic.spawnNightCreatures(this, false);

			if(isNightTime()){
				if(nightAlhpa < 0.97f)
					nightAlhpa +=0.0003f;
			}
			else{
				if(nightAlhpa > 0f)
					nightAlhpa -= 0.0005f;
			}

			player.update();

			for(MapObject obj : listWithMapObjects){

				if(!isOutOfBounds(obj) || obj.persistantUpdate()){
					obj.update();

					Rectangle playerRectangleExtra = new Rectangle(player.getRectangle().x, player.getRectangle().y, player.getRectangle().width, player.getRectangle().height);

					if(obj instanceof BlockBreakable) // extend bounding box for breakable blocks so you can stand slightly further away
						//i just like to add this. i forgot the point ...
						playerRectangleExtra = new Rectangle(player.getRectangle().x - 10, player.getRectangle().y, player.getRectangle().width + 20, player.getRectangle().height);

					if(playerRectangleExtra.intersects(obj.getRectangle())){
						if(!player.getCollidingMapObjects().contains(obj)){
							player.setCollidingMapObjects(obj);
							player.isCollidingWithBlock = true;
						}

					}else{
						player.getCollidingMapObjects().remove(obj);
						if(player.getCollidingMapObjects().isEmpty())
							player.isCollidingWithBlock = false;
					}

					if(obj.remove){

						if(player.getCollidingMapObjects().contains(obj))
							player.getCollidingMapObjects().remove(obj);

						listWithMapObjects.remove(obj);

						if(obj instanceof EntityLiving){
							if(((EntityLiving) obj).canPlayDeathAnimation()){
								EntityDeathAnim anim = obj.getDeathAnimation();
								anim.setPosition(obj.getScreenXpos(), obj.getScreenYpos());
								listWithMapObjects.add(anim);
							}
						}
						break;
					}
				}
			}
		}
	}

	public void handleInput() {

		if(KeyHandler.isPressed(KeyHandler.CTRL)){
			//			player.initHealth(15f);
		}

		if(isConsoleDisplayed){
			consoleInput();
			return;
		}

		if(KeyHandler.isPressed(KeyHandler.QUICKSAVE)){
			Save.writePlayerData(player);
			Save.writeWorld(this, Loading.index);
			Save.writeRandomParts();
			displaySaveMessage = true;
		}

		if(KeyHandler.prevKeyState[KeyHandler.CTRL] && KeyHandler.isPressed(KeyHandler.SHIFT)){
			displayConsole();
			return;
		}


		//returns when gui's are closed are to prevent player from jumping
		//right after you press the jump/validation key to select a close option
		if(KeyHandler.isPressed(KeyHandler.ESCAPE) && guiDisplaying instanceof GuiHud && !(guiDisplaying instanceof GuiPause)){
			displayGui(new GuiPause(this, player));
		}else if (guiDisplaying instanceof GuiPause && KeyHandler.isPressed(KeyHandler.ESCAPE) || guiDisplaying instanceof GuiPause && KeyHandler.isPressed(KeyHandler.ESCAPE2) && XboxController.controller != null){
			displayGui(null);
			return;
		}

		if(KeyHandler.isPressed(KeyHandler.INVENTORY) && guiDisplaying instanceof GuiHud && !(guiDisplaying instanceof GuiPlayerInventory)){
			displayGui(new GuiPlayerInventory(this, player));
		}else if (guiDisplaying instanceof GuiPlayerInventory && KeyHandler.isPressed(KeyHandler.INVENTORY) ){
			displayGui(null);
			return;
		}

		if (player.getHealth() == 0)
			return;

		if(guiDisplaying != null && guiDisplaying.pausesGame()){
			guiDisplaying.handleGuiKeyInput();
			return;
		}

		player.handleInput();

		if (KeyHandler.isPressed(KeyHandler.B))
			showBoundingBoxes = showBoundingBoxes ? false :true;
	}

	@Override
	public void writeToSave(DataTag tag){
		super.writeToSave(tag);

		tag.writeInt("gametime", gametime.getCurrentTime());
		tag.writeFloat("nightshade", new Float(nightAlhpa));

		tag.writeBoolean("creatureFlag", hasCreaturesSpawned);

		DataList list = new DataList();
		for(MapObject mo : listWithMapObjects){
			DataTag dt = new DataTag();
			mo.writeToSave(dt);
			list.write(dt);
		}

		tag.writeList("content", list);
	}

	@Override
	public void readFromSave(DataTag tag){
		super.readFromSave(tag);

		gametime.writeCurrentGameTime(tag.readInt("gametime"));
		nightAlhpa = tag.readFloat("nightshade");

		hasCreaturesSpawned = tag.readBoolean("creatureFlag");

		DataList list = tag.readList("content");

		for(int i = 0; i < list.data().size(); i ++){
			DataTag dt = list.readArray(i);
			String uin = dt.readString("UIN");

			MapObject mo = Blocks.loadMapObjectFromString(uin, this);

			if(mo == null)
				mo = Entity.createEntityFromUIN(uin, this);
			if(mo != null){
				mo.readFromSave(dt);
				listWithMapObjects.add(mo);
			}else{
				System.out.println("The Entity for " + uin + " was not recognized. Skipped loading this entity");
			}
		}
	}

	public void displayGui(Gui gui){
		if(gui != null){
			isDisplayingGui = true;
			guiDisplaying = gui;
		}
		else{
			isDisplayingGui = false;
			guiDisplaying = null;
			displayGui(new GuiHud(this, player));
		}
	}

	private boolean isOutOfBounds(MapObject obj){
		int Px = (int)player.getScreenXpos();
		int Py = (int)player.getScreenYpos();

		int screenX = Window.getWidth()/(32*(int)GamePanel.SCALE);
		int screenY = Window.getHeight()/(32*(int)GamePanel.SCALE);

		int arroundX = 32* (screenX + 2) ; // +2 is error margin
		int arroundY = 32* (screenY + 2);

		int xDistanceMin = Px-arroundX;
		int xDistanceMax = Px+arroundX;

		int yDistanceMin = Py-arroundY;
		int yDistanceMax = Py+arroundY;

		if(obj.getScreenXpos() >= xDistanceMin && obj.getScreenXpos() < xDistanceMax)
			if(obj.getScreenYpos() >= yDistanceMin && obj.getScreenYpos() < yDistanceMax)
				return false;

		return true;
	}

	private void displayConsole(){
		isConsoleDisplayed = true;
	}

	private void consoleInput(){
		if(KeyHandler.isPressed(KeyHandler.ESCAPE))
			isConsoleDisplayed = false;
		else
			consolePrint = KeyHandler.getKeyString(KeyHandler.ANYKEY, consolePrint);

		if(KeyHandler.isPressed(KeyHandler.ENTER))
			consoleCommands(consolePrint);
	}

	private void consoleCommands(String cmd){

		if(cmd.equals("upupandaway")){
			player.flyCheat = !player.flyCheat;
		}
		else if(cmd.equals("mute")){
			Music.toggleMute();
		}

		else if(cmd.equals("kill")){
			for(MapObject mo : listWithMapObjects){
				if(mo instanceof EntityLiving)
					mo.remove = true;
			}
		}
		else if(cmd.startsWith("hurt")){
			String [] split = cmd.split("\\s+");
			if(split.length == 2)
				player.hurtEntity(Float.valueOf(split[1]), null);
		}

		else if(cmd.equals("heal")){
			player.heal(1f);
		}

		else if(cmd.equals("night"))
			gametime.writeCurrentGameTime(gametime.getDawn());

		else if(cmd.equals("day"))
			gametime.writeCurrentGameTime(0);

		else if(cmd.startsWith("need")){
			String [] split = cmd.split("\\s+") ;
			if(split.length == 3){
				Item item = Items.getItemFromUIN(split[1]);
				if(item != null){
					ItemStack stack = new ItemStack(item, Integer.valueOf(split[2]), item.getItemDamage());
					player.setStackInNextAvailableSlot(stack);
				}
			}
		}else if(cmd.startsWith("spawn")){
			String [] split = cmd.split("\\s+") ;
			if(split.length == 2){
				EntityLiving entity = Entity.createEntityFromUIN(split[1], this);
				if(entity != null){
					entity.setPosition(player.getScreenXpos(), player.getScreenYpos());
					listWithMapObjects.add(entity);
				}
			}
			else if(split.length == 3){
				int loop = Integer.parseInt(split[2]);
				for(int i = 0; i < loop; i ++){
					EntityLiving entity = Entity.createEntityFromUIN(split[1], this);
					if(entity != null){
						entity.setPosition(player.getScreenXpos(), player.getScreenYpos());
						listWithMapObjects.add(entity);
					}
				}
			}
		}else if(cmd.startsWith("time")){
			String [] split = cmd.split("\\s+") ;
			if(split.length == 2){
				try {
					int timeSet = Integer.valueOf(split[1]);
					gametime.writeCurrentGameTime(timeSet);
				} catch (Exception e) {
					System.out.println("[ERROR] " + split[1] + " is not a valid number !");
				}
			}else{
				System.out.println("[INFO" + " current time = " + gametime.getCurrentTime());
			}
		}

		consolePrint = "";
		isConsoleDisplayed = false;
	}

	/**cuts out an ellipse in the night shade /lighting bufferedimage drawn over the screen at night*/
	private void cutoutLight(MapObject mo, Graphics2D gbi){

		Ellipse2D ellipse = null;

		if(mo instanceof BlockLight){

			BlockLight light = (BlockLight)mo;

			int i = 3;

			int x =(light.posX() + i * 2) - (light.getRadius()/2 - 16);
			int y = (light.posY() + i * 2) - (light.getRadius()/2 - 16);
			int h = light.getRadius() - i * 4;
			int w = light.getRadius() - i * 4;

			float scale = 15f;
			float f =  0f + (float)i / 10f;

			for(float i1 = 0; i1 <5; i1++){
				gbi.setColor(new Color(0f, 0f, 0f, f));    
				gbi.setComposite(AlphaComposite.DstOut);
				ellipse = new Ellipse2D.Double(x+(i1*(scale/2f)),y+(i1*(scale/2f)),h-(i1*scale),w-(i1*scale));
				gbi.fill(ellipse);
			}
		}

		else if (mo instanceof BlockOven){
			BlockOven oven = (BlockOven)mo;

			if(oven.isLit()){

				for(int i = 0; i < 2; i++){
					float f =  0f + (float)i / 10f;
					gbi.setColor(new Color(0.0f, 0.0f, 0.0f, f));    
					gbi.setComposite(AlphaComposite.DstOut);
					gbi.fill(new Ellipse2D.Double((oven.posX() + i * 5) - (oven.getRadius()/2 - 32/2), (oven.posY() + i * 5) - (oven.getRadius()/2 - 32/2), oven.getRadius() - i * 10, oven.getRadius() - i *10));
				}

				int i = 2;

				int x =(oven.posX() + i * 5) - (oven.getRadius()/2 - 32/2);
				int y = (oven.posY() + i * 5) - (oven.getRadius()/2 - 32/2);
				int h = oven.getRadius() - i * 10;
				int w = oven.getRadius() - i *10;

				float scale = 15f;
				float f =  0f + (float)i / 10f;

				for(float i1 = 0; i1 <2; i1++){
					gbi.setColor(new Color(0f, 0f, 0f, f));    
					gbi.setComposite(AlphaComposite.DstOut);
					ellipse = new Ellipse2D.Double(x+(i1*(scale/2f)),y+(i1*(scale/2f)),h-(i1*scale),w-(i1*scale));
					gbi.fill(ellipse);
				}
			}
		}

		//cut torch light out dynamicly
		for(ItemStack stack : player.getItems())
			if(stack != null)
				if(stack.getItem().equals(Items.lantern)){

					ItemLantern lant = (ItemLantern) stack.getItem();
					if(!lant.isLit())
						break;

					int i =5;

					float f =  0f + (float)i / 10f;

					int x =(player.posX() + i * 5) - (player.getRadius()/2 - 32/2);
					int y = (player.posY() + i * 5) - (player.getRadius()/2 - 32/2);
					int h = player.getRadius() - i * 10;
					int w = player.getRadius() - i *10;

					float scale = 15f;
					for(float i1 = 0; i1 <5; i1++){
						gbi.setColor(new Color(0f, 0f, 0f, f));    
						gbi.setComposite(AlphaComposite.DstOut);
						ellipse = new Ellipse2D.Double(x+(i1*(scale/2f)),y+(i1*(scale/2f)),h-(i1*scale),w-(i1*scale));
						gbi.fill(ellipse);
					}
				}
	}

	public Player getPlayer(){
		return player;
	}

	public void shakeWorld(){
		shake = true;
	}
}
