package models.server_models

import models.shared_models.JsonParser
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.Socket
import java.nio.charset.StandardCharsets

/**
 * This Class is used to Handle connection Sockets that the server creates Using Threads
 */
class ServerHandler
/**
 * constructor for handler
 * @param connectionSocket is the connection socket between server and client
 */
(private val connectionSocket: Socket, private val byteSocket: Socket) : Runnable {
    private var thread: Thread? = null

    fun start() {
        if (thread == null) {
            thread = Thread(this)
            thread!!.start()
        }
    }

    override fun run() {
        val clientRequest: String
        val response: String

        try {
            val inFromClient = BufferedReader(
                    InputStreamReader(
                            connectionSocket.getInputStream(), StandardCharsets.UTF_8))

            val inputStreamBytes = DataInputStream(byteSocket.getInputStream())

            val outToClientStrings = DataOutputStream(connectionSocket.getOutputStream())
            val outToClientBytes = DataOutputStream(byteSocket.getOutputStream())


            clientRequest = inFromClient.readLine()

            if (clientRequest.compareTo(BROWSER_REQUEST) == 0) {//browser request
                val path = inFromClient.readLine()
                response = JsonParser.basicFileDataToJson(StorageHandler.fileLister(path)) + "\n"
                outToClientStrings.write(response.toByteArray(charset("UTF-8")))
            }

            if (clientRequest.compareTo(DELETE_REQUEST) == 0) {//delete request
                val path = inFromClient.readLine()
                StorageHandler.deleteFile(path)
            }


            if (clientRequest.compareTo(DOWNLOAD_REQUEST) == 0) {//download request
                val path = inFromClient.readLine()
                StorageHandler.uploadFile(outToClientStrings, outToClientBytes, path)
            }

            if (clientRequest.compareTo(UPLOAD_REQUEST) == 0) {//upload request
                val path = inFromClient.readLine()
                StorageHandler.downloadFile(inputStreamBytes, inFromClient, path)
            }

            if (clientRequest.compareTo(TEST_REQUEST) == 0) {
                val path = inFromClient.readLine()
                StorageHandler.uploadFile(outToClientStrings, outToClientBytes, path)
            }

            inputStreamBytes.close()
            inFromClient.close()
            outToClientStrings.close()
            connectionSocket.close()
            outToClientBytes.close()
            byteSocket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    companion object {
        private val DOWNLOAD_REQUEST = "Download"
        private val UPLOAD_REQUEST = "Upload"
        private val DELETE_REQUEST = "Delete"
        private val BROWSER_REQUEST = "Browser"
        private val TEST_REQUEST = "Test"
    }

}

