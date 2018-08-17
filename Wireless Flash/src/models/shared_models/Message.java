package models.shared_models;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Class used to exchange simple messages between server and client
 */
public class Message {
	private final static int DOWNLOAD_MESSAGE = 0;
	private final static int UPLOAD_MESSAGE = 1;
	private final static int DELETE_MESSAGE = 2;
	private final static int BROWSE_MESSAGE = 3;
	private final static int ERROR_MESSAGE = 4;
	private final static int SUCCESS_MESSAGE = 5;
	
	private int messageType;
	private String messageInfo;
	
	
	/**
	 * Constructor for Message object
	 */
	public Message() {}
	
	/**
	 * get method for messageType
	 * @return the type of message as an integer
	 */
	public int getMessageType() {
		return messageType;
	}

	/**
	 * set method for messageType
	 * @param messageType is the new type of the message
	 */
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	/**
	 * get method for messageInfo
	 * @return extra information about the message
	 */
	public String getMessageInfo() {
		return messageInfo;
	}
	
	/**
	 * set method for messageInfo
	 * @param messageInfo is some new information for the message
	 */
	public void setMessageInfo(String messageInfo) {
		this.messageInfo = messageInfo;
	}

	/**
	 * method used to check of current message is a download message
	 * @return a boolean that indicates if the message is a download message or not
	 */
	@JsonIgnore
	public boolean isDownloadMessage() {
		return this.messageType == Message.DOWNLOAD_MESSAGE;
	}
	
	/**
	 * method used to check of current message is an upload message
	 * @return a boolean that indicates if the message is an upload message or not
	 */
	@JsonIgnore
	public boolean isUploadMessage() {
		return this.messageType == Message.UPLOAD_MESSAGE;
	}
	
	/**
	 * method used to check of current message is a delete message
	 * @return a boolean that indicates if the message is a delete message or not
	 */
	@JsonIgnore
	public boolean isDeleteMessage() {
		return this.messageType == Message.DELETE_MESSAGE;
	}
	
	/**
	 * method used to check of current message is a browse message
	 * @return a boolean that indicates if the message is a download browse or not
	 */
	@JsonIgnore
	public boolean isBrowseMessage() {
		return this.messageType == Message.BROWSE_MESSAGE;
	}
	
	/**
	 * method used to check of current message is an error message
	 * @return a boolean that indicates if the message is an error message or not
	 */
	@JsonIgnore
	public boolean isERRORMessage() {
		return this.messageType == Message.ERROR_MESSAGE;
	} 
	
	/**
	 * method used to check of current message is a success message
	 * @return a boolean that indicates if the message is a success message or not
	 */
	@JsonIgnore
	public boolean isSuccessMessage() {
		return this.messageType == Message.SUCCESS_MESSAGE;
	} 
	
	/**
	 * method used to create a download message
	 * @param info is some extra info for the message (file path)
	 */
	public void createDownloadMessage(String info) {
		this.messageType = Message.DOWNLOAD_MESSAGE;
		this.messageInfo = info;
	}
	
	/**
	 * method used to create an upload message
	 * @param info is some extra info for the message (where to save file)
	 */
	public void createUploadMessage(String info) {
		this.messageType = Message.UPLOAD_MESSAGE;
		this.messageInfo = info;
	}
	
	/**
	 * method used to create a delete message
	 * @param info is some extra info for the message (file path)
	 */
	public void createDeleteMessage(String info) {
		this.messageType = Message.DELETE_MESSAGE;
		this.messageInfo = info;
	}
	
	/**
	 * method used to create a browse message
	 * @param info is some extra info for the message (file path)
	 */
	public void createBrowseMessage(String info) {
		this.messageType = Message.BROWSE_MESSAGE;
		this.messageInfo = info;
	}
	
	/**
	 * method used to create a success message
	 * @param info is some extra info for the message (usually empty)
	 */
	public void createSuccessMessage(String info) {
		this.messageType = Message.SUCCESS_MESSAGE;
		this.messageInfo = info;
	}
	
	/**
	 * method used to create an error message
	 * @param info is some extra info for the message (usually empty)
	 */
	public void createErrorMessage(String info) {
		this.messageType = Message.ERROR_MESSAGE;
		this.messageInfo = info;
	}	
}
