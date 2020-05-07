package be.sioxox.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Downloader {
	
	public static ExecutorService pool = Executors.newFixedThreadPool(10);

	public static void downloadFile(URL url, File output) throws IOException {
		output.getParentFile().mkdirs();
	
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.addRequestProperty("User-Agent", "Mozilla/4.76");
		ReadableByteChannel rbc = Channels.newChannel(connection
				.getInputStream());
	
		FileOutputStream fos = new FileOutputStream(output);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	
		fos.close();
		rbc.close();
	}


}
