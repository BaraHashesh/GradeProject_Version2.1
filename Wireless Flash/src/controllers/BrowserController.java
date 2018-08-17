package controllers;

import java.io.File;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.client_models.OperatingSystemAdapter;
import models.client_models.RowData;
import models.client_models.connection.BrowsingClient;
import models.client_models.connection.DownloadClient;
import models.client_models.connection.UploadClient;

public class BrowserController implements Initializable{
	private static String IP ;
	private static Scene browserScene;
	private static Stage browserStage;
	private static ObservableList<RowData> list;
	private static String parentDirectory = "";
	private static BrowsingClient browsingClient = null;
	
	@FXML 
	private Label labelPath;
	
	@FXML
	private TableView<RowData> fileTable;

	
	/**
	 * method used to convert FXML file to a javafx scene 
	 * @return Scene of the FXML file
	 */
	public Stage getStage() {
		if(browserScene != null)
			return browserStage;
		try {
			Parent p = FXMLLoader.load(getClass().getResource("/views/BrowserView.fxml"));
			browserScene = new Scene(p);
			browserStage = new Stage();
			browserStage.setScene(browserScene);
			return browserStage;
		} catch (IOException e) {
			System.out.println(e.toString());
			return null;
		}
	}

	/**
	 * set the observable list connected to the Table
	 * @param listTODisplay list of files to display
	 */
	public static void setList(RowData[] listTODisplay) {
		list = FXCollections.observableArrayList(listTODisplay);
	}
	
	/**
	 * method used to initialize the table view
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		TableColumn<RowData, Image> image = new TableColumn<>("");
		TableColumn<RowData, String> name = new TableColumn<>("name");
    	TableColumn<RowData, String> type = new TableColumn<>("type");
    	TableColumn<RowData, String> date = new TableColumn<>("Last Modified");
    	TableColumn<RowData, String> size = new TableColumn<>("size");
    	
    	image.setCellValueFactory(new PropertyValueFactory<>("icon"));
    	name.setCellValueFactory(new PropertyValueFactory<>("name"));
    	type.setCellValueFactory(new PropertyValueFactory<>("type"));
    	date.setCellValueFactory(new PropertyValueFactory<>("modifiedDate"));
    	size.setCellValueFactory(new PropertyValueFactory<>("sizeBytes"));

    	name.setPrefWidth(400);
    	type.setPrefWidth(200);
    	date.setPrefWidth(150);
    	size.setPrefWidth(100);
		
		fileTable.getColumns().clear();
		fileTable.getColumns().addAll(image, name, type, date, size);

		fileTable.setItems(list);
		
		fileTable.setRowFactory( tv -> {
		    TableRow<RowData> row = new TableRow<>();
		    row.setOnMouseClicked(event -> {
		        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
					nextDirectory();
		        }
		    });
		    return row ;
		});
	}
	
	/**
	 * Action that is used to move downward in the USB file tree
	 */
	private void nextDirectory(){
		RowData file = fileTable.getSelectionModel().getSelectedItem();
		
		if(file != null && file.isDirectory()) {
			String newPath = file.getPath();
			parentDirectory = file.getParent();
			if(parentDirectory == null)
				parentDirectory = "";
			setList(browsingClient.browserRequest(newPath));
			updateLabel(newPath);
			fileTable.setItems(list);
		}
	}
	
	/**
	 * Action that is used to move backward in the USB file tree
	 */
	public void PreviousDirectory() {
		if(list.toArray().length == 0) {
			setList(browsingClient.browserRequest(parentDirectory));
			updateLabel(parentDirectory);
		}
		else {
			String path = list.get(0).getPreviousDirectory();
			setList(browsingClient.browserRequest(path));
			updateLabel(list.get(0).getPath().
					substring(0, list.get(0).getPath().lastIndexOf(list.get(0).getName())));
		}
		fileTable.setItems(list);
	}
	
	/**
	 * update the string in the label
	 * @param path is the string to be saved in the label
	 */
	private void updateLabel(String path) {
		labelPath.setText(path);
	}

	/**
	 * method used to delete files
	 */
	public void delete() {
		RowData file = fileTable.getSelectionModel().getSelectedItem();
		browsingClient.deleteRequest(file.getPath());
		list.remove(file);
		fileTable.setItems(list);
	}
	
	/**
	 * method used to choose file to download
	 */
	public void download() {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Place to Save");
		File defaultDirectory = new File(".");
		chooser.setInitialDirectory(defaultDirectory);
		RowData file = fileTable.getSelectionModel().getSelectedItem();
        File directoryChooser = chooser.showDialog(null);
        if(directoryChooser != null)
        	new DownloadClient(IP).start(file.getPath(),
					directoryChooser.getAbsolutePath()+OperatingSystemAdapter.getOS().getDash());
	}
	
	/**
	 * method used to choose file to upload
	 */
	public void uploadFile() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("File to Upload");
		File defaultDirectory = new File(".");
		chooser.setInitialDirectory(defaultDirectory);
        File FileChooser = chooser.showOpenDialog(null);
        if(FileChooser != null)
        	new UploadClient(IP).start(FileChooser, labelPath.getText());
	}
	
	/**
	 * method used to choose folder to upload
	 */
	public void uploadFolder() {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Folder to Upload");
		File defaultDirectory = new File(".");
		chooser.setInitialDirectory(defaultDirectory);
        File directoryChooser = chooser.showDialog(null);
        if(directoryChooser != null)
        	new UploadClient(IP).start(directoryChooser, labelPath.getText());
	}

	/**
	 * set method for server IP
	 * @param hostIP is the server IP
	 */
	public static void setIP(String hostIP) {
		IP = hostIP;
		browsingClient = new BrowsingClient(IP);
	}
	
	/**
	 * destory method for singlton variables of the object
	 */
	public static void destorySinglton() {
		BrowserController.browserScene = null;
		BrowserController.browserStage = null;
	}
}
