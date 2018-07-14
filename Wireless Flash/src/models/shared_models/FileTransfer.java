package models.shared_models;

import java.io.*;


/**
 * This class is used to transfer and receive files
 */
public class FileTransfer {
	private long transferredFileSize = 0;
	private File firstFile;
	private boolean work=true;
	
	private static final int BUFFER = 1024 * 1024 * 4;

	/**
	 * method used to recursively upload files/folders
	 * @param outputStreamStrings is output stream for strings
	 * @param outputStreamBytes is output stream for bytes
	 * @param file is the main file/folder to be uploaded 
	 * @param mainPath is the parents path (to establish relationship of files)
	 */
	public void sendFiles(DataOutputStream outputStreamStrings,
			DataOutputStream outputStreamBytes,
			File file, String mainPath) {
		
		try {
			//check if pipe was broken
			if(!work)
				return;

			BasicFileData basicFileData = new BasicFileData(file);

			basicFileData.setPath(basicFileData.getPath().substring(mainPath.length()));
			
			
			if(basicFileData.getPath().startsWith("\\") || basicFileData.getPath().startsWith("/"))
				basicFileData.setPath(basicFileData.getPath().substring(1));

			String jsonFile = JsonParser.basicFileDataToJson(basicFileData);
			
			outputStreamStrings.write(jsonFile.getBytes("UTF-8"));

			outputStreamStrings.writeBytes("\n");
			
			outputStreamStrings.flush();
			
			if(file.isDirectory()) {
				File[] list = file.listFiles();
				for(int i = 0; i < list.length; i++)
					sendFiles(outputStreamStrings, outputStreamBytes, list[i], mainPath);
			}else{
				FileInputStream fileData = new FileInputStream(file);
				byte[] buffer = new byte[BUFFER];
				long size = file.length();
				while(size != 0) {
					if(size >= buffer.length) {
						fileData.read(buffer, 0, buffer.length);
						outputStreamBytes.write(buffer, 0, buffer.length);
						size -= buffer.length;
						this.transferredFileSize += buffer.length;
					}
					else {
						fileData.read(buffer, 0, (int)size);
						outputStreamBytes.write(buffer, 0, (int)size);
						this.transferredFileSize += size;
						size = 0;
					}
					outputStreamBytes.flush();
				}
				fileData.close();
			}
		}catch(Exception e) {
			work = false; //pipe was broken
			e.printStackTrace();
		}
	}

	/**
	 * method used to download files/folders
	 * @param byteStream is input stream to receive bytes from
	 * @param inputStream is input stream to receive strings from
	 * @param path is location to save data under
	 */
	public void receiveFiles(DataInputStream byteStream, BufferedReader inputStream, String path){
		FileOutputStream output = null;
		try {
			for(String temp; (temp = inputStream.readLine()) != null; ) {
				BasicFileData[] tempBasicFileData = JsonParser.JsonToBasicFileData(temp);
				BasicFileData basicFileData = null;

				if(tempBasicFileData != null)
					basicFileData = tempBasicFileData[0];
				else
					throw new Exception("No Meta Data");

				if(basicFileData.isDirectory()) {
					File file = new File(path+basicFileData.getPath());
					
					//check if file is the first to be received
					if(firstFile == null)
						firstFile = file;
					
					file.mkdirs();
				}
				else {

					output = new FileOutputStream(path+basicFileData.getPath());
					long size = basicFileData.getSize();
					byte[] buffer = new byte[BUFFER];

					//check if file is the first to be received
					if(firstFile == null)
						firstFile = new File(path+basicFileData.getPath());
					
					while(size > 0) {
						int bytesRead = 0;

						if(size > BUFFER)
							bytesRead = byteStream.read(buffer, 0, BUFFER);
						else
							bytesRead = byteStream.read(buffer, 0, (int)size);

						if (bytesRead != -1) {
							this.transferredFileSize += bytesRead;
							size -= bytesRead;
							output.write(buffer, 0, bytesRead);
						}else
							break;

					}
					output.close();
				}
			}
		}catch(Exception e) {	
			try {output.close();} catch (IOException e1) {}
			deleteFile(firstFile);
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used to calculate Total size for files/folders
	 * @param file is a file/folder
	 * @return the total size of the file/folder
	 */
	public long calculateSize(File file) {
		long sum = 0;
		if(file.isDirectory()) {
			for(File temp : file.listFiles()) {
				if(temp.isDirectory())
					sum += calculateSize(temp);
				else
					sum += temp.length();
			}
		}
		else{
			sum += file.length();
		}
		return sum;
	}
	
	/**
	 * get method for transferredFileSize
	 * @return the number of bytes that have been written/read so far
	 */
	public long getTransferredFileSize() {
		return transferredFileSize;
	}


	/**
	 * method used to delete a file/folder
	 * @param file is file/folder to be deleted
	 */
	private static void deleteFile(File file) {
		if(file == null || !file.exists())
			return;
		try {
			if(file.isDirectory()) {
				for(File sub : file.listFiles()) {
					if(sub.isDirectory())
						deleteFile(sub);
					sub.delete();
				}
			}
			file.delete();	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
