package models.client_models;

import java.util.ArrayList;

/**
 * This Object is used by the ServerInfoController Object to display informations 
 * about the servers (storage devices available for the user)
 */
public class ServerInfo {

	private String ip;
	
	public ServerInfo(String ip) {
		this.ip = ip;		
	}
	
	/**
	 * method used to construct a list of ServerInfo Objects
	 * @param primitiveServerInfo an array list of servers info as a continuous strings
	 * @return a list of ServerInfo Objects
	 */
	public static ServerInfo[] constructServerInfo(ArrayList<String> primitiveServerInfo) {
		ServerInfo[] serverInfos = new ServerInfo[primitiveServerInfo.size()];
		
		for (int i = 0; i < primitiveServerInfo.size(); i++) {
			serverInfos[i] = new ServerInfo(primitiveServerInfo.get(i));
		}
		return serverInfos;
	}

	/**
	 * get method for ip variable 
	 * @return ip of the storage device
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * set method for ip
	 * @param ip new ip of the storage device
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}	
}
