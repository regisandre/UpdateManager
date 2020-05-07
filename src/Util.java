package be.sioxox.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {

	public static ArrayList<File> listFiles(GalkinsUpdateManager updateManager,
			String folder) {
		ArrayList<File> files = new ArrayList<File>();
		File[] folderFiles = new File(updateManager.getOutputFolder(), folder)
				.listFiles();
		if (folderFiles != null)
			for (File f : folderFiles)
				if (!FileIgnorer.ignore(updateManager, f))
					if (f.isDirectory())
						files.addAll(listFiles(updateManager,
								folder + "/" + f.getName()));
					else
						files.add(f);

		return files;
	}

	public static void deleteEmptyFolders(List<File> files) {
		for (File f : files)
			if (f.isDirectory()) {
				deleteEmptyFolders(Arrays.asList(f.listFiles()));
				if (f.listFiles().length == 0)
					f.delete();
			}
	}

}