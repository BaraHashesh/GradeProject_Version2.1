package main;

import java.net.UnknownHostException;

import controllers.BrowserController;
import javafx.application.Application;
import javafx.stage.Stage;
import models.client_models.RowData;
import models.client_models.tcp_connection.BrowsingClient;

public class Main extends Application {
	private static String IP;
	
	public static void main(String[] args) throws UnknownHostException {
		System.setProperty("java.net.preferIPv4Stack", "true");
		//IP = "172.24.1.66";
		//IP = "172.24.1.1";
		IP = "192.168.1.104";
		//IP = "192.168.137.64";
		
		RowData[] listOfFiles = new BrowsingClient(IP).browserRequest("");
		
		if(listOfFiles==null)
			return;

		BrowserController.setList(listOfFiles);
	    Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			BrowserController.setIP(IP);
			BrowserController controller = new BrowserController();
			primaryStage.setScene(controller.getScene());
			//primaryStage.setScene(new ChooseBaseIPController().getScene());
			primaryStage.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
