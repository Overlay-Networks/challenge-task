import com.uzh.csg.overlaynetworks.domain.dto.Contact;
import com.uzh.csg.overlaynetworks.domain.dto.Message;
import com.uzh.csg.overlaynetworks.domain.dto.MessageResult;
import com.uzh.csg.overlaynetworks.domain.exception.LoginFailedException;
import com.uzh.csg.overlaynetworks.domain.exception.MessageSendFailureException;
import com.uzh.csg.overlaynetworks.p2p.P2PClient;
import com.uzh.csg.overlaynetworks.p2p.P2PClientDelegate;
import com.uzh.csg.overlaynetworks.p2p.PeerInfo;
import com.uzh.csg.overlaynetworks.p2p.error.P2PError;

import javafx.util.Pair;
import net.tomp2p.dht.PeerDHT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Created by yurybelevskiy on 27.05.17.
 */
public class P2PClientTest implements P2PClientDelegate {

	private List<Pair<P2PClient, PeerState>> clients;

	private class PeerState {

		boolean isLoggedIn;
		Map<Long, Pair<Boolean, Boolean>> messages;

		public PeerState() {
			this.isLoggedIn = false;
			this.messages = new HashMap<>();
		}

	}

	public P2PClientTest(List<P2PClient> clients) {
		if(!clients.isEmpty()) {
			this.clients = new ArrayList<>();
		}
		for (P2PClient client : clients) {
			client.delegate = this;
			Pair<P2PClient, PeerState> peer = new Pair<>(client, new PeerState());
			this.clients.add(peer);
		}
	}

	public void startTest() {
		bootstrapClients();
	}

	/* Assumes that bootstrapping server is running and P2PClient is configured with correct IP and port */
	private void bootstrapClients() {
		for(Pair<P2PClient, PeerState> client : clients) {
			try {
				client.getKey().start();
			} catch (LoginFailedException lfe) {
				System.err.println("Failed to start client " + client);
				lfe.printStackTrace();
			}
		}
	}

	/* Send direct messages from every client to another client */
	private void sendDirectMessages() {
		for(Pair<P2PClient, PeerState> sendingClient : clients) {
			for(Pair<P2PClient, PeerState> receivingClient : clients) {
				PeerInfo sendingClientInfo = sendingClient.getKey().getPeerInfo();
				PeerInfo receivingClientInfo = receivingClient.getKey().getPeerInfo();
				if (sendingClientInfo.getUsername().compareTo(receivingClientInfo.getUsername()) == 0) {
					continue;
				} else {
					Message message = new Message();
					message.setReceiver(new Contact(receivingClientInfo.getUsername()));
					message.setMessage("Message with contents");
					MessageResult result = new MessageResult();
					receivingClient.getValue().messages.put(result.getMessageId(), new Pair<>(false, false));
					sendingClient.getKey().sendMessage(message, result);
				}
			}
		}
	}

	private boolean allClientsLoggedIn() {
		for(Pair<P2PClient, PeerState> client : clients) {
			if(!client.getValue().isLoggedIn) {
				return false;
			}
		}
		return true;
	}

	private boolean allMessagesAcked(Pair<P2PClient, PeerState> client) {
		for(Map.Entry<Long, Pair<Boolean, Boolean>> messageState : client.getValue().messages.entrySet()) {
			if(!(messageState.getValue().getKey() && messageState.getValue().getValue())) {
				return false;
			}
		}
		return true;
	}

	/* P2PClientDelegate methods */

	public void didLogin(PeerInfo peer, P2PError error) throws LoginFailedException {
		if(error != null) {
			System.err.println("Failed to login client " + peer.getUsername() + "! Error is: " + error.getErrorMessage());
		} else {
			System.out.println("DID LOGIN: " + peer.getUsername());
			for(Pair<P2PClient, PeerState> client : clients) {
				PeerInfo clientInfo = client.getKey().getPeerInfo();
				if (clientInfo.getUsername().compareTo(peer.getUsername()) == 0) {
					client.getValue().isLoggedIn = true;
					break;
				}
			}

			if(allClientsLoggedIn()) {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sendDirectMessages();
			}
		}
	}

	public void didReceiveMessage(Message message, Contact from, MessageResult result, P2PError error) {
		if(error != null) {
			System.err.println("Failed to send message! Error is: " + error.getErrorMessage());
		} else {
			System.out.println("Received message " + message.getMessage() + " from " + from.getName() + "!");
			for(Pair<P2PClient, PeerState> client : clients) {
				PeerInfo clientInfo = client.getKey().getPeerInfo();
				if(clientInfo.getUsername().compareTo(from.getName()) == 0) {
					client.getValue().messages.put(result.getMessageId(), new Pair<>(true, false));
				}
			}
		}
	}

	public void didReceiveAck(MessageResult result, P2PError error) {
		for(Pair<P2PClient, PeerState> client : clients) {
			Pair<Boolean, Boolean> messageState = client.getValue().messages.get(result.getMessageId());
			if(messageState != null) {
				client.getValue().messages.put(result.getMessageId(), new Pair<>(true, true));
			}
		}
		boolean allMessagesReceived = false;
		for (Pair<P2PClient, PeerState> client : clients) {
			allMessagesReceived &= allMessagesAcked(client);

		}
		if(allMessagesReceived) {
			System.out.println("All messages in the network have been successfully delivered and acknowledged");
		}
	}

	public void didSendMessage(P2PError error) throws MessageSendFailureException {
		System.err.println("Error sending P2P message! " + error.getErrorMessage());
	}

	public void didUpdateOnlineStatus(Contact contact, boolean isOnline, P2PError error) {

	}

	public void didShutdown(P2PError error) {

	}

}
