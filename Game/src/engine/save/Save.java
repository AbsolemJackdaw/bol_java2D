package engine.save;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Save {
	
	/**returns an object that is a JsonObject. 
	 * @param s String name for file*/
	public static Object read(String s){

		JSONParser parser = new JSONParser();

		try {
			Object obj = parser.parse(new FileReader("saves/"+s+".json"));
			return obj;
		} catch (FileNotFoundException e) {
//			System.out.println("[SAVING] Save file '" + s + "' not found.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**Writes a json file from the given DataTag
	 * @param s String name for file*/
	public static void write(String s, DataTag obj){
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
