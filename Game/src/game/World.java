package game;

import static base.main.GamePanel.HEIGHT;
import static base.main.GamePanel.WIDTH;
import game.content.SpawningLogic;
import game.content.save.DataList;
import game.content.save.DataTag;
import game.content.save.Save;
import game.entity.Entity;
import game.entity.MapObject;
import game.entity.block.BlockLight;
import game.entity.block.BlockOven;
import game.entity.block.Blocks;
import game.entity.living.EntityLiving;
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
import game.util.Util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import base.main.GameState;
import base.main.GameStateManager;
import base.main.keyhandler.KeyHandler;
import base.main.keyhandler.XboxController;
import base.tilemap.Background;
import base.tilemap.TileMap;

public class World extends GameState{

	private Player player;

	public TileMap tileMap;

	public boolean showBB;

	protected ArrayList<Background> backGrounds;

	private String worldPath = "/maps/cave_rand_1.map";

	public ArrayList<MapObject> listWithMapObjects ;

	public boolean isDisplayingGui;
	public Gui guiDisplaying;

	public int GameTime = 0;
	public int GameTimeTotalCycle = 18000*3;

	public float nightAlhpa = 0;

	private boolean messageSaved;
	private Font font = new Font("Constantia", Font.PLAIN, 36);
	private float messageAlpha = 1.0F;

	private Font defaultfont = new Font("Century Gothic", Font.PLAIN, 10);

	public boolean hasCreaturesSpawned;

	public BufferedImage bg = null;
	
	public World(GameStateManager gsm) {
		this.gsm = gsm;

		tileMap = new TileMap(32);

		loadWorld();

		player = new Player(tileMap, this);

		if(Save.getPlayerData() != null)
			player.readFromSave(Save.getPlayerData());

		backGrounds = new ArrayList<Background>();
		//		backGrounds.add(Images.instance.menuBackGround);

		bg = Util.generateStalactiteBackGround();
		
		listWithMapObjects = new ArrayList<MapObject>();

		displayGui(new GuiHud(this, player));

	}

