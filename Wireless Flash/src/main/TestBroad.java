package main;


import models.client_models.connection.BroadCastSender;

public class TestBroad {

    public static void main(String[] args) {
        BroadCastSender sender = new BroadCastSender("192.168.137.0");
        sender.start();

        System.out.println(sender.getResults().toString());
    }
}
