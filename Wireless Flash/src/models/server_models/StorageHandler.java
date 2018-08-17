package models.server_models;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import models.shared_models.BasicFileData;
import models.shared_models.FileTransfer;

/**
 * This Class is used to access the USB and get data or directories from it
 * ROOT constant is used to indicated the path for the USB
 */
public class StorageHandler {
	
	private static final String ROOT = "D:\\";

	/**
	 * Method used to get the file list in a folder in the USB
	 * @param folderURL Path to folder (given root is an empty string i.e. "")
	 * @return list of files in a directory
	 */
	public static BasicFileData[] fileLister(String folderURL){
		if(folderURL.length() < ROOT.length())
			folderURL = ROOT;
		
		File folder = new File(folderURL);
		if(folder.exists()) 
			return BasicFileData.parseFile(folder.listFiles());
		return null;
	}
	
	/**
	 * method used to delete a file
	 * @param path is the path of the file within the USB
	 */
	public static void deleteFile(String path) {
		try {
			File toDelete = new File(path);
			if(toDelete.isDirectory()) {
				for(File sub : toDelete.listFiles()) {
					if(sub.isDirectory())
						deleteFile(sub.getPath());
					sub.delete();
				}
			}
			toDelete.delete();	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	/**
	 * method used to declare main requirements to upload files
	 * @param outToClientStrings is output stream for sending strings
	 * @param outToClientBytes is output stream for sending bytes
	 * @param path is the path of the file/folder to be uploaded
	 */
	public static void uploadFile(DataOutputStream outToClientStrings,
			DataOutputStream outToClientBytes, String path) {
		File mainFile = new File(path);
		
		String parent = mainFile.getParent();
		FileTransfer fileTransfer = new FileTransfer();
		try {
			outToClientStrings.write((fileTransfer.calculateSize(mainFile)+"").getBytes("UTF-8"));
			outToClientStrings.writeByte('\n');
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileTransfer.sendFiles(outToClientStrings, outToClientBytes, mainFile, parent);
	}
	
	/**
	 * method used to receive files from client
	 * @param fromClient is input stream
	 * @param path is location to be saved with reference to the USB
	 */
	public static void downloadFile(DataInputStream bytesStream, BufferedReader fromClient, String path) {
		if(path.compareTo("") == 0)
			path = ROOT;
		
		if(path.compareTo(ROOT) != 0)
			path = path + "\\";
		
		try {
			new FileTransfer().receiveFiles(bytesStream, fromClient, path);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}