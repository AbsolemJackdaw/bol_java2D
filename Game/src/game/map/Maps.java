package game.map;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Maps {

	private static ArrayList<String> maps;

	public void init(){
		maps = new ArrayList<String>();

		readUsingZipInputStream();

		loadIntoList();
	}

	private void loadIntoList(){

		File dir = new File("maps/");

		if(!dir.exists() || dir.listFiles().length == 0){
			System.out.println("ERROR ! No Maps were found. Game cannot continue !!");
			System.out.println("Exiting game");
			System.exit(0);
		}

		for(File f : dir.listFiles()){
			maps.add(f.getName());
		}
	}

	private static void readUsingZipInputStream() {

		try {
			InputStream is = Maps.class.getResourceAsStream("/maps/maps.zip");
			final ZipInputStream zis = new ZipInputStream(is);

			try {
				ZipEntry entry;
				while ((entry = zis.getNextEntry()) != null) {

					String name = entry.getName();
					File f = new File(name);

					if(name.endsWith("/")){
						f.mkdirs();
						continue;
					}else{
						if(!name.endsWith(".map")){
							System.out.println("skipped " + name + " which had an invalid extension");
							continue;
						}
					}

					System.out.printf("File: %s Size %d  Modified on %TD %n", entry.getName(), entry.getSize(), new Date(entry.getTime()));
					extractEntry(entry, zis);
				}
			} finally {
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void extractEntry(final ZipEntry entry, InputStream is) throws IOException {
		String exractedFile = entry.getName();
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(exractedFile);
			final byte[] buf = new byte[2048];
			int length;

			while ((length = is.read(buf, 0, buf.length)) >= 0) {
				fos.write(buf, 0, length);
			}

		} catch (IOException ioex) {
			fos.close();
		}

	}

	public static ArrayList<String> getMaps() {
		return maps;
	}
}
