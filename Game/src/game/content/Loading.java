package game.content;

import engine.gamestate.GameStateManagerBase;
import engine.keyhandlers.KeyHandler;
import engine.map.Tile;
import engine.map.TileMap;
import engine.save.DataTag;
import game.GameStateManager;
import game.World;
import game.content.WorldTask.EnumTask;
import game.content.save.Save;
import game.entity.Entity;
import game.entity.EntityLiving;
import game.entity.block.Blocks;
import game.entity.block.breakable.BlockBreakable;
import game.entity.block.breakable.BlockRock;
import game.entity.block.breakable.BlockWood;
import game.entity.block.environement.BlockInfoPane;
import game.entity.living.player.Player;
import game.item.Items;
import game.util.Constants;
import game.util.Util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.SwingWorker;


public class Loading {

	public static BufferedImage[] stalactites = new BufferedImage[5];

	//both ints are saved in World
	/**always increases. tracks the number of unlocked maps*/
	public static int maps = 1;

	/**increases and decreases. tracks the player's current map*/
	public static int index = 0;

	//	/**set to false once the tutorial has been skipped or played*/
	//	public static boolean tutorial = true;

	/**
	 *Provides a random new map between map #2 and #max_total of maps 
	 */
	public static String newMap(){

		//skip map x_1, so that map is used only in the very beginning
		// x1 + x2 is max_total maps, where map min is 2
		int i = Constants.RANDOM.nextInt(2)+1;
		String s = "/maps/map_" + i;
		System.out.println(s);
		return s;
	}

	public static void gotoNextLevel(final GameStateManagerBase gsm){

		Util.startLoadIcon();

		new SwingWorker<Void, Integer>(){
			@Override
			protected Void doInBackground() throws Exception {

				//save world we are currently in
				World currentWorld = (World)gsm.getGameState(gsm.getCurrentState());
				Save.writeWorld(currentWorld, index);
				Save.writePlayerData(currentWorld.getPlayer());

				//get gametime to transfer to the new world and continue counting
				int time = currentWorld.gametime.getCurrentTime();
				float nightShade = currentWorld.nightAlhpa;

				//set a new world
				gsm.setState(GameStateManager.GAME);

				//increase index to indicate the new world's index
				index++;
				World newWorld = (World)gsm.getGameState(gsm.getCurrentState());

				//if its a new map
				if(index == maps){

					String s = newMap();
					newWorld.loadMap(s);
					newWorld.init();
					maps++;

					populateWorld(newWorld);

					//set gametime to continue counting
					newWorld.gametime.writeCurrentGameTime(time);
					newWorld.nightAlhpa = nightShade;

					if(newWorld.isNightTime()){
						SpawningLogic.spawnNightCreatures(newWorld, true);
					}

				}else{
					newWorld.readFromSave(Save.getWorldData(index));
					//set gametime to continue counting
					newWorld.gametime.writeCurrentGameTime(time);
					newWorld.nightAlhpa = nightShade;
					newWorld.init();
				}

				Save.writeRandomParts();

				for(int x = 0; x < newWorld.tileMap.getXRows(); x++)
					for(int y = 0; y < newWorld.tileMap.getYRows(); y++){
						if(newWorld.tileMap.getBlockID(x, y) == 7)
							newWorld.getPlayer().setPosition(x+1, y);
					}

				//save the new world as well > this prevents bugs/glitches if closed without saving !
				Save.writeWorld(newWorld, index);
				Save.writePlayerData(newWorld.getPlayer());

				return null;
			}
			@Override
			protected void done() {
				super.done();
				Util.stopLoadIcon();
			}
		}.execute();
	}

