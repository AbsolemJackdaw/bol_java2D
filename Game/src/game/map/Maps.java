package game.map;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Maps {

	private static ArrayList<String> maps;

	public static void init(){
		maps = new ArrayList<String>();
		load();
	}

	private static void load(){
		System.out.println("loading");

		final String path = "maps/";
		final File jarFile = new File(Maps.class.getProtectionDomain().getCodeSource().getLocation().getPath());

		if(jarFile.isFile()) {  // Run with JAR file

			try {
				JarFile jar = new JarFile(jarFile);

				final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
				while(entries.hasMoreElements()) {
					final String name = entries.nextElement().getName();
					if (name.startsWith(path +"map")) { //filter according to the path
						maps.add(name);
					}
				}
				jar.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else { // Run with IDE
			final URL url = Maps.class.getResource("/" + path);
			System.out.println(url);
			if (url != null) {
				try {
					final File apps = new File(url.toURI());
					for (File app : apps.listFiles()) {
						if(app.getName().startsWith("map")){
							maps.add(app.getName());
						}
					}
				} catch (URISyntaxException ex) {
					// never happens
				}
			}
		}
	}

	public static ArrayList<String> getMaps() {
		return maps;
	}

}
