package models.client_models.connection;

import models.client_models.EstimationViewManagementThread;
import models.shared_models.FileTransfer;
import models.shared_models.JsonParser;
import models.shared_models.Message;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * This class is used to download files from server on a separate thread
 */
public class DownloadClient implements Runnable {
	private String path;
	private String locationToSave;
	private String IP;
	private Thread thread;

	/**
	 * constructor for specific server
	 */
	public DownloadClient(String hostIP) {
		this.IP = hostIP;
	}

	/**
	 * Initialize method for variables as well as method used to start
	 * operations
	 * 
	 * @param path
	 *            is the path of file within the USB to download
	 * @param locationToSave
	 *            is the path in which to save files under
	 */
	public void start(String path, String locationToSave) {
		this.path = path;
		this.locationToSave = locationToSave;
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	/**
	 * download is done here
	 */
	@Override
	public void run() {
		String request;
		try {
			SocketBuilder socketBuilder = new SocketBuilder(this.IP);

			Socket clientSocketStrings = socketBuilder.createStringSocket();
			Socket clientSocketBytes = socketBuilder.createByteSocket();

			DataOutputStream outToServer = new DataOutputStream(clientSocketStrings.getOutputStream());

			DataInputStream bytesStream = new DataInputStream(clientSocketBytes.getInputStream());

			BufferedReader inFromServer = new BufferedReader(
					new InputStreamReader(clientSocketStrings.getInputStream(), StandardCharsets.UTF_8));
			
			Message requestMessage = new Message();
			requestMessage.createDownloadMessage(path);

			request = JsonParser.messageToJson(requestMessage);

			outToServer.write(request.getBytes("UTF-8"));
			outToServer.writeByte('\n');

			Message responseMessage = JsonParser.jsonToMessage(inFromServer.readLine());

			// check if operation is possible
			if (responseMessage.isERRORMessage()) {
				/*
				 * Handle Error Here
				 */
			} else {

				FileTransfer fileTransfer = new FileTransfer();

				long size = Long.parseLong(inFromServer.readLine());

				EstimationViewManagementThread manage = new EstimationViewManagementThread(size, 
						fileTransfer, clientSocketStrings, clientSocketBytes);
				manage.start();
				fileTransfer.receiveFiles(bytesStream, inFromServer, locationToSave);
			}
			
			outToServer.close();
			bytesStream.close();
			inFromServer.close();
			clientSocketBytes.close();
			clientSocketStrings.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
