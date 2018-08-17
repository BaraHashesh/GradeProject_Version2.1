package models.client_models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.shared_models.BasicFileData;

/**
 * RowData object is used as a container for the MyFile object
 * to interfaced with the TableView in javafx
 */
public class RowData extends BasicFileData{
	private static final String FILE_NAME = "b626052a_d7bf_41b1_b365_3c653a2936a8";

	/**
	 * method used to get the size of the file
	 * @return the size of file in bytes or K-bytes or M-bytes
	 */
	public String getSizeBytes() {

		if(isDirectory())
			return "";

		else {
			long totalSize = getSize();
			String info = "B";

			if(totalSize >= 1024) {
				totalSize = (long)Math.ceil((double)totalSize/1024.0);
				info = "KB";
			}

			if(totalSize >= 1024) {
				totalSize = (long)Math.ceil((double)totalSize/1024.0);
				info = "MB";
			}

			return totalSize + " " + info;
		}
	}


	/**
	 * method used to create Icons to display for files
	 * @return the icon of the file
	 */
	@SuppressWarnings("static-access")
	public ImageView getIcon() {
		try {

			if(isDirectory()) {
				return new ImageView(new Image(getClass().getResource("/resources/images/folder.png").openStream()));
			}

			String extension = extractExtension();

			if (extension.compareTo("") == 0) {
				return new ImageView(new Image(getClass().getResource("/resources/images/file.png").openStream()));
			}

			File file = File.createTempFile(this.FILE_NAME, "." + extension);

			ImageIcon swingImageIcon = (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(file);
			java.awt.Image awtImage = swingImageIcon.getImage();
			Image image = SwingFXUtils.toFXImage((BufferedImage) awtImage, null);

			file.delete();

			return new ImageView(image);

			//return new ImageView(new Image(getClass().getResource("/resources/" + extension +".png").openStream()));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		try {
			return new ImageView(new Image(getClass().getResource("/resources/images/file.png").openStream()));
		}catch(Exception e) {
			return null;
		}
	}

	/**
	 * method used to obtain the system type of the file
	 * @return the system type of the file
	 */
	public String getType() {
		if(isDirectory())
			return "File Folder";

		else if(extractExtension().compareTo("") == 0)
			return "File";

		else {
			try {
				@SuppressWarnings({"static-access"})
				File file = File.createTempFile(this.FILE_NAME, "."+extractExtension());

				String type = FileSystemView.getFileSystemView().getSystemTypeDescription(file);

				file.delete();

				return type;
			}
			catch(Exception e) {
				e.printStackTrace();
				return "File";
			}
		}
	}

	/**
	 * method used to modify the format of the date
	 * @return the date of the last edit done to the file
	 */
	public String getModifiedDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return dateFormat.format(this.getLastModified());
	}

    /**
     * method used to extract file name from file path
     * @return the name of the file
     */
	public String getName(){
		int i = getPath().lastIndexOf(OperatingSystemAdapter.getOS().getFileDash());

		return getPath().substring(i+1);
	}

	/**
	 * method used to obtain parent directory
	 * @return directory at which the parent exists
	 */
	public String getPreviousDirectory() {
		String parent = getParent();

		int i = parent.lastIndexOf(OperatingSystemAdapter.getOS().getFileDash());

		return parent.substring(0, i+1);
	}

    /**
     * method used to extract file's parent from file path
     * @return the parent of the file
     */
	public String getParent(){
		int i = getPath().lastIndexOf(OperatingSystemAdapter.getOS().getFileDash());

		return getPath().substring(0, i);
	}

    /**
     * method used to extract exact type of file
     * @return extension for the file (EXE, PDF, ...)
     */
    private String extractExtension() {
        String extension = "";

        int i = getPath().lastIndexOf('.');
        int p = getPath().lastIndexOf(OperatingSystemAdapter.getOS().getFileDash());

        if (i > p && i > 0) {
            extension = getPath().substring(i+1);
        }

        return extension;
    }
    
    public static RowData[] convertBasicFileDataToRowData(BasicFileData... basicFileDatas) {
    	RowData[] result = new RowData[basicFileDatas.length];
    	
    	for(int i = 0; i < basicFileDatas.length; i++) {
    		result[i] = new RowData();
    		result[i].setPath(basicFileDatas[i].getPath());
    		result[i].setDirectory(basicFileDatas[i].isDirectory());
    		result[i].setLastModified(basicFileDatas[i].getLastModified());
    		result[i].setSize(basicFileDatas[i].getLastModified());
    	}
    	return result;
    }
}
