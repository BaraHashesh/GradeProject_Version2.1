package models.client_models;

import java.net.Socket;

import controllers.EstimationViewController;
import models.shared_models.FileTransfer;

public class EstimationViewManagementThread implements Runnable{
	private boolean started = false;
	private EstimationViewController est;
	private FileTransfer fileTransfer;
	private long totalSize;
	
	public EstimationViewManagementThread(long size, FileTransfer ft, Socket sockStrings, Socket sockBytes) {
		est = new EstimationViewController(size, sockStrings, sockBytes);
		this.totalSize = size;
		this.fileTransfer = ft;
	}
	
	public void start() {
		Thread t = new Thread(this);
		t.start();
	}
	@Override
	public void run() {

		while(this.totalSize > fileTransfer.getTransferredFileSize()) {
			if(!started) {
				est.update(0);
				started = true;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
			else {
				est.update(fileTransfer.getTransferredFileSize());

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
		}
		est.update(this.totalSize);
	}
}