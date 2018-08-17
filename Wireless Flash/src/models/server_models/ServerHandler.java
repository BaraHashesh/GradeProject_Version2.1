package models.server_models;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import models.shared_models.JsonParser;
import models.shared_models.Message;

/**
 * This Class is used to Handle connection Sockets that the server creates Using
 * Threads
 */
public class ServerHandler implements Runnable {
	private Socket connectionSocket;
	private Socket byteSocket;
	private Thread thread;

	/**
	 * constructor for handler
	 * 
	 * @param socket
	 *            is the connection socket between server and client
	 */
	public ServerHandler(Socket socket, Socket bytes) {
		this.connectionSocket = socket;
		this.byteSocket = bytes;
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run() {
		Message clientRequest, serverResponse;

		try {
			BufferedReader inFromClient = new BufferedReader(
					new InputStreamReader(connectionSocket.getInputStream(), StandardCharsets.UTF_8));

			DataInputStream inputStreamBytes = new DataInputStream(byteSocket.getInputStream());

			DataOutputStream outToClientStrings = new DataOutputStream(connectionSocket.getOutputStream());
			DataOutputStream outToClientBytes = new DataOutputStream(byteSocket.getOutputStream());

			clientRequest = JsonParser.jsonToMessage(inFromClient.readLine());

			if (clientRequest.isBrowseMessage()) {// browser request
				String path = clientRequest.getMessageInfo();

				String conflict = checkForConflict(path);
				// check for conflicts
				if (conflict == null) {
					String files = JsonParser.basicFileDataToJson(StorageHandler.fileLister(path));

					serverResponse = new Message();
					serverResponse.createSuccessMessage(files);

					outToClientStrings.write(JsonParser.messageToJson(serverResponse).getBytes("UTF-8"));
					outToClientStrings.writeByte('\n');
				} else {
					serverResponse = new Message();
					serverResponse.createErrorMessage(conflict);
					outToClientStrings.write(JsonParser.messageToJson(serverResponse).getBytes("UTF-8"));
					outToClientStrings.writeByte('\n');
				}
			}

			if (clientRequest.isDeleteMessage()) {// delete request
				String path = clientRequest.getMessageInfo();

				String conflict = checkForConflict(path);
				// check for conflicts
				if (conflict == null) {
					StorageHandler.deleteFile(path);
					
					serverResponse = new Message();
					serverResponse.createSuccessMessage("");
					
					outToClientStrings.write(JsonParser.messageToJson(serverResponse).getBytes("UTF-8"));
					outToClientStrings.writeByte('\n');
				} else {
					serverResponse = new Message();
					serverResponse.createErrorMessage(conflict);
					outToClientStrings.write(JsonParser.messageToJson(serverResponse).getBytes("UTF-8"));
					outToClientStrings.writeByte('\n');
				}
			}
			
			if (clientRequest.isDownloadMessage()) {// download request
				String path = clientRequest.getMessageInfo();
				
				String conflict = checkForConflict(path);
				// check for conflicts
				if (conflict == null) {				
					serverResponse = new Message();
					serverResponse.createSuccessMessage("");
					
					outToClientStrings.write(JsonParser.messageToJson(serverResponse).getBytes("UTF-8"));
					outToClientStrings.writeByte('\n');
					
					StorageHandler.uploadFile(outToClientStrings, outToClientBytes, path);
				} else {
					serverResponse = new Message();
					serverResponse.createErrorMessage(conflict);
					outToClientStrings.write(JsonParser.messageToJson(serverResponse).getBytes("UTF-8"));
					outToClientStrings.writeByte('\n');
				}
			}

			if (clientRequest.isUploadMessage()) {// upload request
				String path = clientRequest.getMessageInfo();
				
				String conflict = checkForConflict(path);
				// check for conflicts
				if (conflict == null) {					
					serverResponse = new Message();
					serverResponse.createSuccessMessage("");
					
					outToClientStrings.write(JsonParser.messageToJson(serverResponse).getBytes("UTF-8"));
					outToClientStrings.writeByte('\n');
					
					StorageHandler.downloadFile(inputStreamBytes, inFromClient, path);
				} else {
					serverResponse = new Message();
					serverResponse.createErrorMessage(conflict);
					outToClientStrings.write(JsonParser.messageToJson(serverResponse).getBytes("UTF-8"));
					outToClientStrings.writeByte('\n');
				}				
			}

			inputStreamBytes.close();
			inFromClient.close();
			outToClientStrings.close();
			connectionSocket.close();
			outToClientBytes.close();
			byteSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * method used to check for read-write conflicts
	 * 
	 * @param filePath
	 *            is the path of the file to be checked
	 * @return a string if there is a conflict, null otherwise
	 */
	public String checkForConflict(String filePath) {
		return null;
	}

}
