package engine.window;

import static engine.music.Music.load;
import engine.music.Music;

public class MusicPanel implements Runnable{

	public static String sound = "";
	private boolean running;
	
	@Override
	public void run() {

		running = true;
		
		Music.init();
		loadMusic();
		
		while (running) {
			if(sound.length() > 1){
				Music.play(sound);
				sound = "";
			}
		}
	}

	public void loadMusic(){
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
