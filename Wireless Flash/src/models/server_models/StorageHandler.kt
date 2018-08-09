package models.server_models

import java.io.BufferedReader
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import models.shared_models.*
/**
 * This Class is used to access the USB and get data or directories from it
 * ROOT constant is used to indicated the path for the USB
 */
object StorageHandler {

    private val ROOT = "/media/pi/"

    /**
     * Method used to get the file list in a folder in the USB
     * @param folderURL Path to folder (given root is an empty string i.e. "")
     * @return list of files in a directory
     */
    fun fileLister(folderURL: String): Array<BasicFileData?>? {
        var newFolderURL = folderURL
        if (newFolderURL.length < ROOT.length)
            newFolderURL = ROOT
        val folder = File(newFolderURL)
        return if (folder.exists()) BasicFileData.parseFile(folder.listFiles()) else null
    }

    /**
     * method used to delete a file
     * @param path is the path of the file within the USB
     */
    fun deleteFile(path: String) {
        try {
            val toDelete = File(path)
            if (toDelete.isDirectory) {
                for (sub in toDelete.listFiles()!!) {
                    if (sub.isDirectory)
                        deleteFile(sub.path)
                    sub.delete()
                }
            }
            toDelete.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    /**
     * method used to declare main requirements to upload files
     * @param outToClientStrings is output stream for sending strings
     * @param outToClientBytes is output stream for sending bytes
     * @param path is the path of the file/folder to be uploaded
     */
    fun uploadFile(outToClientStrings: DataOutputStream,
                   outToClientBytes: DataOutputStream, path: String) {
        val mainFile = File(path)
        val parent = mainFile.parent
        val fileTransfer = FileTransfer()
        try {
            outToClientStrings.write(("${fileTransfer.calculateSize(mainFile)}").toByteArray(charset("UTF-8")))
            outToClientStrings.writeByte('\n'.toInt())
        } catch (e: IOException) {
            e.printStackTrace()
        }

        fileTransfer.sendFiles(outToClientStrings, outToClientBytes, mainFile, parent)
    }

    /**
     * method used to receive files from client
     * @param fromClient is input stream
     * @param path is location to be saved with reference to the USB
     */
    fun downloadFile(bytesStream: DataInputStream, fromClient: BufferedReader, path: String) {
        var currentPath = path
        if (currentPath.compareTo("") == 0)
            currentPath = ROOT

        if (currentPath.compareTo(ROOT) != 0)
            currentPath = "$currentPath/"

        try {
            FileTransfer().receiveFiles(bytesStream, fromClient, currentPath)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}