package models.client_models.connection;

import java.net.Socket;

/**
 * This class is used to build sockets for client
 */
public class SocketBuilder {
	
	private static final int PORT_STRING = 8888;
	private static final int PORT_Bytes = 9999;
	
	private String serverIP;
	
	/**
	 * constructor for SocketBuilder class
	 * @param IP is the IP of the server
	 */
	public SocketBuilder(String IP) {
		this.serverIP = IP;
	}
	
	/**
	 * method used to create sockets used to send and recieve strings
	 * @return connection socket to server
	 */
	public Socket createStringSocket() {
		try {
			return new Socket(this.serverIP, PORT_STRING);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * method used to create sockets used to recieve and send bytes
	 * @return connection socket to server
	 */
	public Socket createByteSocket() {
		try {
			return new Socket(this.serverIP, PORT_Bytes);
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
