package be.sioxox.manager;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class FileToUpdate {

	public static final int NOTHING = 0;
	public static final int DOWNLOAD = 1;
	public static final int REMOVE = 2;
	
	private URL fileURL;
	private File file;
	private Date lastModified;
	private int action;

	public FileToUpdate(URL fileURL, Date lastModified) {
		this.fileURL = fileURL;
		this.lastModified = lastModified;
	}

	public FileToUpdate(File file, Date lastModified) {
		this.file = file;
		this.lastModified = lastModified;
	}
	
	public URL getFileURL() {
		return fileURL;
	}

	public Date getLastModified() {
		return lastModified;
	}
	
	public File getFile() {
		return file;
	}
	
	public int getAction() {
		return this.action;
	}
	
	public void setAction(int action) {
		this.action = action;
	}
	
}
