package be.sioxox.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FileIgnorer {

	private static ArrayList<String> filesToIgnore = new ArrayList<String>();

	public static void init(GalkinsUpdateManager updateManager)
			throws IOException {
		filesToIgnore.add(".gumignore");
		URL url = new URL(updateManager.getBaseURL() + "/.gumignore");
		File tmpGumIgnore = new File(updateManager.getOutputFolder(),
				".tmpgumignore");

		Downloader.downloadFile(url, tmpGumIgnore);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.addRequestProperty("User-Agent", "Mozilla/4.76");
		BufferedReader br = new BufferedReader(new FileReader(tmpGumIgnore));
		while (br.ready())
			filesToIgnore.add(br.readLine());
		br.close();
		
		tmpGumIgnore.delete();
	}

	public static ArrayList<String> getFilesToIgnore() {
		return filesToIgnore;
	}

	public static boolean ignore(GalkinsUpdateManager updateManager, File ftu) {
		for (String file : filesToIgnore) {
			String fileName = ftu
					.getAbsolutePath()
					.replace(updateManager.getOutputFolder().getAbsolutePath(),
							"").replace('\\', '/');
			if (fileName.startsWith("/"))
				fileName = fileName.replaceFirst("/", "");
			if (file.equals(fileName))
				return true;
		}
		return false;
	}

}
