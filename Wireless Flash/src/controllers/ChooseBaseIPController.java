package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class ChooseBaseIPController {

    private static Scene networkScene;

    /**
     * method used to convert FXML file to a javafx scene
     * @return Scene of the FXML file
     */
    public Scene getScene() {
        if(networkScene != null)
            return networkScene;
        try {
            Parent p = FXMLLoader.load(getClass().getResource("/views/ChooseBaseIPView.fxml"));
            networkScene = new Scene(p);
            return networkScene;
        } catch (IOException e) {
            System.out.println(e.toString());
            return null;
        }
    }
}
