package models.shared_models

import java.io.File


/**
 * BasicFileData object is used as a replacement for the File object
 * to exchange file information over TCP
 */
class BasicFileData(){

    /**
     * get method for path
     * @return the path of the file
     */
    /**
     * method used to set the path of the file
     * used by JsonParser Class (cast reasons) and FileTransfer Class (set relative path)
     * @param path the new path of the file
     */
    var path: String? = null
    /**
     * get method for file size
     * @return file size in bytes
     */
    var size: Long = 0
    /**
     * get method for last time modified date
     * @return date at which file was last modified
     */
    var lastModified: Long = 0
    /**
     * get method for the boolean directory varaiable
     * @return wither the file is a directory or not
     */
    var isDirectory: Boolean = false

    /**
     * simple constructor for file object -> uses predefined methods
     */
    constructor(file: File) : this() {
        this.path = file.path
        this.size = file.length()
        this.lastModified = file.lastModified()
        this.isDirectory = file.isDirectory
    }


    override fun toString(): String {
        return "BasicFileData [path=$path]"
    }

    companion object {

        /**
         * method used to convert File objects into BasicFileData objects
         * @param fileList is a list of File objects
         * @return a list of my File objects
         */
        fun parseFile(fileList: Array<File>): Array<BasicFileData?> {
            val newList = arrayOfNulls<BasicFileData>(fileList.size)
            for (i in fileList.indices)
                newList[i] = BasicFileData(fileList[i])
            return newList
        }
    }

}
