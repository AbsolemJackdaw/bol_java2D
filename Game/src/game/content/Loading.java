package game.content;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import engine.game.entity.EntityLiving;
import engine.gamestate.GameStateManagerBase;
import engine.keyhandlers.KeyHandler;
import engine.map.TileMap;
import engine.save.DataTag;
import game.GameStateManager;
import game.World;
import game.content.WorldTask.EnumTask;
import game.content.save.Save;
import game.entity.Entity;
import game.entity.block.Blocks;
import game.entity.block.breakable.BlockBreakable;
import game.entity.block.breakable.BlockRock;
import game.entity.block.breakable.BlockWood;
import game.entity.block.environement.BlockInfoPane;
import game.entity.living.player.Player;
import game.item.Items;


public class Loading {

	public static BufferedImage[] stalactites = new BufferedImage[5];

	//both ints are saved in World
	/**always increases. tracks the number of unlocked maps*/
	public static int maps = 1;

	/**increases and decreases. tracks the player's current map*/
	public static int index = 0;

	/**
	 *Provides a random new map between map #2 and #max_total of maps 
	 */
	public static String newMap(){

		//skip map x_1, so that map is used only in the very beginning
		// x1 + x2 is max_total maps, where map min is 2
		int i = new Random().nextInt(5)+2;
		String s = "/maps/maps/map/Map" + i + ".map";
		System.out.println(s);
		return s;
	}

	public static void gotoNextLevel(GameStateManagerBase gsm){

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
			generateRandomTree(newWorld);
			generateRandomOre(newWorld, Blocks.IRON, 7);
			generateRandomOre(newWorld, Blocks.ROCK, 20);
			populateEntities(newWorld, Entity.PIG, 10);
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

		for(int i = 0; i < newWorld.tileMap.getXRows(); i++)
			for(int j = 0; j < newWorld.tileMap.getYRows(); j++){
				if(newWorld.tileMap.getBlockID(i, j) == 7)
					newWorld.getPlayer().setPosition(i*32 + 32+16, j*32);
			}

		//save the new world as well > this prevents bugs/glitches if closed without saving !
		Save.writeWorld(newWorld, index);
		Save.writePlayerData(newWorld.getPlayer());

	}

	public static void gotoPreviousLevel(GameStateManagerBase gsm){

		World currentWorld = (World)gsm.getGameState(gsm.getCurrentState());

		if(index == 0){
			currentWorld.getPlayer().setVector(4, 0);
			return;
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
					newWorld.getPlayer().setPosition(i*32 - 16, j*32);
					newWorld.getPlayer().facingRight = false;
				}
			}
		Save.writeRandomParts();

		//save world we went to, this prevents bugs/glitches if closed without saving !
		Save.writeWorld(newWorld, index);
		Save.writePlayerData(newWorld.getPlayer());
	}

	public static void loadTutorialLevel(GameStateManagerBase gsm){

		World world = (World)gsm.getGameState(gsm.getCurrentState());

		Player player = world.getPlayer();
		player.setPosition(66, 63);

		world.tasks.add(new WorldTask(WorldTask.JUMP, 1, EnumTask.ACTION));
		world.tasks.add(new WorldTask(WorldTask.WALK, 1, EnumTask.ACTION));
		world.tasks.add(new WorldTask(WorldTask.SWIM, 1, EnumTask.ACTION));
		world.tasks.add(new WorldTask(Items.woodChip.getDisplayName(), 10, EnumTask.COLLECTIBLE));
		world.tasks.add(new WorldTask(Items.rock.getDisplayName(), 5, EnumTask.COLLECTIBLE));

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
		text.add("Inventory :" );
		text.add("Open with " + KeyHandler.getKeyName(KeyHandler.INVENTORY).toLowerCase());
		text.add("Navigate with arrow keys.");
		pane.setText(text);
		pane.setPosition(83, 48);
		world.listWithMapObjects.add(pane);

		pane = new BlockInfoPane(world, Blocks.SIGN);
		text = new ArrayList<String>();
		text.add("Have you tried making a work desk ?");
		text.add("Make some sticks in your inventory !");
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

	}

	public static void startAtLastSavedLevel(GameStateManagerBase gsm){
		World currentWorld = (World)gsm.getGameState(gsm.getCurrentState());
		try {
			currentWorld.readFromSave(Save.getWorldData(index));
		} catch (Exception e) {
			System.out.println("Savefiles not found. Starting new world.");
		}
	}

	private static void generateRandomTree(World world){
		BlockWood b = null;

		TileMap tm = world.tileMap;

		for(int numTrees = 0; numTrees < 5; numTrees++){

			int x = new Random().nextInt(tm.getXRows());
			int y = new Random().nextInt(tm.getYRows());

			for(int numLogs = 0; numLogs < 3; numLogs++){

				if(y - numLogs > 0)
					if(world.tileMap.isAir(x, (y-numLogs))){
						b = new BlockWood(world);
						b.setPosition(x, (y-numLogs));
						world.listWithMapObjects.add(b);
						System.out.println("added block at " + x + " " + (y-numLogs));
					}
			}
		}
	}

	private static void generateRandomOre(World world, String block, int loops){
		TileMap tm = world.tileMap;

		for(int i = 0; i < loops; i++){
			BlockBreakable b = (BlockBreakable) Blocks.loadMapObjectFromString(block, world);

			int x = new Random().nextInt(tm.getXRows());
			int y = new Random().nextInt(tm.getYRows());

			if(y+1 < tm.getYRows())
				if(world.tileMap.getBlockID(x, y) == 0){
					if(world.tileMap.getBlockID(x, y+1) > 0){
						b.setPosition(x, y);
						world.listWithMapObjects.add(b);
					}
				}
		}
	}

	private static void populateEntities(World world, String uin, int loops){

		TileMap tm = world.tileMap;

		for(int i = 0; i < loops; i++){
			EntityLiving el = (EntityLiving) Entity.createEntityFromUIN(uin, tm, world);

			int x = new Random().nextInt(tm.getXRows());
			int y = new Random().nextInt(tm.getYRows());

			if(y+1 < tm.getYRows())
				if(world.tileMap.getBlockID(x, y) == 0){
					if(world.tileMap.getBlockID(x, y+1) > 0){
						el.setPosition(x, y);
						world.listWithMapObjects.add(el);
						System.out.println("added pig at " + x + " " + (y));

					}
				}
		}
	}

	public static void writeRandomParts(DataTag tag){
		tag.writeInt("worldIndex", Loading.index);
		tag.writeInt("mapNumber", Loading.maps);
	}

	public static void readRandomParts(DataTag tag){
		index = tag.readInt("worldIndex");
		maps = tag.readInt("mapNumber");
	}
}
