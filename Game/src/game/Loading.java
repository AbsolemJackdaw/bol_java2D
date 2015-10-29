package game;

import game.content.SpawningLogic;
import game.content.save.DataTag;
import game.content.save.Save;
import game.entity.Entity;
import game.entity.block.BlockBreakable;
import game.entity.block.BlockLog;
import game.entity.block.Blocks;
import game.entity.living.EntityLiving;
import game.util.Util;

import java.util.Random;

import base.main.GameStateManager;
import base.tilemap.TileMap;

public class Loading {

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
		int i = new Random().nextInt(10)+2; 
		String s = "/maps/cave_rand_" + i + ".map";
		System.out.println(s);
		return s;
	}

	public static void gotoNextLevel(GameStateManager gsm){

		//save world we are currently in
		World currentWorld = (World)gsm.getGameState(gsm.getCurrentState());
		Save.writeWorld(currentWorld, index);
		Save.writePlayerData(currentWorld.getPlayer());

		//get gametime to transfer to the new world and continue counting
		int time = currentWorld.GameTime;
		float nightShade = currentWorld.nightAlhpa;

		//set a new world
		gsm.setState(GameStateManager.GAME);

		//increase index to indicate the new world's index
		index++;
		World newWorld = (World)gsm.getGameState(gsm.getCurrentState());

		newWorld.bg = Util.generateStalactiteBackGround();
		
		//if its a new map
		if(index == maps){
			String s = newMap();
			newWorld.reloadMap(s);
			maps++;
			generateRandomTree(newWorld);
			generateRandomOre(newWorld, Blocks.IRON, 7);
			generateRandomOre(newWorld, Blocks.ROCK, 20);
			populateEntities(newWorld, Entity.PIG, 10);
			//set gametime to continue counting
			newWorld.GameTime = time;
			newWorld.nightAlhpa = nightShade;
			
			if(newWorld.isNightTime()){
				SpawningLogic.spawnNightCreatures(newWorld, true);
			}

		}else{
			newWorld.readFromSave(Save.getWorldData(index));
			//set gametime to continue counting
			newWorld.GameTime = time;
			newWorld.nightAlhpa = nightShade;
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

	public static void gotoPreviousLevel(GameStateManager gsm){

		World currentWorld = (World)gsm.getGameState(gsm.getCurrentState());

		if(index == 0){
			currentWorld.getPlayer().setVector(4, 0);
			return;
		}

		//save world we are currently in
		Save.writeWorld(currentWorld, index);
		Save.writePlayerData(currentWorld.getPlayer());

		//get gametime to transfer to the new world and continue counting
		int time = currentWorld.GameTime;
		float nightShade = currentWorld.nightAlhpa;
		//set a new world
		gsm.setState(GameStateManager.GAME);

		//increase index to indicate the new world's index
		index--;
		World newWorld = (World)gsm.getGameState(gsm.getCurrentState());

		newWorld.readFromSave(Save.getWorldData(index));

		//set gametime to continue counting
		newWorld.GameTime = time;
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

	public static void startAtLastSavedLevel(GameStateManager gsm){
		World nw = (World)gsm.getGameState(gsm.getCurrentState());
		try {
			nw.readFromSave(Save.getWorldData(index));
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
}
