package be.sioxox.manager;

import java.io.File;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		GalkinsUpdateManager gum = new GalkinsUpdateManager("http://galkins.fr/launcher/galkins/",
				System.getProperty("user.home") + "/AppData/Roaming/.Galkins");
		
		Updater.update(gum);
	}
	
	public static long folderSize(File directory) {
	    long length = 0;
	    for (File file : directory.listFiles()) {
	        if (file.isFile())
	            length += file.length();
	        else
	            length += folderSize(file);
	    }
	    return length;
	}
}
