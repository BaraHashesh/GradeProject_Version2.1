package models.shared_models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * This class is used to parse object into json string 
 * as well as parse object from json string
 */
public class JsonParser {
	/**
	 * method used to convert BasicFileData objects into json string
	 * @param basicFileData is a group of BasicFileData objects
	 * @return json string containing BasicFileData objects
	 */
	public static String basicFileDataToJson(BasicFileData... basicFileData){
		try {
			ObjectWriter ow = new ObjectMapper().writer();
			return ow.writeValueAsString(basicFileData);
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * method used to convert json string into BasicFileData objects
	 * @param json is json string containing BasicFileData objects
	 * @return list of BasicFileData Objects
	 */
	public static BasicFileData[] jsonToBasicFileData(String json){
		try {
			ObjectMapper om = new ObjectMapper();
			return om.readValue(json, BasicFileData[].class);
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * method used to convert Message objects into json string
	 * @param message is a Message objects
	 * @return json string containing a Message object
	 */
	public static String messageToJson(Message message) {
		try {
			ObjectWriter ow = new ObjectMapper().writer();
			return ow.writeValueAsString(message);
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * method used to convert json string into a Message object
	 * @param json is json string containing a Message object
	 * @return a Message object
	 */
	public static Message jsonToMessage(String json){
		try {
			ObjectMapper om = new ObjectMapper();
			return om.readValue(json, Message.class);
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}


