package be.sioxox.manager;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Updater {

	static int totalSize;
	static int currentSize;

	private static ArrayList<FileInZip> zipUpdate(
			GalkinsUpdateManager updateManager) throws IOException {
		System.out.println("[GalkinsUpdateManager] Listing files in zips...");
		ArrayList<FileInZip> filesInZip = ZipFilesLister
				.listFiles(updateManager);

		ArrayList<URL> zipsToDownload = new ArrayList<URL>();

		System.out
				.println("[GalkinsUpdateManager] Starting downloads and uncompression of zips containing unexisting/modified files");
		for (FileInZip fiz : filesInZip) {
			File file = new File(updateManager.getOutputFolder(),
					fiz.getFilePath());
			if (!file.exists() || fiz.getLastModified() != file.lastModified()) {
				URL zipURL = new URL(updateManager.getBaseURL() + "/zips/"
						+ fiz.getBaseZip().replace(" ", "%20"));

				setTotalLength(zipURL);

				if (!ArraysUtil.contains(zipsToDownload, zipURL))
					zipsToDownload.add(zipURL);
			}
		}

		for (URL url : zipsToDownload) {
			File zipFile = new File(updateManager.getOutputFolder(), url
					.toString().replace(updateManager.getBaseURL() + "/zips/",
							""));

			zipFile.delete();

			Downloader.pool.submit(new DownloadAndUnzipTask(url, zipFile));

			while (currentSize < totalSize) {
				setCurrentLength(zipFile.length());
			}
		}

		return filesInZip;
	}

	public static void update(GalkinsUpdateManager updateManager)
			throws IOException {
		System.out.println("[GalkinsUpdateManager] Sending request to gum.php");
		URL gumPhpURL = new URL(updateManager.getBaseURL() + "/gum.php");
		HttpURLConnection con = (HttpURLConnection) gumPhpURL.openConnection();
		if (!con.getResponseMessage().contains("OK"))
			throw new IOException("Can't connect to the gum.php ! Response : "
					+ con.getResponseMessage() + " URL : "
					+ gumPhpURL.toString());

		ArrayList<FileInZip> filesInZip = zipUpdate(updateManager);

		System.out.println("[GalkinsUpdateManager] Reading .gumignore file");
		FileIgnorer.init(updateManager);

		System.out.println("[GalkinsUpdateManager] Listing local files...");
		ArrayList<File> localFiles = Util.listFiles(updateManager, "");

		System.out.println("[GalkinsUpdateManager] Listing cloud files...");
		ArrayList<FileToUpdate> cloudFiles = NetFilesLister
				.listFiles(updateManager);

		System.out
				.println("[GalkinsUpdateManager] Starting downloads of unexisting/modified files");
		for (FileToUpdate f : cloudFiles)
			if (!ArraysUtil.compare(updateManager, f, localFiles)) {
				URL fileURL = new URL(f.getFileURL().toString()
						.replace("//", "/").replace("http:/", "http://")
						.replace(" ", "%20"));
				setTotalLength(fileURL);
				System.out.println(getTotalLength());
				final File dest = new File(updateManager.getOutputFolder(), f
						.getFileURL().toString().replace("%20", " ")
						.replace(updateManager.getBaseURL() + "/files/", ""));

				dest.delete();

				Downloader.pool.submit(new DownloadTask(f.getFileURL(), dest));

				while (currentSize < totalSize) {
					setCurrentLength(dest.length());
				}
			}
		System.out
				.println("[GalkinsUpdateManager] Waiting for downloads/unzip tasks...");
		Downloader.pool.shutdown();
		try {
			Downloader.pool.awaitTermination(Long.MAX_VALUE,
					TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
		}

		System.out.println("[GalkinsUpdateManager] Relisting local files...");
		localFiles = Util.listFiles(updateManager, "");

		System.out.println("[GalkinsUpdateManager] Updating dates");
		for (FileToUpdate f : cloudFiles)
			if (!ArraysUtil.compare(updateManager, f, localFiles))
				ArraysUtil.updateDate(updateManager, f, localFiles);

		System.out
				.println("[GalkinsUpdateManager] Updating date of extracted files");
		for (FileInZip fiz : filesInZip) {
			File localFile = new File(updateManager.getOutputFolder(),
					fiz.getFilePath());
			localFile.setLastModified(fiz.getLastModified());
		}

		System.out.println("[GalkinsUpdateManager] Deleting unknown files");
		for (File f : localFiles)
			if (!ArraysUtil.exists(updateManager, f, cloudFiles)
					&& !ArraysUtil.isInZip(updateManager, f, filesInZip)) {
				System.out.println("[GalkinsUpdateManager] Deleting file "
						+ f.getAbsolutePath());
				f.delete();
			}

		System.out.println("[GalkinsUpdateManager] Deleting empty folders");
		Util.deleteEmptyFolders(Arrays.asList(updateManager.getOutputFolder()
				.listFiles()));

		System.out.println("[GalkinsUpdateManager] Up to date !");
	}

	public static void setCurrentLength(long fileSize) throws IOException {
		int length = (int) fileSize;
		currentSize = currentSize + length;
	}

	public static int getCurrentLength() {
		return currentSize;
	}

	public static void setTotalLength(URL url) throws IOException {
		int length = url.openConnection().getContentLength();
		totalSize = totalSize + length;
	}

	public static int getTotalLength() {
		return totalSize;
	}

}
