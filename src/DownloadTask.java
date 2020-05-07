package be.sioxox.manager;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class DownloadTask implements Runnable {

	private URL url;
	private File output;

	public DownloadTask(URL url, File output) {
		this.url = url;
		this.output = output;
	}

	@Override
	public void run() {
		
		System.out.println("[GalkinsUpdateManager] Downloading file " + url);
		try {
			Downloader.downloadFile(url, output);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out
					.println("[GalkinsUpdateManager] Finished to download file "
							+ url);
		}
	}
	
}
