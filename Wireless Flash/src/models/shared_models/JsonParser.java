package models.shared_models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import models.client_models.RowData;

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
	public static BasicFileData[] JsonToBasicFileData(String json){
		try {
			ObjectMapper om = new ObjectMapper();
			return om.readValue(json, RowData[].class);
		}catch (Exception e) {
			//e.printStackTrace();
			return null;
		}
	}
}


