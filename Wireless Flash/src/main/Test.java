package main;

import java.io.File;

import models.shared_models.JsonParser;
import models.shared_models.Message;

public class Test{
   
	
   public static void main(String[] args) {
	   Message temp = new Message();
	   temp.createBrowseMessage("test");
	   
	   String json = JsonParser.messageToJson(temp);
	   Message re = JsonParser.jsonToMessage(json);
			   
	   System.out.println(json);
	   System.out.println(re);
	   
	   File x = new File("D:\\baraa\\temp\\license.lic");
	   System.out.println(x.exists());
	   System.out.println(x.getPath());
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
}