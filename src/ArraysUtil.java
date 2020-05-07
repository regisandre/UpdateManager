package be.sioxox.manager;

import java.io.File;
import java.util.ArrayList;

public final class ArraysUtil {

	public static boolean compare(GalkinsUpdateManager updateManager,
			FileToUpdate cloudFile, ArrayList<File> localList) {
		for (File f : localList) {
			String urlPath = cloudFile
					.getFileURL()
					.toString()
					.replaceAll("%20", " ")
					.replace(updateManager.getBaseURL().toString() + "/files/",
							"").replace("//", "/").replace("//", "/");
			String localPath = f
					.getAbsolutePath()
					.replace(updateManager.getOutputFolder().getAbsolutePath(),
							"").replace("\\", "/");

			if (urlPath.startsWith("/"))
				urlPath = urlPath.replaceFirst("/", "");
			if (localPath.startsWith("/"))
				localPath = localPath.replaceFirst("/", "");

			if (localPath.equals(urlPath))
				return f.lastModified() == cloudFile.getLastModified()
						.getTime();
		}
		return false;
	}

	public static boolean exists(GalkinsUpdateManager updateManager,
			File localFile, ArrayList<FileToUpdate> cloudList) {
		for (FileToUpdate f : cloudList) {
			String urlPath = f
					.getFileURL()
					.toString()
					.replaceAll("%20", " ")
					.replace(updateManager.getBaseURL().toString() + "/files/",
							"").replace("//", "/").replace("//", "/");
			String localPath = localFile
					.getAbsolutePath()
					.replace(updateManager.getOutputFolder().getAbsolutePath(),
							"").replace("\\", "/");

			if (urlPath.startsWith("/"))
				urlPath = urlPath.replaceFirst("/", "");
			if (localPath.startsWith("/"))
				localPath = localPath.replaceFirst("/", "");

			if (localPath.equals(urlPath))
				return true;
		}
		return false;
	}

	public static boolean isInZip(GalkinsUpdateManager updateManager,
			File localFile, ArrayList<FileInZip> filesInZips) {
		for (FileInZip f : filesInZips) {
			String localFilePath = localFile
					.getAbsolutePath()
					.replace(updateManager.getOutputFolder().getAbsolutePath(),
							"").replace("\\", "/");
			if (localFilePath.startsWith("/"))
				localFilePath = localFilePath.replaceFirst("/", "");

			if (localFilePath.equals(f.getFilePath()))
				return true;
		}
		return false;
	}

	public static void updateDate(GalkinsUpdateManager updateManager,
			FileToUpdate cloudFile, ArrayList<File> localFiles) {
		for (File f : localFiles) {
			String urlPath = cloudFile
					.getFileURL()
					.toString()
					.replaceAll("%20", " ")
					.replace(updateManager.getBaseURL().toString() + "/files/",
							"").replace("//", "/").replace("//", "/");
			String localPath = f
					.getAbsolutePath()
					.replace(updateManager.getOutputFolder().getAbsolutePath(),
							"").replace("\\", "/");

			if (urlPath.startsWith("/"))
				urlPath = urlPath.replaceFirst("/", "");
			if (localPath.startsWith("/"))
				localPath = localPath.replaceFirst("/", "");

			if (localPath.equals(urlPath))
				f.setLastModified(cloudFile.getLastModified().getTime());
		}
	}

	public static <T> boolean contains(ArrayList<T> list, T element) {
		for (T t : list)
			if (t.equals(element))
				return true;
		return false;
	}

}
