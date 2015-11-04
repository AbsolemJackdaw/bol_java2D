package game.content.save;

import static engine.save.Save.read;
import static engine.save.Save.write;

import org.json.simple.JSONObject;

import engine.keyhandlers.KeyHandler;
import engine.save.DataTag;
import game.World;
import game.content.Loading;
import game.entity.living.player.Player;


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
}
