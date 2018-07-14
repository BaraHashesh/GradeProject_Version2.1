package models.client_models.tcp_connection;

import java.net.*;
import java.util.ArrayList;

/**
 * Class used by clients to discover storage devices
 */
public class BroadCastSender implements Runnable{
    private String broadcastIP;

    private ArrayList<String> arrayListIP = new ArrayList<>(10);


    /**
     * @param baseIP is the base IP of the network
     */
    public BroadCastSender(String baseIP){
        String[] temp = baseIP.split("\\.");
        this.broadcastIP = temp[0] + "." + temp[1] + "." + temp[2] + ".255" ;
    }

    private Thread thread = null;

    /**
     * method used to start the thread of the broadcast discover operations
     */
    public void start(){
        if (thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * run method used to discover storage devices
     */
    public void run(){
    	DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);

            byte[] buffer = new byte[1];

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
                    InetAddress.getByName(this.broadcastIP), 10000);

            socket.send(packet); // send a broadcast message for all devices
            socket.setSoTimeout(2000);
            //noinspection InfiniteLoopStatement
            while (true) {
                packet = new DatagramPacket(buffer, buffer.length);

                socket.receive(packet); // receive replays from devices for the broad cast massage

                InetAddress address = packet.getAddress(); // get the IP of the replaying device
                arrayListIP.add(address.toString().substring(1));
            }
        }
        catch(Exception e) {
            System.out.println("time Out");
        }
    }

    public ArrayList<String> getResults() {
        try {
            Thread.sleep(2000);
        } catch (Exception e) {e.printStackTrace();}
        return this.arrayListIP;
    }
}
