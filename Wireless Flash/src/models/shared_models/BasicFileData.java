package models.shared_models;

import java.io.File;


/**
 * BasicFileData object is used as a replacement for the File object
 * to exchange file information over TCP
 */
public class BasicFileData{

	private String path;
	private long size;
	private long lastModified;
	private boolean directory;

	/**
	 * simple constructor for file object -> uses predefined methods
	 */
	public BasicFileData(File file) {
		this.path = file.getPath();
		this.size = file.length();
		this.lastModified = file.lastModified();
		this.directory = file.isDirectory();
	}

	/**
	 * empty constructor (used by JsonParser)
	 */
	public BasicFileData() {}

	/**
	 * get method for file size
	 * @return file size in bytes
	 */
	public long getSize() {
		return size;
	}

	/**
	 * get method for last time modified date
	 * @return date at which file was last modified
	 */
	public long getLastModified() {
		return lastModified;
	}

	/**
	 * method used to convert File objects into BasicFileData objects
	 * @param fileList is a list of File objects
	 * @return a list of my File objects
	 */
	public static BasicFileData[] parseFile(File[] fileList) {
		BasicFileData[] newList = new BasicFileData[fileList.length];
		for(int i = 0; i < fileList.length; i++)
			newList[i] = new BasicFileData(fileList[i]);
		return newList;
	}

	/**
	 * get method for the boolean directory varaiable
	 * @return wither the file is a directory or not
	 */
	public boolean isDirectory() {
		return directory;
	}

	/**
	 * get method for path
	 * @return the path of the file
	 */
	public String getPath() {
		return path;
	}


	@Override
	public String toString() {
		return "BasicFileData [path=" + path + "]";
	}


	/**
	 * method used to set the path of the file
	 * used by JsonParser Class (cast reasons) and FileTransfer Class (set relative path)
	 * @param path the new path of the file
	 */
	public void setPath(String path) {
		this.path = path;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public void setDirectory(boolean directory) {
		this.directory = directory;
	}

}
