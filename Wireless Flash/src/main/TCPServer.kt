package main

import java.net.*
import models.server_models.BroadCastReceiver
import models.server_models.ServerHandler

/**
 * Class used to create a simple server on the device
 */
object TCPServer {
    @Throws(Exception::class)
    @JvmStatic
    fun main(argv: Array<String>) {
        System.setProperty("java.net.preferIPv4Stack", "true")
        BroadCastReceiver().start()
        val welcomeSocket = ServerSocket(8888)
        val welcomeByteSocket = ServerSocket(9999)
        while (true) {
            val connectionSocket = welcomeSocket.accept()
            val byteSocket = welcomeByteSocket.accept()
            ServerHandler(connectionSocket, byteSocket).start()
        }
    }
}