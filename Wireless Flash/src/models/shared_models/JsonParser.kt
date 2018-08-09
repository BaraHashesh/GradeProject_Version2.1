package models.shared_models

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * This class is used to parse object into json string
 * as well as parse object from json string
 */
class JsonParser {
    /**
     * method used to convert BasicFileData objects into json string
     * @vararg basicFileData is a group of BasicFileData objects
     * @return json string containing BasicFileData objects
     */
    companion object {
        fun basicFileDataToJson(basicFileData: Array<BasicFileData?>? ): String {
            return try {
                val ow = ObjectMapper().writer()
                ow.writeValueAsString(basicFileData)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }

        /**
         * method used to convert json string into BasicFileData objects
         * @param json is json string containing BasicFileData objects
         * @return list of BasicFileData Objects
         */
        fun JsonToBasicFileData(json: String): Array<BasicFileData>? {
            return try {
                val om = ObjectMapper()
                om.readValue(json, Array<BasicFileData>::class.java)
            } catch (e: Exception) {
                //e.printStackTrace();
                null
            }
        }
    }

}


