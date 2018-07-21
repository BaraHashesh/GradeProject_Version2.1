package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Main;
import models.client_models.RowData;
import models.client_models.ServerInfo;
import models.client_models.tcp_connection.BrowsingClient;

public class ServerInfoController implements Initializable {
	private static Scene serverInfoScene;
    private static Stage serverInfoStage;
    private static ObservableList<ServerInfo> list;
    
    @FXML
    private Button enter, cancel;
    
    @FXML
    private TableView<ServerInfo> serverInfoTable;

    /**
     * method used to convert FXML file to a javafx scene
     * @return a Stage of the FXML file
     */
    public Stage getStage() {
        if(serverInfoScene != null)
            return serverInfoStage;
        try {
            Parent p = FXMLLoader.load(getClass().getResource("/views/ServerInfoView.fxml"));
            serverInfoScene = new Scene(p);
            serverInfoStage = new Stage();
            serverInfoStage.setScene(serverInfoScene);
            return serverInfoStage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    /**
	 * set the observable list connected to the Table
	 * @param listTODisplay list of servers to display
	 */
	public static void setList(ServerInfo[] listTODisplay) {
		list = FXCollections.observableArrayList(listTODisplay);
	}

	/**
	 * method used to initialize the table view
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TableColumn<ServerInfo, String> ip = new TableColumn<>("Server IP");
    	
    	ip.setCellValueFactory(new PropertyValueFactory<>("ip"));
    	
    	serverInfoTable.getColumns().clear();
    	serverInfoTable.getColumns().addAll(ip);

    	serverInfoTable.setItems(list);
    }
	
	/**
	 * destory method for singlton variables of the object
	 */
	public static void destorySinglton() {
		ServerInfoController.serverInfoScene = null;
		ServerInfoController.serverInfoStage = null;
	}
	
	/**
	 * method used to connect to the choosen storage device
	 */
	public void browseStorage() {
		ServerInfo server = serverInfoTable.getSelectionModel().getSelectedItem();
		
		if(server != null) {
	    	BrowserController.setIP(server.getIp());
	    	RowData[] listOfFiles = new BrowsingClient(server.getIp()).browserRequest("");
	    	BrowserController.setList(listOfFiles);
	    	
	    	BrowserController.destorySinglton();
	    	Main.browserStage = new BrowserController().getStage();
	    	Main.browserStage.show();
	    	Main.serverInfoStage.close();
		}
	}
	
	
	public void close() {
		
	}
}
