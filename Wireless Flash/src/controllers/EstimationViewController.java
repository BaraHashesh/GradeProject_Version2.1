package controllers;

import java.io.IOException;
import java.net.Socket;

import javafx.application.Platform;
import views.EstimationViewBuilder;

/**
 * Class used to control estimation view
 */
public class EstimationViewController implements Runnable{
	private EstimationViewBuilder estimationView;
	private long fileSize;
	private Socket socketStrings;
	private Socket socketBytes;
	private long fileSizeDone;
	private long timeRemaining;
	private double transferSpeed;
	private boolean initialized = false;

	/**
	 * Constructor for the EstimationViewController Class
	 * @param fileSize is the total size of the file being transferee
	 * @param sockStrings is the stream socket for meta data of files
	 * @param sockBytes is the stream socket for in file data
	 */
	public EstimationViewController(long fileSize, Socket sockStrings, Socket sockBytes) {
		this.fileSize = fileSize;
		this.estimationView = new EstimationViewBuilder();
		this.socketStrings = sockStrings;
		this.socketBytes = sockBytes;
		Platform.runLater(this);
	}
	
	/**
	 * method used to display the estimation view
	 */
	private void display() {
		this.estimationView.build();
		initialize();
		this.estimationView.getEstimationStage().setOnCloseRequest(e -> {
			try {
				this.socketStrings.close();
				this.socketBytes.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		this.estimationView.getEstimationStage().show();
	}
	
	/**
	 * method used to initialize the estimation view
	 */
	private void initialize() {
		this.fileSizeDone = 0;

		String[] temp = reduce(this.fileSize);
		
		String sizeToDisplay = temp[0];
		String sizeInfo = temp[1];
		
		
		this.estimationView.getFileSizeLabel().setText("The size of the file is " + 
								sizeToDisplay + " " + sizeInfo);
		
		this.estimationView.getInformationLabel().setText("Calculating!!!");
		
		this.estimationView.getDoneLabel().setPrefWidth(0);
		
		this.estimationView.getRemainingLabel().setPrefWidth(this.estimationView.getWidth());
	}
	
	/**
	 * method used to round double/float values
	 * @param value is the value to round
	 * @param position is how many significant figures to keep
	 */
	private double round(double value, int position) {
		value = value * Math.pow(10, position);
		value = (double) Math.round(value);
		value = value/Math.pow(10, position);
		return value;
	}
	
	/**
	 * method used to update information of GUI
	 * @param dataDone is the size of data that has been transferred/received so far
	 */
	public void update(long dataDone) {
		this.transferSpeed = dataDone - this.fileSizeDone;
		this.fileSizeDone = dataDone;
		this.timeRemaining = (long) (((double)(this.fileSize - this.fileSizeDone))/ this.transferSpeed);
		Platform.runLater(this);
	}

	@Override
	public void run() {
		if(!this.initialized) {
			display();
			this.initialized = true;
		}
		else {
			//close file transfer view if done
			if(this.fileSizeDone == this.fileSize)
				estimationView.getEstimationStage().close();
			
			
			String[] temp = reduce(this.fileSizeDone);
			
			String sizeToDisplay = temp[0];
			String sizeInfo = temp[1];
			
			String info = sizeToDisplay + " " + sizeInfo +" have been transferred/received - ";
			
			long hours = this.timeRemaining / 360;
			this.timeRemaining  = this.timeRemaining %360;
			int minutes = (int) (this.timeRemaining / 60);
			this.timeRemaining = this.timeRemaining %60;
			
			info = info + "approximately " + hours + " hours and " + minutes + 
					" minutes and " + this.timeRemaining + " seconds are remaining to finish work";
			
			info = info + " ( " + round(this.transferSpeed/(1024.0*1024.0), 2) + " MB/s )";
			
			this.estimationView.getInformationLabel().setText(info);
			
			double percentage = estimationView.getWidth()*(double)this.fileSizeDone/(double)this.fileSize;
			
			this.estimationView.getDoneLabel().setPrefWidth(percentage);
			
			this.estimationView.getRemainingLabel().
					setPrefWidth(this.estimationView.getWidth() - percentage);
		}
	}
	
	public String[] reduce(long byteSize) {
		String[] result = new String[2];
		
		double resultByte = byteSize;
		String resultInfo = "Bytes";
		
		if(resultByte >= 1024) {
			resultByte /= 1024.0;
			resultInfo = "KB";
		}

		if(resultByte >= 1024) {
			resultByte /= 1024.0;
			resultInfo = "MB";
		}
		
		result[0] = round(resultByte, 2) + "";
		result[1] = resultInfo;
		
		return result;
	}
}
