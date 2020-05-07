package be.sioxox.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class DownloadAndUnzipTask implements Runnable {

	private URL url;
	private File zipFile;

	public DownloadAndUnzipTask(URL url, File zipFile) {
		this.url = url;
		this.zipFile = zipFile;
	}

	@Override
	public void run() {
		ZipFile zf = null;
		try {
			System.out
					.println("[GalkinsUpdateManager] Downloading file " + url);
			Downloader.downloadFile(url, zipFile);

			System.out.println("[GalkinsUpdateManager] Unzipping file "
					+ zipFile.getAbsolutePath());
			Charset CP866 = Charset.forName("CP866");
			zf = new ZipFile(zipFile, CP866);
			Enumeration<? extends ZipEntry> entries = zf.entries();

			while (entries.hasMoreElements()) {
				ZipEntry ze = entries.nextElement();

				File dest = new File(zipFile.getParentFile(), ze.getName());

				if (ze.isDirectory()) {
					dest.mkdirs();
					continue;
				}

				InputStream is = zf.getInputStream(ze);
				FileOutputStream fos = new FileOutputStream(dest);

				int i;
				byte[] buffer = new byte[1024];
				while ((i = is.read(buffer)) > 0)
					fos.write(buffer, 0, i);

				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (zf != null)
					zf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			zipFile.delete();

			System.out
					.println("[GalkinsUpdateManager] Finished to download and unzip file "
							+ url);
		}
	}

}
