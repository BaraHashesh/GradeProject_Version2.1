package main;
import java.net.*;

import models.server_models.BroadCastReceiver;
import models.server_models.ServerHandler;


/**
 * Class used to create a simple server on the device
 */
public class TCPServer {
	@SuppressWarnings({"resource"})
	public static void main(String argv[]) throws Exception {
		System.setProperty("java.net.preferIPv4Stack", "true");
		
		new BroadCastReceiver().start();
		
		ServerSocket welcomeSocket = new ServerSocket(8888);
		ServerSocket welcomeByteSocket = new ServerSocket(9999);

		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			Socket byteSocket =  welcomeByteSocket.accept();
			new ServerHandler(connectionSocket, byteSocket).start();
		}
	}
}