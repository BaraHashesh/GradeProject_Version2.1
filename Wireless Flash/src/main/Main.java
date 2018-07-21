package main;

import java.net.UnknownHostException;

import controllers.ChooseBaseIPController;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
	
	public static Stage networkStage;
	public static Stage serverInfoStage;
	public static Stage browserStage;
	
	public static void main(String[] args) throws UnknownHostException {
		System.setProperty("java.net.preferIPv4Stack", "true");
		
	    Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			Main.networkStage = new ChooseBaseIPController().getStage();
			networkStage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
