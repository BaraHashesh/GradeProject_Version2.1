package models.server_models;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import models.shared_models.JsonParser;

/**
 * This Class is used to Handle connection Sockets that the server creates Using Threads
 */
public class ServerHandler implements Runnable{
	private static final String DOWNLOAD_REQUEST = "Download";
	private static final String UPLOAD_REQUEST = "Upload";
	private static final String DELETE_REQUEST = "Delete";
	private static final String BROWSER_REQUEST = "Browser";
	private static final String TEST_REQUEST = "Test";
	
	private Socket connectionSocket;
	private Socket byteSocket;
	private Thread thread;
	
	/**
	 * constructor for handler
	 * @param socket is the connection socket between server and client
	 */
	public ServerHandler(Socket socket, Socket bytes) {
		this.connectionSocket = socket;
		this.byteSocket = bytes;
	}
	
	public void start() {
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	@Override
	public void run() {
		String clientRequest, response;
		
		try {
			BufferedReader inFromClient = new BufferedReader(
	                 new InputStreamReader(
	 	                    connectionSocket.getInputStream(), StandardCharsets.UTF_8));

			DataInputStream inputStreamBytes = new DataInputStream(byteSocket.getInputStream());
		
			DataOutputStream outToClientStrings = new DataOutputStream(connectionSocket.getOutputStream());
			DataOutputStream outToClientBytes = new DataOutputStream(byteSocket.getOutputStream());


			clientRequest = inFromClient.readLine();
		
			if(clientRequest.compareTo(BROWSER_REQUEST) == 0) {//browser request
				String path = inFromClient.readLine();
				response = JsonParser.basicFileDataToJson(StorageHandler.fileLister(path)) + "\n";
				outToClientStrings.write(response.getBytes("UTF-8"));
			}	
	
			if(clientRequest.compareTo(DELETE_REQUEST) == 0) {//delete request
				String path = inFromClient.readLine();
				StorageHandler.deleteFile(path);
			}
		
		
			if(clientRequest.compareTo(DOWNLOAD_REQUEST) == 0) {//download request
				String path = inFromClient.readLine();
				StorageHandler.uploadFile(outToClientStrings, outToClientBytes, path);
			}
			
			if(clientRequest.compareTo(UPLOAD_REQUEST) == 0) {//upload request
				String path = inFromClient.readLine();
				StorageHandler.downloadFile(inputStreamBytes, inFromClient, path);
			}
		
			if(clientRequest.compareTo(TEST_REQUEST) == 0) {
				String path = inFromClient.readLine();
				StorageHandler.uploadFile(outToClientStrings, outToClientBytes, path);
			}

			inputStreamBytes.close();
			inFromClient.close();
			outToClientStrings.close();
			connectionSocket.close();
			outToClientBytes.close();
			byteSocket.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
