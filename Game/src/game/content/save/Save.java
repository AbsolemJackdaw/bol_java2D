package game.content.save;

import game.Loading;
import game.World;
import game.entity.living.player.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import base.main.keyhandler.KeyHandler;

public class Save {
	
	public static DataTag getPlayerData(){
		Object obj = read("player/player");
		if(obj == null)
			return null;
		JSONObject data = (JSONObject) obj;
		return new DataTag(data);
	}

	public static void writePlayerData(Player p){
		DataTag tag = new DataTag();
		p.writeToSave(tag);
		write("player/player", tag);
	}

	public static void writeWorld(World world, int index){
		DataTag tag = new DataTag();
		world.writeToSave(tag);
		write("world/world_"+index, tag);
	}

	public static DataTag getWorldData(int index){
		Object obj = read("world/world_"+index);
		if(obj == null)
			return null;
		JSONObject data = (JSONObject) obj;
		return new DataTag(data);
	}
	
	public static void writeRandomParts(){
		DataTag tag = new DataTag();
		Loading.writeRandomParts(tag);
		write("misc/randData", tag);
	}
	
	public static void readRandomParts(){
		Object obj = read("misc/randData");
		if(obj == null)
			return;
		JSONObject data = (JSONObject) obj;
		DataTag d = new DataTag(data);
		
		Loading.readRandomParts(d);
	}

	public static void writeKeyBinds(){
		
		DataTag tag = new DataTag();
		
		for(int i  = 0; i < KeyHandler.registeredKeys.length; i ++){
			
			tag.writeInt("key_"+i, KeyHandler.registeredKeys[i]);
			
		}
		
		write("keybinds/keys", tag);
		
	}
	
	public static void readKeyBinds(){
		
		Object obj = read("keybinds/keys");
		
		if(obj == null)
			return;
		
		JSONObject data = (JSONObject) obj;	
		DataTag tag = new DataTag(data);
		
		for(int i = 0; i < KeyHandler.registeredKeys.length; i++){
			KeyHandler.registeredKeys[i] = tag.readInt("key_"+i);
		}
		
	}

	/**returns an object that is a JsonObject. 
	 * @params s String name for file*/
	private static Object read(String s){

		JSONParser parser = new JSONParser();

		try {
			Object obj = parser.parse(new FileReader("saves/"+s+".json"));
			return obj;
		} catch (FileNotFoundException e) {
			System.out.println("[SAVING] Save file '" + s + "' not found.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**Writes a json file from the given DataTag
	 * @params s String name for file*/
	private static void write(String s, DataTag obj){
		try {

			File theDir = new File("saves/"+s+".json");
			theDir.getParentFile().mkdirs();

			FileWriter file = new FileWriter(theDir);
			file.write(obj.toString());
			file.flush();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
