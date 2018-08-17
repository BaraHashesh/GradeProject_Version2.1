package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import main.Main;
import models.client_models.ServerInfo;
import models.client_models.connection.BroadCastSender;

import java.io.IOException;
import java.util.ArrayList;

import com.jfoenix.controls.JFXTextField;

public class ChooseBaseIPController {

    private static Scene networkScene;
    private static Stage networkStage;
    
    @FXML
    private Button enter, cancel;
    
    @FXML
    private JFXTextField ip;

    /**
     * method used to convert FXML file to a javafx scene
     * @return a Stage of the FXML file
     */
    public Stage getStage() {
        if(networkScene != null)
            return networkStage;
        try {
            Parent p = FXMLLoader.load(getClass().getResource("/views/ChooseBaseIPView.fxml"));
            networkScene = new Scene(p);
            networkStage = new Stage();
            networkStage.setScene(networkScene);
            return networkStage;
        } catch (IOException e) {
            System.out.println(e.toString());
            return null;
        }
    }
    
    public void search() {
    	BroadCastSender broadCastSender = new BroadCastSender(ip.getText());
    	broadCastSender.start();
    	ArrayList<String> availableDecivces = broadCastSender.getResults();
    	ServerInfo[] serverInfos = ServerInfo.constructServerInfo(availableDecivces);
    	
    	ServerInfoController.setList(serverInfos);
    	ServerInfoController.destorySinglton();
    	Main.serverInfoStage = new ServerInfoController().getStage();
    	Main.serverInfoStage.show();
    	Main.networkStage.close();
    }
    
    public void close() {
    	
    }
}
