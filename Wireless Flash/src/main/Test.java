package main;

import controllers.ChooseBaseIPController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Test extends Application{
   
	public static Stage networkStage;
	public static Stage browserStage;
	
   public static void main(String[] args) {
	   System.setProperty("java.net.preferIPv4Stack", "true");
	   Application.launch(args);
//      try {
//         Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
//         for (NetworkInterface netint : Collections.list(nets))
//            displayInterfaceInformation(netint);
//      }catch (Exception e){
//         e.printStackTrace();
//      }
	   
	   
   }
//   private static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
//      System.out.printf("Display name: %s\n", netint.getDisplayName());
//      System.out.printf("Name: %s\n", netint.getName());
//      Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
//      for (InetAddress inetAddress : Collections.list(inetAddresses)) {
//         System.out.printf("InetAddress: %s\n", inetAddress.toString().substring(1));
//      }
//      System.out.printf("\n");
//   }
   
	@Override
	public void start(Stage primaryStage) throws Exception {
		networkStage = new ChooseBaseIPController().getStage();	
		networkStage.show();
	}
}