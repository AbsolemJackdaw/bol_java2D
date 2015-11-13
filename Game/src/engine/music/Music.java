package engine.music;

import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music {

	private static HashMap<String, Clip> clips;
	private static int gap;
	private static boolean mute = false;

	public static void close(String s) {
		stop(s);
		clips.get(s).close();
	}

	public static int getFrames(String s) {
		return clips.get(s).getFrameLength();
	}

	public static int getPosition(String s) {
		return clips.get(s).getFramePosition();
	}

	public static void init() {
		clips = new HashMap<String, Clip>();
		gap = 0;

	}

	public static void load(String s, String n) {
		if (clips.get(n) != null)
			return;
		Clip clip;
		try {
			final AudioInputStream ais = AudioSystem.getAudioInputStream(Music.class.getResourceAsStream(s));
			final AudioFormat baseFormat = ais.getFormat();
			final AudioFormat decodeFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
					false);
			final AudioInputStream dais = AudioSystem.getAudioInputStream(
					decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
			clips.put(n, clip);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void loop(String s) {
		loop(s, gap, gap, clips.get(s).getFrameLength() - 1);
	}

	public static void loop(String s, int frame) {
		loop(s, frame, gap, clips.get(s).getFrameLength() - 1);
	}

	public static void loop(String s, int start, int end) {
		loop(s, gap, start, end);
	}

	public static void loop(String s, int frame, int start, int end) {
		stop(s);
		if (mute)
			return;
		clips.get(s).setLoopPoints(start, end);
		clips.get(s).setFramePosition(frame);
		clips.get(s).loop(Clip.LOOP_CONTINUOUSLY);
	}

	public static void play(final String s){
		new Thread(
				new Runnable() {
					@Override
					public void run() {
						playMusic(s);	
					}
				}).start();
	}
	
	public static void play(final String s, final int gap){
		new Thread(
				new Runnable() {
					@Override
					public void run() {
						playMusic(s, gap);	
					}
				}).start();
	}

	private static void playMusic(String s) {
		if(!clips.containsKey(s)){
			System.out.println("the key " + s + " for sounds does not exist");
			return;
		}

		try {
			play(s, gap);
		} catch (Exception e) {
			System.out.println("An error occured playing a sound file !");
			e.printStackTrace();
		}
	}

	private static void playMusic(String s, int i) {
		
		if(!clips.containsKey(s)){
			System.out.println("the key " + s + " for sounds does not exist");
			return;
		}
		
		try {

			if (mute)
				return;

			final Clip c = clips.get(s);

			if (c == null)
				return;

			if (c.isRunning())
				c.stop();

			c.setFramePosition(i);

			while (!c.isRunning())
				c.start();

		} catch (Exception e) {
			System.out.println("An error occured trying to play a sound file !");
			e.printStackTrace();
		}
	}

	public static void resume(String s) {
		if (mute)
			return;
		if (clips.get(s).isRunning())
			return;
		clips.get(s).start();
	}

	public static void setPosition(String s, int frame) {
		clips.get(s).setFramePosition(frame);
	}

	public static void stop(String s) {
		if (clips.get(s) == null)
			return;
		if (clips.get(s).isRunning())
			clips.get(s).stop();
	}

	/**toggles mute on or off*/
	public static void toggleMute(){
		mute = !mute;
	}

}