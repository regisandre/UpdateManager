package be.sioxox.manager;

public class FileInZip {

	private String baseZip;
	private String filePath;
	private long lastModified;

	public FileInZip(String baseZipName, String filePath, long lastModified) {
		this.baseZip = baseZipName;

		if (baseZipName.contains("/")) {
			String[] splittedLocation = baseZipName.split("/");
			String zipLocation = baseZipName.substring(0, baseZipName.length()
					- splittedLocation[splittedLocation.length - 1].length());

			this.filePath = zipLocation + filePath;
		} else
			this.filePath = filePath;

		this.lastModified = lastModified;
	}

	public String getBaseZip() {
		return baseZip;
	}

	public String getFilePath() {
		return filePath;
	}

	public long getLastModified() {
		return lastModified;
	}

}