	@Override
	public void draw(Graphics2D g){

		g.setColor(Color.gray);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setFont(defaultfont);
		
		g.drawImage(bg, 0, 0, null);

		if(backGrounds != null && !backGrounds.isEmpty())
			for(Background bg : backGrounds)
				bg.draw(g);

		tileMap.draw(g, player, 1);

		for(MapObject obj : listWithMapObjects){
			//do not draw entities outside of the player's range
			if(!isOutOfBounds(obj))
				obj.draw(g);
		}

		player.draw(g);

		tileMap.draw(g, player, 2);
		
		// Creates the buffered image. has to be recreated every time for transparancy
		BufferedImage lighting = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

		Graphics2D gbi = lighting.createGraphics();

		if((guiDisplaying instanceof GuiHud))
			if(isNightTime()){
				if(nightAlhpa < 0.97f)
					nightAlhpa +=0.0003f;
			}
			else{
				if(nightAlhpa > 0f)
					nightAlhpa -= 0.0005f;
			}

		gbi.setColor(new Color(0f, 0f, 0.07f, nightAlhpa));
		gbi.fillRect(0, 0, WIDTH, HEIGHT);

		Ellipse2D ellipse = null;

		//campfire cuts out circle here
		if(nightAlhpa > 0.1f){

			for(MapObject mo : listWithMapObjects){
				if(isOutOfBounds(mo))
					continue;

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
				}else if (mo instanceof BlockOven){
					BlockOven oven = (BlockOven)mo;
					if(!oven.isLit())
						continue;

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
		// Draws the buffered image.
		g.drawImage(lighting, 0,0, null);

		if(isDisplayingGui && guiDisplaying != null){
			guiDisplaying.draw(g);
		}

		if(isConsoleDisplayed){
			g.setColor(new Color(0f,0f,0f,0.5f));
			g.fillRect(10, 10, 250, 25);
			g.setColor(Color.white);
			g.drawString(consolePrint, 25,25);
		}

		if(messageSaved)
		{	
			//set the opacity
			g.setFont(font);
			g.setColor(new Color(1f, 1f, 1f, messageAlpha));
			g.drawString("Successfully saved.", WIDTH / 2 - (WIDTH / 4), HEIGHT / 2);
			messageAlpha -= 0.01f;

			//increase the opacity and repaint
			if (messageAlpha <= 0.0F)
				messageAlpha = 0.0F;

			if(messageAlpha == 0.0F){
				messageSaved = false;
				messageAlpha = 1.0f;
			}
		}
	}

	public boolean isNightTime(){
		return GameTime > 36000;
	}

	public int getGameTime(){
		return GameTime;
	}

	@Override
	public void update(){

		if(backGrounds != null && !backGrounds.isEmpty())
			for(Background bg : backGrounds)
				bg.update();

		handleInput();

		tileMap.setPosition((WIDTH / 2) - player.getScreenXpos(),(HEIGHT / 2) - player.getScreenYpos());

		if((guiDisplaying instanceof GuiHud)){
			GameTime++;

			if(GameTime >= 54000){
				GameTime = 0;
			}

			if(hasCreaturesSpawned)
				if(!isNightTime())
					hasCreaturesSpawned = false;

			SpawningLogic.spawnNightCreatures(this, false);

			player.update();

			for(MapObject obj : listWithMapObjects){

				obj.update();

				if(player.getRectangle().intersects(obj.getRectangle())){
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
						if(!((EntityLiving) obj).canPlayDeathAnimation())
							break;
						EntityDeathAnim anim = new EntityDeathAnim(tileMap, this);
						anim.setPosition(obj.getScreenXpos(), obj.getScreenYpos());
						listWithMapObjects.add(anim);
					}
					break;
				}
			}
		}

		//				System.out.println(player.getCollidingBlocks());
	}

	private void loadWorld() {
		tileMap.loadTiles(getTileTexture());
		tileMap.loadMap(worldPath);
		tileMap.setPosition(0, 0);
	}

	public void reloadMap(String s){
		tileMap.loadMap(s);
		worldPath = s;
	}

	public String getTileTexture(){
		return "/maps/platformer_tiles.png";
	}

	public void handleInput() {

		if(KeyHandler.isPressed(KeyHandler.CTRL))
		{


			//			float x = WIDTH * SCALE;
			//			float y = HEIGHT * SCALE;
			//
			//			SCALEDX = (int)x;
			//			SCALEDY = (int)y;
			//
			//			Container c = this.gsm.jframeInstance.getTopLevelAncestor();
			//			c.setPreferredSize(new Dimension(SCALEDX, SCALEDY + 30));
			//			if(c instanceof JFrame){
			//				((JFrame)c).pack();
			//				((JFrame)c).setExtendedState(JFrame.MAXIMIZED_BOTH); 

			//				((JFrame)c).setVisible(true);
			//				((JFrame)c).setLocationRelativeTo(null);
			//			}

			//			this.gsm.jframeInstance.setPreferredSize(new Dimension(500,300));
			//			this.gsm.jframeInstance.setFocusable(true);
			//			this.gsm.jframeInstance.requestFocus();
		}

		if(isConsoleDisplayed){
			consoleInput();
			return;
		}

		if(KeyHandler.isPressed(KeyHandler.QUICKSAVE)){
			Save.writePlayerData(getPlayer());
			Save.writeWorld(this, Loading.index);
			Save.writeRandomParts();
			messageSaved = true;
		}

		if(KeyHandler.isPressed(KeyHandler.SHIFT) && KeyHandler.prevKeyState[KeyHandler.CTRL]){
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

		if (KeyHandler.isPressed(KeyHandler.B)){
			showBB = showBB ? false :true;
		}

	}

	public Player getPlayer(){
		return player;
	}

	public void writeToSave(DataTag tag){

		tag.writeString("map", worldPath);

		tag.writeInt("gametime", GameTime);
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

	public void readFromSave(DataTag tag){

		reloadMap(tag.readString("map"));
		GameTime = tag.readInt("gametime");
		nightAlhpa = tag.readFloat("nightshade");

		hasCreaturesSpawned = tag.readBoolean("creatureFlag");

		DataList list = tag.readList("content");

		for(int i = 0; i < list.data().size(); i ++){
			DataTag dt = list.readArray(i);
			String uin = dt.readString("UIN");

			MapObject mo = Blocks.loadMapObjectFromString(uin, tileMap, this);

			if(mo == null)
				mo = Entity.createEntityFromUIN(uin, tileMap, this);
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
		int Px = player.getScreenXpos();
		int Py = player.getScreenYpos();

		int arroundX = 32*20; //TODO make renderDistance Configurable
		int arroundY = 32*12;

		int xDistanceMin = Px-arroundX;
		int xDistanceMax = Px+arroundX;

		int yDistanceMin = Py-arroundY;
		int yDistanceMax = Py+arroundY;

		if(obj.getScreenXpos() >= xDistanceMin && obj.getScreenXpos() < xDistanceMax)
			if(obj.getScreenYpos() >= yDistanceMin && obj.getScreenYpos() < yDistanceMax)
				return false;
		return true;
	}

	private boolean isConsoleDisplayed = false;
	private String consolePrint = "";

	private void displayConsole(){
		isConsoleDisplayed = true;
	}

	private void consoleInput(){
		if(KeyHandler.isPressed(KeyHandler.ESCAPE))
			isConsoleDisplayed = false;
		else
			consolePrint = KeyHandler.keyPressed(KeyHandler.ANYKEY, consolePrint);

		if(KeyHandler.isPressed(KeyHandler.ENTER))
			consoleCommands(consolePrint);
	}

	private void consoleCommands(String cmd){
		if(cmd.equals("night"))
			GameTime = 36000;

		else if(cmd.equals("day"))
			GameTime = 0;

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
				EntityLiving entity = Entity.createEntityFromUIN(split[1], tileMap, this);
				if(entity != null){
					entity.setPosition(player.getScreenXpos(), player.getScreenYpos());
					listWithMapObjects.add(entity);
				}
			}
			else if(split.length == 3){
				int loop = Integer.parseInt(split[2]);
				for(int i = 0; i < loop; i ++){
					EntityLiving entity = Entity.createEntityFromUIN(split[1], tileMap, this);
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
					GameTime = timeSet;
				} catch (Exception e) {
					//					e.printStackTrace();
					System.out.println("[ERROR] " + split[1] + " is not a valid number !");
				}
			}else{
				System.out.println("[INFO" + " current time = " + GameTime);
			}
		}
		consolePrint = "";
		isConsoleDisplayed = false;
	}
}
