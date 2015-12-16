package engine.game;

import java.awt.Graphics2D;
import java.util.ArrayList;

import engine.gamestate.GameState;
import engine.gamestate.GameStateManagerBase;
import engine.map.TileMap;
import engine.save.DataList;
import engine.save.DataTag;

public class GameWorld extends GameState {

	public TileMap tileMap;

	/**ArrayList filled with every entity in this world instance*/
	private ArrayList<MapObject> listWithMapObjects ;

	protected String resourceMapPath = "";

	protected boolean isConsoleDisplayed = false;
	protected String consolePrint = "";

	public boolean showBoundingBoxes;

	public GameWorld(GameStateManagerBase gsm) {
		this.gsm = gsm;
		listWithMapObjects = new ArrayList<MapObject>();
	}

	@Override
	public void draw(Graphics2D g) {

	}

	@Override
	public void update() {

	}

	public void loadMap(String s){
		resourceMapPath = s;
		tileMap.loadTiles(getTileTexture());
		tileMap.loadMap(resourceMapPath);
		tileMap.setPosition(0, 0);
	}

	public String getTileTexture(){
		return "/maps/platformer_tiles.png";
	}

	/**
	 * Write world data to json file
	 */
	public void writeToSave(DataTag tag){
		tag.writeString("map", resourceMapPath);

		DataList list = new DataList();

		for(MapObject mo : listWithMapObjects){

			DataTag dt = new DataTag();
			mo.writeToSave(dt);
			list.write(dt);
		}
		tag.writeList("content", list);
	}

	/**
	 * Read world data from json file
	 */
	public void readFromSave(DataTag tag){
		loadMap(tag.readString("map"));
	}

	public void init(){
	}

	/**
	 * returns a copy of the list with all entities in the world
	 * do not use to add entities to a list
	 */

	public ArrayList<MapObject> getWorldEntities(){
		return new ArrayList<MapObject>(listWithMapObjects) ; 
	}

	private ArrayList<MapObject> queuedEntities = new ArrayList<MapObject>();
	private ArrayList<MapObject> queuedRemoval = new ArrayList<MapObject>();

	/**queued map object to be added to world entity list*/
	public void addEntity(MapObject mo){
		queuedEntities.add(mo);
	}

	/**queues map object to be removed from world entity list*/
	public void removeEntity(MapObject mo){
		queuedRemoval.add(mo);
	}
	
	/**removes queued map objects from world entity list */
	protected void removeQueuedEntities(){
		if(!queuedRemoval.isEmpty())
			for(MapObject mo: queuedRemoval){
				listWithMapObjects.remove(mo);
			}
		queuedRemoval.clear();
	}
	
	/**adds queued map objects to world entity list */
	protected void loadQueuedEntities(){
		//add queued entities to world before updating them
		if(!queuedEntities.isEmpty())
			for(MapObject mo: queuedEntities){
				listWithMapObjects.add(mo);
			}
		queuedEntities.clear();
	}
}