	public static void gotoPreviousLevel(final GameStateManagerBase gsm){

		Util.startLoadIcon();

		new SwingWorker<Void, Integer>() {
			@Override
			protected Void doInBackground() throws Exception {

				World currentWorld = (World)gsm.getGameState(gsm.getCurrentState());

				if(index == 0 || index == 1 && Save.getWorldData(0)!= null && Save.getWorldData(0).readString("map").equals("/maps/tutorial_island")){
					currentWorld.getPlayer().setVector(4, 0);
					return null;
				}

				//save world we are currently in
				Save.writeWorld(currentWorld, index);
				Save.writePlayerData(currentWorld.getPlayer());

				//get gametime to transfer to the new world and continue counting
				int time = currentWorld.gametime.getCurrentTime();
				float nightShade = currentWorld.nightAlhpa;
				//set a new world
				gsm.setState(GameStateManager.GAME);

				//increase index to indicate the new world's index
				index--;
				World newWorld = (World)gsm.getGameState(gsm.getCurrentState());

				newWorld.readFromSave(Save.getWorldData(index));

				newWorld.init();

				//set gametime to continue counting
				newWorld.gametime.writeCurrentGameTime(time);
				newWorld.nightAlhpa = nightShade;

				if(!newWorld.hasCreaturesSpawned){
					SpawningLogic.spawnNightCreatures(newWorld, true);
				}

				for(int i = 0; i < newWorld.tileMap.getXRows(); i++)
					for(int j = 0; j < newWorld.tileMap.getYRows(); j++){
						if(newWorld.tileMap.getBlockID(i, j) == 6){
							newWorld.getPlayer().setPosition(i-1, j);
							newWorld.getPlayer().facingRight = false;
							break;
						}
					}
				Save.writeRandomParts();

				//save world we went to, this prevents bugs/glitches if closed without saving !
				Save.writeWorld(newWorld, index);
				Save.writePlayerData(newWorld.getPlayer());

				return null;
			}
			
			@Override
			protected void done() {
				super.done();
				Util.stopLoadIcon();
			}
			
		}.execute();
	}

	/**loads a level to skip the tutorial.*/
	public static void loadFirstLevel(final GameStateManagerBase gsm){

		Util.startLoadIcon();

		new SwingWorker<Void, Integer>(){
			@Override
			protected Void doInBackground() throws Exception {

				World world = (World)gsm.getGameState(gsm.getCurrentState());
				Player player = world.getPlayer();

				for(int x = 0; x < world.tileMap.getXRows(); x++)
					for(int y = 0; y < world.tileMap.getYRows(); y++){
						if(world.tileMap.getBlockID(x, y) == 7)
							player.setPosition(x+1, y);
					}

				populateWorld(world);

				return null;
			}

			@Override
			protected void done() {
				super.done();
				Util.stopLoadIcon();
			}
		}.execute();

	}

