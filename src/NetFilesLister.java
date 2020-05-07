package be.sioxox.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class NetFilesLister {

	public static ArrayList<FileToUpdate> listFiles(
			GalkinsUpdateManager updateManager) throws IOException {
		ArrayList<FileToUpdate> list = new ArrayList<FileToUpdate>();
		URL url = new URL(updateManager.getBaseURL() + "/gum.php");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		InputStream inputStream = conn.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));

		String line = null;
		while ((line = reader.readLine()) != null) {
			list.add(new FileToUpdate(new URL((updateManager.getBaseURL()
					+ "/files/" + line).replace(" ", "%20")), new Date(Long
					.parseLong(reader.readLine()))));
		}

		return list;
	}

}
