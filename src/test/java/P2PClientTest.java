import com.uzh.csg.overlaynetworks.domain.dto.Contact;
import com.uzh.csg.overlaynetworks.domain.dto.Message;
import com.uzh.csg.overlaynetworks.domain.dto.MessageResult;
import com.uzh.csg.overlaynetworks.domain.exception.LoginFailedException;
import com.uzh.csg.overlaynetworks.domain.exception.MessageSendFailureException;
import com.uzh.csg.overlaynetworks.p2p.P2PClient;
import com.uzh.csg.overlaynetworks.p2p.P2PClientDelegate;
import com.uzh.csg.overlaynetworks.p2p.PeerInfo;
import com.uzh.csg.overlaynetworks.p2p.error.P2PError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yurybelevskiy on 27.05.17.
 */
public class P2PClientTest implements P2PClientDelegate {

	private List<P2PClient> clients;

	public P2PClientTest(List<P2PClient> clients) {
		this.clients = clients;
		for (P2PClient client : this.clients) {
			client.delegate = this;
		}
	}

	/* Assumes that bootstrapping server is running and P2PClient is configured with correct IP and port */
	public void bootstrapClients() {
		for(P2PClient client : clients) {
			try {
				client.start();
			} catch (LoginFailedException lfe) {
				System.err.println("Failed to start client " + client);
				lfe.printStackTrace();
			}
		}
	}

	public void didLogin(PeerInfo peer, P2PError error) throws LoginFailedException {
		if(error != null) {
			System.err.println("Failed to login client " + peer.getUsername() + "! Error is: " + error.getErrorMessage());
		} else {
			System.out.println("DID LOGIN: " + peer.getUsername());
		}
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
