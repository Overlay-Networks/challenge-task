import com.uzh.csg.overlaynetworks.p2p.P2PClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yurybelevskiy on 27.05.17.
 */
public class P2PClientTestStart {

	private static List<P2PClient> clients = new ArrayList<>();

	private static final int NUM_CLIENTS = 20;

	public static void main(String[] args) {
		for(int i=0; i < NUM_CLIENTS; i++) {
			P2PClient client = new P2PClient("peer" + i);
			clients.add(client);
		}
		P2PClientTest p2pTest = new P2PClientTest(clients);
		p2pTest.startTest();
	}

}
