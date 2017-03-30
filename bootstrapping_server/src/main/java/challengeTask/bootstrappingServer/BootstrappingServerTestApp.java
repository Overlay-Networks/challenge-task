package challengeTask.bootstrappingServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class BootstrappingServerTestApp {

	/* Constants */
	private final static int serverPort = 4000;
	
	private static List<Client> clients;
	
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Please, specify IP address for bootstrapping server!");
			System.exit(0);
		} else {
			try {
				InetAddress inetAddress = InetAddress.getByName(args[0]);
				BootstrappingServer server = new BootstrappingServer(inetAddress, serverPort);
				server.start();
				
				clients = new ArrayList<Client>();
				for (int i=0; i < 5; i++) {
					Client client = new Client(serverPort + i + 5);
					client.start();
					clients.add(client);
				}
				
				for(Client client : clients) {
					client.boostrap(inetAddress, serverPort);
				}
				
			} catch (UnknownHostException uhe) {
				System.err.println(args[0] + " isn't a valid IP address! Please, provide valid IP address");
				System.exit(0);
			}
		}
		
	}

}
