package models.server_models;


import java.net.*;

/**
 * Class used by server to receive discover massages from clients and then replay to them
 */
public class BroadCastReceiver extends Thread {

    private Thread thread = null;

    /**
     * method used to start the thread of the broadcast receiving operations
     */
    public void start(){
        if (this.thread == null){
            this.thread = new Thread(this);
            this.thread.start();
        }
    }

    /**
     * run method used to receive discovery messages
     */
    public void run() {
    	DatagramSocket socket;
        try {
            socket = new DatagramSocket(10000);

            //noinspection InfiniteLoopStatement
            while (true) {
                byte[] buffer = new byte[1];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(packet);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
