package models.client_models.connection;

import models.client_models.RowData;
import models.shared_models.JsonParser;
import models.shared_models.Message;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * This class is used to browse the flash and to delete files if necessary
 * 
 * @author BaraHashesh
 *
 */
public class BrowsingClient {
	private String IP;

	/**
	 * Constructor for specific server
	 * 
	 * @param clientIP
	 *            is the IP of the server
	 */
	public BrowsingClient(String clientIP) {
		this.IP = clientIP;
	}

	/**
	 * request used to fetch the information if files in a certain directory
	 * 
	 * @param path
	 *            is the path to the directory with in the USB
	 * @return information about the files in the directory if it exists
	 */
	public RowData[] browserRequest(String path) {
		String request, response;
		try {
			SocketBuilder socketBuilder = new SocketBuilder(this.IP);

			Socket clientSocketStrings = socketBuilder.createStringSocket();
			Socket clientSocketBytes = socketBuilder.createByteSocket();

			DataOutputStream outToServer = new DataOutputStream(clientSocketStrings.getOutputStream());

			/*
			 * BufferedReader inFromServer = new BufferedReader( new
			 * InputStreamReader(clientSocket.getInputStream()));
			 */

			BufferedReader inFromServer = new BufferedReader(
					new InputStreamReader(clientSocketStrings.getInputStream(), StandardCharsets.UTF_8));

			Message requestMessage = new Message();
			requestMessage.createBrowseMessage(path);

			request = JsonParser.messageToJson(requestMessage);

			outToServer.write(request.getBytes("UTF-8"));
			outToServer.writeByte('\n');

			response = inFromServer.readLine();
			
			Message responseMessage = JsonParser.jsonToMessage(response);
			
			outToServer.close();
			inFromServer.close();
			clientSocketStrings.close();
			clientSocketBytes.close();
			
			// check if operation is possible
			if (responseMessage.isSuccessMessage()) {
				return RowData.convertBasicFileDataToRowData(
						JsonParser.jsonToBasicFileData(responseMessage.getMessageInfo()));
			} else {
				/*
				 * Do Error from server handling here
				 */
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * request used to delete a file/directory on the USB
	 * 
	 * @param path
	 *            is the path to the file/directory within the flash
	 */
	public boolean deleteRequest(String path) {
		String request;
		try {
			SocketBuilder socketBuilder = new SocketBuilder(this.IP);

			Socket clientSocketStrings = socketBuilder.createStringSocket();
			Socket clientSocketBytes = socketBuilder.createByteSocket();

			DataOutputStream outToServer = new DataOutputStream(clientSocketStrings.getOutputStream());

			BufferedReader inFromServer = new BufferedReader(
					new InputStreamReader(clientSocketStrings.getInputStream(), StandardCharsets.UTF_8));
			
			Message requestMessage = new Message();
			requestMessage.createDeleteMessage(path);

			request = JsonParser.messageToJson(requestMessage);

			outToServer.write(request.getBytes("UTF-8"));
			outToServer.writeByte('\n');

			Message responseMessage = JsonParser.jsonToMessage(inFromServer.readLine());
			
			// check if operation is possible
			if (responseMessage.isERRORMessage()) {
				/*
				 * Handle Error Here
				 */
				return false;
			}
			
			inFromServer.close();
			outToServer.close();
			clientSocketStrings.close();
			clientSocketBytes.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
