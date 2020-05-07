package be.sioxox.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ZipFilesLister {

	public static ArrayList<FileInZip> listFiles(
			GalkinsUpdateManager updateManager) throws IOException {
		ArrayList<FileInZip> filesInZips = new ArrayList<FileInZip>();
		URL gumZipURL = new URL(updateManager.getBaseURL() + "/.gumzip");
		File tmpGumZip = new File(updateManager.getOutputFolder(), ".tmpgumzip");

		Downloader.downloadFile(gumZipURL, tmpGumZip);

		BufferedReader br = new BufferedReader(new FileReader(tmpGumZip));
		while (br.ready()) {
			FileInZip fiz = new FileInZip(br.readLine(), br.readLine(),
					Long.parseLong(br.readLine()));
			filesInZips.add(fiz);
		}

		br.close();
		if (!tmpGumZip.delete())
			tmpGumZip.deleteOnExit();
		return filesInZips;
	}

}