	/**load tutorial level if no saves are found, and the player chooses to play the tutorial*/
	public static void loadTutorialLevel(final GameStateManagerBase gsm){

		Util.startLoadIcon();

		new SwingWorker<Void, Integer>(){
			@Override
			protected Void doInBackground() throws Exception {

				World world = (World)gsm.getGameState(gsm.getCurrentState());

				Player player = world.getPlayer();
				player.setPosition(66, 63);

				world.tasks.add(new WorldTask(WorldTask.SWIM, 1, EnumTask.ACTION));
				world.tasks.add(new WorldTask(Items.woodChip.getDisplayName(), 10, EnumTask.COLLECTIBLE));
				world.tasks.add(new WorldTask(Items.rock.getDisplayName(), 5, EnumTask.COLLECTIBLE));
				world.tasks.add(new WorldTask(Items.craftTable.getDisplayName(), 1, EnumTask.CRAFT));

				BlockInfoPane pane = null;
				ArrayList<String> text = null;

				pane = new BlockInfoPane(world, Blocks.SIGN);
				text = new ArrayList<String>();
				text.add("WELCOME!");
				text.add("to Tutorial Island.");
				text.add(KeyHandler.getKeyName(KeyHandler.UP).toLowerCase() + KeyHandler.getKeyName(KeyHandler.LEFT).toLowerCase() +
						KeyHandler.getKeyName(KeyHandler.DOWN).toLowerCase() +KeyHandler.getKeyName(KeyHandler.RIGHT).toLowerCase() +
						" to move.");
				pane.setText(text);
				pane.setPosition(66,63);
				world.listWithMapObjects.add(pane);

				pane = new BlockInfoPane(world, Blocks.SIGN);
				text = new ArrayList<String>();
				text.add("Or arrow Keys, whatever...");
				pane.setText(text);
				pane.setPosition(69,63);
				world.listWithMapObjects.add(pane);

				pane = new BlockInfoPane(world, Blocks.SIGN);
				text = new ArrayList<String>();
				text.add(KeyHandler.getKeyName(KeyHandler.UP).toLowerCase() + " to jump up ! ^" );
				pane.setText(text);
				pane.setPosition(74,63);
				world.listWithMapObjects.add(pane);

				pane = new BlockInfoPane(world, Blocks.SIGN);
				text = new ArrayList<String>();
				text.add("Lets take a dip in the" );
				text.add("strawberry river !");
				pane.setText(text);
				pane.setPosition(78,63);
				world.listWithMapObjects.add(pane);

				pane = new BlockInfoPane(world, Blocks.SIGN);
				text = new ArrayList<String>();
				text.add("I lied..." );
				text.add("This is just a pond of water.");
				text.add("Lets get to the other side !");
				pane.setText(text);
				pane.setPosition(81,63);
				world.listWithMapObjects.add(pane);

				pane = new BlockInfoPane(world, Blocks.SIGN);
				text = new ArrayList<String>();
				text.add("SPLISH SPLASH," );
				text.add("YOU ARE TAKING A BATH !");
				pane.setText(text);
				pane.setPosition(104,64);
				world.listWithMapObjects.add(pane);

				pane = new BlockInfoPane(world, Blocks.SIGN);
				text = new ArrayList<String>();
				text.add("Your adventure is about to start..." );
				text.add("Just keep hopping up !");
				pane.setText(text);
				pane.setPosition(116, 59);
				world.listWithMapObjects.add(pane);

				pane = new BlockInfoPane(world, Blocks.SIGN);
				text = new ArrayList<String>();
				text.add("Every level has goals," );
				text.add("they are drawn in the top left corner.");
				text.add("Completete all of them to go on ! ");
				pane.setText(text);
				pane.setPosition(83, 48);
				world.listWithMapObjects.add(pane);

				pane = new BlockInfoPane(world, Blocks.SIGN);
				text = new ArrayList<String>();
				text.add("Have you tried making a work desk ?");
				text.add("Make some sticks in your inventory ! ("+ KeyHandler.getKeyName(KeyHandler.INVENTORY)+")");
				text.add("Place down the desk with the number in the hotbar.");
				pane.setText(text);
				pane.setPosition(102, 22);
				world.listWithMapObjects.add(pane);

				pane = new BlockInfoPane(world, Blocks.SIGN);
				text = new ArrayList<String>();
				text.add("Try fiddling with the numbers while");
				text.add("in the inventory to move around items.");
				text.add("You're ready for adventure... Have fun !");
				pane.setText(text);
				pane.setPosition(115, 22);
				world.listWithMapObjects.add(pane);

				BlockRock rock ;

				rock = new BlockRock(world);
				rock.setPosition(67, 48);
				world.listWithMapObjects.add(rock);

				rock = new BlockRock(world);
				rock.setPosition(73, 28);
				world.listWithMapObjects.add(rock);

				rock = new BlockRock(world);
				rock.setPosition(85, 39);
				world.listWithMapObjects.add(rock);

				rock = new BlockRock(world);
				rock.setPosition(91, 30);
				world.listWithMapObjects.add(rock);

				BlockWood wood;
				for(int i = 0; i < 3; i++){
					wood = new BlockWood(world, i == 2);
					wood.setPosition(75, 39+i);
					world.listWithMapObjects.add(wood);
				}

				for(int i = 0; i < 4; i++){
					wood = new BlockWood(world, i == 3);
					wood.setPosition(68, 39+i);
					world.listWithMapObjects.add(wood);
				}

				for(int i = 0; i < 3; i++){
					wood = new BlockWood(world, i == 2);
					wood.setPosition(81, 26+i);
					world.listWithMapObjects.add(wood);
				}


				for(int i = 0; i < 3; i++){
					wood = new BlockWood(world, i == 2);
					wood.setPosition(70, 31+i);
					world.listWithMapObjects.add(wood);
				}

				for(int i = 0; i < 4; i++){
					wood = new BlockWood(world, i == 3);
					wood.setPosition(98, 30+i);
					world.listWithMapObjects.add(wood);
				}

				return null;
			}
			
			@Override
			protected void done() {
				super.done();
				Util.stopLoadIcon();
			}
		}.execute();
	}

