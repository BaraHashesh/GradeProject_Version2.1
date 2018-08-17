package models.client_models.connection;

import models.client_models.EstimationViewManagementThread;
import models.shared_models.FileTransfer;
import models.shared_models.JsonParser;
import models.shared_models.Message;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * This class is used to upload files to server on a separate thread
 */
public class UploadClient implements Runnable {
	private File file;
	private String locationToSave;
	private String IP;
	private Thread thread;

	/**
	 * constructor for specific server
	 */
	public UploadClient(String hostIP) {
		this.IP = hostIP;
	}

	/**
	 * Initialize method for variables as well as method used to start
	 * operations
	 * 
	 * @param file
	 *            is file to be uploaded
	 * @param locationToSave
	 *            is the location to save file under within the USB
	 */
	public void start(File file, String locationToSave) {
		this.file = file;
		this.locationToSave = locationToSave;
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	/**
	 * upload is done here
	 */
	@Override
	public void run() {
		String request;
		try {
			SocketBuilder socketBuilder = new SocketBuilder(this.IP);

			Socket clientSocketStrings = socketBuilder.createStringSocket();
			Socket clientSocketBytes = socketBuilder.createByteSocket();

			DataOutputStream outToServerString = new DataOutputStream(clientSocketStrings.getOutputStream());

			DataOutputStream outToServerBytes = new DataOutputStream(clientSocketBytes.getOutputStream());

			BufferedReader inFromServer = new BufferedReader(
					new InputStreamReader(clientSocketStrings.getInputStream(), StandardCharsets.UTF_8));
			
			Message requestMessage = new Message();
			requestMessage.createUploadMessage(locationToSave);

			request = JsonParser.messageToJson(requestMessage);

			outToServerString.write(request.getBytes("UTF-8"));
			outToServerString.writeByte('\n');

			Message responseMessage = JsonParser.jsonToMessage(inFromServer.readLine());

			// check if operation is possible
			if (responseMessage.isERRORMessage()) {
				/*
				 * Handle Error Here
				 */
			} else {
				String parent = file.getParent();
				FileTransfer fileTransfer = new FileTransfer();
				EstimationViewManagementThread manage = new EstimationViewManagementThread(
						fileTransfer.calculateSize(file), fileTransfer, clientSocketStrings, clientSocketBytes);
				manage.start();
				fileTransfer.sendFiles(outToServerString, outToServerBytes, file, parent);
			}
			
			outToServerBytes.close();
			inFromServer.close();
			outToServerString.close();
			clientSocketStrings.close();
			clientSocketBytes.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
