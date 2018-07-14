package views;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * This class is used to create the estimation view to display progress in download and upload
 */
public class EstimationViewBuilder {
	private static double width = 1200;
	private Stage estimationStage;
	private Label fileSizeLabel, informationLabel, doneLabel, remainingLabel;
	
	/**
	 * method used to build the scene of the estimation view
	 */
	public void build() {
		double spacing = 5;

		AnchorPane parent = new AnchorPane();
		VBox vbox = new VBox(spacing);
		HBox hbox = new HBox();
		
		this.fileSizeLabel = new Label();
		this.informationLabel = new Label();
		this.doneLabel = new Label();
		this.remainingLabel = new Label();
		
		this.fileSizeLabel.setPrefWidth(width);
		this.fileSizeLabel.setFont(new Font("Cambrian", 32));
		this.informationLabel.setPrefWidth(width);
		this.informationLabel.setFont(new Font("Cambrian", 16));
		
		this.doneLabel.setStyle("-fx-background-color: blue;");
		this.remainingLabel.setStyle("-fx-background-color: white;");
		
		hbox.getChildren().addAll(doneLabel, remainingLabel);
		vbox.getChildren().addAll(fileSizeLabel, informationLabel, hbox);
		parent.getChildren().add(vbox);
		
		Scene scene = new Scene(parent);
		this.estimationStage = new Stage();
		this.estimationStage.setScene(scene);
	}

	/**
	 * get method for width
	 * @return the width for the labels of the scene
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * get method for the estimationStage
	 * @return the stage of the estimation view
	 */
	public Stage getEstimationStage() {
		return estimationStage;
	}

	/**
	 * get method for fileSizeLabel
	 * @return the label responsible for displaying information about the size of the file
	 */
	public Label getFileSizeLabel() {
		return fileSizeLabel;
	}

	/**
	 * get method for informationLabel
	 * @return the label responsible for displaying information about remaining time and progress so far
	 */
	public Label getInformationLabel() {
		return informationLabel;
	}

	/**
	 * get method for doneLabel
	 * @return the label responsible for displaying percentage of job done
	 */
	public Label getDoneLabel() {
		return doneLabel;
	}

	/**
	 * get method for remainingLabel
	 * @return the label responsible for displaying percentage of job remaining
	 */
	public Label getRemainingLabel() {
		return remainingLabel;
	}

}