	public static void startAtLastSavedLevel(GameStateManagerBase gsm){
		World currentWorld = (World)gsm.getGameState(gsm.getCurrentState());
		try {
			currentWorld.readFromSave(Save.getWorldData(index));
		} catch (Exception e) {
			System.out.println("Savefiles not found. Starting new world.");
		}
	}

	private static void generateRandomTree(World world, int x, int y){
		BlockWood vine = null;

		TileMap tm = world.tileMap;
		int numLogs = 3 + Constants.RANDOM.nextInt(2);

		if(tm.getBlockID(x, y) == 0 && tm.getType(y-1, x) == Tile.SOLID){

			boolean flag = false; 

			for(int i = 0; i < numLogs; i++)
				if(tm.getBlockID(x, y+i) != 0)
					flag = true;

			if(!flag){
				for(int i = 0; i < numLogs; i++){
					vine = new BlockWood(world, i == numLogs-1);
					vine.setPosition(x, y+i);
					world.listWithMapObjects.add(vine);
				}
			}

		}
	}

	private static void generateRandomOre(World world, String block, int x, int y, int rarity){

		if(Constants.RANDOM.nextInt(rarity) > 0)
			return;

		TileMap tm = world.tileMap;

		BlockBreakable b = (BlockBreakable) Blocks.loadMapObjectFromString(block, world);

		if(tm.isAir(x, y) && tm.getType(y+1, x) == Tile.SOLID){
			b.setPosition(x, y);
			world.listWithMapObjects.add(b);
		}
	}

	private static void populateEntities(World world, String uin, int rarity){

		if(Constants.RANDOM.nextInt(rarity) > 0)
			return;
		
		TileMap tm = world.tileMap;

		EntityLiving el = (EntityLiving) Entity.createEntityFromUIN(uin, world);

		int x = Constants.RANDOM.nextInt(tm.getXRows());
		int y = Constants.RANDOM.nextInt(tm.getYRows());

		if(y+1 < tm.getYRows())
			if(world.tileMap.getBlockID(x, y) == 0){
				if(world.tileMap.getBlockID(x, y+1) > 0){
					el.setPosition(x, y);
					world.listWithMapObjects.add(el);

				}
			}
	}
	
	private static void populateWaterEntities(World world, String uin, int x, int y){

		EntityLiving el = (EntityLiving) Entity.createEntityFromUIN(uin, world);
		el.setPosition(x, y);
		world.listWithMapObjects.add(el);
	}

	private static void populateWorld(World world){

		int x = world.tileMap.getXRows();
		int y = world.tileMap.getYRows();

		int airBlocks = 0;

		for(int i = 0; i < x; i++){
			for(int j = 0; j < y; j++){
				if(world.tileMap.getBlockID(i, j) == 0){
					airBlocks++;
				}

				//full water block
				if(world.tileMap.getBlockID(i, j) == 10){
					if(Constants.RANDOM.nextInt(15) == 0){
						int fish = Constants.RANDOM.nextInt(4)+1;
						while(fish > 0){
							populateWaterEntities(world, Entity.FISH, i, j);
							fish--;
						}
					}
				}
			}
		}

		System.out.println(airBlocks/10);

		for(int i = 0; i < airBlocks/10; i++){
			generateRandomTree(world, Constants.RANDOM.nextInt(x),  Constants.RANDOM.nextInt(y));
			generateRandomOre(world, Blocks.ROCK, Constants.RANDOM.nextInt(x), Constants.RANDOM.nextInt(y), 3);
			generateRandomOre(world, Blocks.IRON, Constants.RANDOM.nextInt(x), Constants.RANDOM.nextInt(y), 10);
			
			populateEntities(world, Entity.PIG, 20);
		}
	}

	public static void writeRandomParts(DataTag tag){
		tag.writeInt("worldIndex", Loading.index);
		tag.writeInt("mapNumber", Loading.maps);
	}

	public static void readRandomParts(DataTag tag){
		index = tag.readInt("worldIndex");
		maps = tag.readInt("mapNumber");
		//		tutorial = tag.readBoolean("tutorialPlayed");
	}

}
