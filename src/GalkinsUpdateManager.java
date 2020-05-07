package be.sioxox.manager;

import java.io.File;
import java.io.IOException;

public class GalkinsUpdateManager {

	private String baseURL;
	private File outputFolder;

	public GalkinsUpdateManager(String httpBaseUrl, String outputFolder)
			throws IOException {
		this.baseURL = httpBaseUrl;
		File outputFolderFile = new File(outputFolder);
		this.outputFolder = outputFolderFile;
		this.outputFolder.mkdirs();
	}

	public String getBaseURL() {
		return baseURL;
	}

	public File getOutputFolder() {
		return outputFolder;
	}

}