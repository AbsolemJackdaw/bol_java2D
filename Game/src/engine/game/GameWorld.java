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
	public ArrayList<MapObject> listWithMapObjects ;

	protected String resourceMapPath = "";

	protected boolean isConsoleDisplayed = false;
	protected String consolePrint = "";

	public boolean showBoundingBoxes;

	public GameWorld(GameStateManagerBase gsm) {
		this.gsm = gsm;
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
}
