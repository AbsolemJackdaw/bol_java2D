package game.content;

import static engine.music.Music.load;

import java.awt.image.BufferedImage;
import java.util.Random;

import engine.game.entity.EntityLiving;
import engine.gamestate.GameStateManagerBase;
import engine.map.TileMap;
import engine.save.DataTag;
import game.GameStateManager;
import game.World;
import game.content.save.Save;
import game.entity.Entity;
import game.entity.block.Blocks;
import game.entity.block.breakable.BlockBreakable;
import game.entity.block.breakable.BlockLog;
import game.entity.living.player.Player;


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

	public static void loadFirstTutorialLevel(GameStateManagerBase gsm){

		World world = (World)gsm.getGameState(gsm.getCurrentState());

		Player player = world.getPlayer();
		player.setPosition(12*32, 12*32);
		
//		///
//		BlockInfoPane pane = new BlockInfoPane(world.tileMap, world, Blocks.SIGN);
//		ArrayList<String> text = new ArrayList<String>();
//
//		text.add("Use " + KeyHandler.getKeyName(KeyHandler.LEFT) + KeyHandler.getKeyName(KeyHandler.DOWN) + KeyHandler.getKeyName(KeyHandler.RIGHT) + " or arrow keys");
//		text.add("to move around !");
//
//		pane.setText(text);
//		pane.setPosition(12*32, 12*32);
//
//		world.listWithMapObjects.add(pane);
//		/////
//		pane = new BlockInfoPane(world.tileMap, world, Blocks.SIGN);
//		ArrayList<String> text2 = new ArrayList<String>();
//
//		text2.add("Press " + KeyHandler.getKeyName(KeyHandler.UP) + " or Up-Arrow to jump.");
//		text2.add("The longer you hold it, the longer you'll leap.");
//		text2.add("Try to leap this gap !");
//
//		pane.setText(text2);
//		pane.setPosition(20*32, 12*32);
//
//		world.listWithMapObjects.add(pane);
//		/////
//		pane = new BlockInfoPane(world.tileMap, world, Blocks.SIGN);
//		ArrayList<String> text3 = new ArrayList<String>();
//
//		text3.add("Great ! You almost got the basics done.");
//		text3.add("Move on to learn how to swim.");
//
//		pane.setText(text3);
//		pane.setPosition(28*32, 12*32);
//
//		world.listWithMapObjects.add(pane);
//		/////
//		pane = new BlockInfoPane(world.tileMap, world, Blocks.SIGN);
//		ArrayList<String> text4 = new ArrayList<String>();
//
//		text4.add("Use the directional keys to");
//		text4.add("swim up, left, or right !");
//
//		pane.setText(text4);
//		pane.setPosition(40*32, 12*32);
//
//		world.listWithMapObjects.add(pane);

	}

	public static void loadSecondTutorialLevel(){

	}

	public static void loadThirdTutorialLevel(){

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
		BlockLog b = null;

		TileMap tm = world.tileMap;

		for(int numTrees = 0; numTrees < 5; numTrees++){

			int x = new Random().nextInt(tm.getXRows());
			int y = new Random().nextInt(tm.getYRows());

			for(int numLogs = 0; numLogs < 3; numLogs++){

				if(y - numLogs > 0)
					if(world.tileMap.isAir(x, (y-numLogs))){
						b = new BlockLog(tm, world);
						b.setPosition(x*32+16, (y-numLogs)*32+16);
						world.listWithMapObjects.add(b);
						System.out.println("added block at " + x + " " + (y-numLogs));
					}
			}
		}
	}

	private static void generateRandomOre(World world, String block, int loops){
		TileMap tm = world.tileMap;

		for(int i = 0; i < loops; i++){
			BlockBreakable b = (BlockBreakable) Blocks.loadMapObjectFromString(block, tm, world);

			int x = new Random().nextInt(tm.getXRows());
			int y = new Random().nextInt(tm.getYRows());

			if(y+1 < tm.getYRows())
				if(world.tileMap.getBlockID(x, y) == 0){
					if(world.tileMap.getBlockID(x, y+1) > 0){
						b.setPosition(x*32 + 16, y*32 + 16);
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
						el.setPosition(x*32 + 16, y*32 + 16);
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

	public static void loadMusic(){
		loadMusic("/sounds/block/wood/hit_wood_1.mp3", "hit_wood_1");
		loadMusic("/sounds/block/wood/hit_wood_2.mp3", "hit_wood_2");
		loadMusic("/sounds/block/wood/hit_wood_3.mp3", "hit_wood_3");
		loadMusic("/sounds/block/wood/hit_wood_4.mp3", "hit_wood_4");
		loadMusic("/sounds/block/wood/hit_wood_5.mp3", "hit_wood_5");
		loadMusic("/sounds/block/wood/hit_wood_6.mp3", "hit_wood_6");

		loadMusic("/sounds/block/rock/hit_rock_1.mp3", "hit_rock_1");
		loadMusic("/sounds/block/rock/hit_rock_2.mp3", "hit_rock_2");
		loadMusic("/sounds/block/rock/hit_rock_3.mp3", "hit_rock_3");
		loadMusic("/sounds/block/rock/hit_rock_4.mp3", "hit_rock_4");
		loadMusic("/sounds/block/rock/hit_rock_5.mp3", "hit_rock_5");

		loadMusic("/sounds/step_1.mp3", "step_1");
		loadMusic("/sounds/step_2.mp3", "step_2");
		loadMusic("/sounds/step_3.mp3", "step_3");
		loadMusic("/sounds/step_4.mp3", "step_4");
		loadMusic("/sounds/step_5.mp3", "step_5");

		loadMusic("/sounds/jump_1.mp3", "jump_1");
		loadMusic("/sounds/jump_2.mp3", "jump_2");
		loadMusic("/sounds/jump_3.mp3", "jump_3");
		loadMusic("/sounds/jump_4.mp3", "jump_4");
		loadMusic("/sounds/jump_5.mp3", "jump_5");

		loadMusic("/sounds/entity/pig/pig_hurt_1.mp3", "hitpig_1");
		loadMusic("/sounds/entity/pig/pig_hurt_2.mp3", "hitpig_2");
		loadMusic("/sounds/entity/pig/pig_hurt_3.mp3", "hitpig_3");
		loadMusic("/sounds/entity/pig/pig_hurt_4.mp3", "hitpig_4");
		loadMusic("/sounds/entity/pig/pig_hurt_5.mp3", "hitpig_5");

		loadMusic("/sounds/entity/player/crunch_small1.mp3", "crunch_1");
		loadMusic("/sounds/entity/player/crunch_small2.mp3", "crunch_2");
		loadMusic("/sounds/entity/player/crunch_small3.mp3", "crunch_3");
		loadMusic("/sounds/entity/player/crunch_small4.mp3", "crunch_4");
		loadMusic("/sounds/entity/player/crunch_small5.mp3", "crunch_5");

		loadMusic("/sounds/entity/explosion/explode_0.mp3","explode_0");
		loadMusic("/sounds/entity/explosion/explode_1.mp3","explode_1");
		loadMusic("/sounds/entity/explosion/explode_2.mp3","explode_2");
		loadMusic("/sounds/entity/explosion/explode_3.mp3","explode_3");
		loadMusic("/sounds/entity/explosion/explode_4.mp3","explode_4");
		loadMusic("/sounds/entity/explosion/explode_5.mp3","explode_5");
		loadMusic("/sounds/entity/explosion/explode_6.mp3","explode_6");

	}

	private static void loadMusic(String path, String name){
		load(path, name);
	}
}
