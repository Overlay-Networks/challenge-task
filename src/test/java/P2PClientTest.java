import com.uzh.csg.overlaynetworks.domain.dto.Contact;
import com.uzh.csg.overlaynetworks.domain.dto.Message;
import com.uzh.csg.overlaynetworks.domain.dto.MessageResult;
import com.uzh.csg.overlaynetworks.domain.exception.LoginFailedException;
import com.uzh.csg.overlaynetworks.domain.exception.MessageSendFailureException;
import com.uzh.csg.overlaynetworks.p2p.P2PClient;
import com.uzh.csg.overlaynetworks.p2p.P2PClientDelegate;
import com.uzh.csg.overlaynetworks.p2p.PeerInfo;
import com.uzh.csg.overlaynetworks.p2p.error.P2PError;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yurybelevskiy on 27.05.17.
 */
public class P2PClientTest implements P2PClientDelegate {

	private static List<P2PClient> clients = new ArrayList<>();

	private static final int NUM_CLIENTS = 20;

	@BeforeClass
	public static void setUp() {
		for(int i=0; i < NUM_CLIENTS; i++) {
			P2PClient client = new P2PClient("peer_" + i);
			clients.add(client);
		}
	}

	@AfterClass
	public static void tearDown() {

	}

	/* Assumes that bootstrapping server is running and P2PClient is configured with correct IP and port */
	@Test
	public void bootstrapClients() {
		for(P2PClient client : clients) {
			client.delegate = this;
			try {
				client.start();
			} catch (LoginFailedException lfe) {
				assertThat(false).isTrue();
				lfe.printStackTrace();
			}
		}
	}

	@Test
	public void sendDirectMessageBetweenAllClients() {

	}
	
	public void didLogin(PeerInfo peer, P2PError error) throws LoginFailedException {
		assertThat(error == null).isTrue();
		System.out.println("DID LOGIN: " + peer.getUsername());
	}

	public void didReceiveMessage(Message message, Contact from, MessageResult result, P2PError error) {

	}

	public void didReceiveAck(MessageResult result, P2PError error) {

	}

	public void didSendMessage(P2PError error) throws MessageSendFailureException {

	}

	public void didUpdateOnlineStatus(Contact contact, boolean isOnline, P2PError error) {

	}

	public void didShutdown(P2PError error) {

	}

}
