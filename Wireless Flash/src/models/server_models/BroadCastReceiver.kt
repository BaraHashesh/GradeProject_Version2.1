package models.server_models


import java.net.*

/**
 * Class used by server to receive discover massages from clients and then replay to them
 */
class BroadCastReceiver : Thread() {

    private var thread: Thread? = null

    /**
     * method used to start the thread of the broadcast receiving operations
     */
    override fun start() {
        if (this.thread == null) {
            this.thread = Thread(this)
            this.thread!!.start()
        }
    }

    /**
     * run method used to receive discovery messages
     */
    override fun run() {
        val socket: DatagramSocket
        try {
            socket = DatagramSocket(10000)
            while (true) {
                val buffer = ByteArray(1)
                var packet = DatagramPacket(buffer, buffer.size)
                socket.receive(packet)
                val address = packet.address
                val port = packet.port
                packet = DatagramPacket(buffer, buffer.size, address, port)
                socket.send(packet)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
