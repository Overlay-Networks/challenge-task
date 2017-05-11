package com.uzh.csg.overlaynetworks.service;

import static java.lang.Math.random;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.uzh.csg.overlaynetworks.domain.DataHolder;
import com.uzh.csg.overlaynetworks.domain.dto.Contact;
import com.uzh.csg.overlaynetworks.domain.dto.ContactWithStatus;
import com.uzh.csg.overlaynetworks.domain.dto.Message;
import com.uzh.csg.overlaynetworks.domain.dto.MessageResult;
import com.uzh.csg.overlaynetworks.domain.dto.ReceiveMessage;
import com.uzh.csg.overlaynetworks.p2p.P2PClient;
import com.uzh.csg.overlaynetworks.p2p.P2PClientDelegate;
import com.uzh.csg.overlaynetworks.p2p.PeerInfo;
import com.uzh.csg.overlaynetworks.p2p.error.P2PError;

@Service
public class P2PService implements P2PClientDelegate {

	@Autowired
	private DataHolder dataHolder;

	@Autowired
	private SimpMessagingTemplate websocket;

	private P2PClient client;
	
	private boolean updateContactIsRunning = false;

	/*
	 * searches the online status for every contact every 5s
	 * returns a contact list with status.
	 * result is returned asynchronously via websockets
	 */
	@Scheduled(fixedDelay = 5000)
	public void updateOnlineStatusOfFriends() {
		Set<ContactWithStatus> result = new HashSet<>();
		if(!updateContactIsRunning && dataHolder.isAuthenticated() && dataHolder.getContacts().size() > 0) {
			updateContactIsRunning = true;

			for(Contact contact : dataHolder.getContacts()) {
				result.add(new ContactWithStatus(contact, getStatusForContact(contact)));
			}
			websocket.convertAndSend("/topic/update-contacts", result);
			updateContactIsRunning = false;
		}
	}
	private boolean getStatusForContact(Contact contact) {
		// TODO get status from contact via P2P network

		return random() < 0.5;
	}

	/*
	 * receives message which are directed to this user.
	 * redirects the message to the socket controller (front-end).
	 * result is returned asynchronously via websockets
	 */
	public void receiveMessage() {
		// TODO get those messages from a p2p channel

		ReceiveMessage message = new ReceiveMessage();
		message.setMessage("test123");
		message.setSender(new Contact("sender123"));

		websocket.convertAndSend("/topic/receive-message", message);
	}

	/*
	 * sends messages into the p2p network,
	 * returns a unique message ID
	 * result is returned immediately (via REST)
	 */
	public MessageResult sendMessage(Message message) {
		// TODO send message into p2p network
		// TODO return a unique message ID

		System.out.println(message);
		MessageResult result = new MessageResult();
		result.setMessageId(Long.valueOf(Math.round(random() * 10000) + ""));

		return result;
	}

	/*
	 * initial call to login the user
	 */
	public void login() {
		client = new P2PClient("TestYury");
		client.delegate = this;
		client.start();
	}

	/*
	 * final call to logout
	 */
	public void logout() {
		// TODO logout of p2p network, the user MUST be able to login again
		// this logout call happens, when the websocket connection abrupts
		// (e.g. browser window closed)
	}
	
	/* P2PClientDelegate */
	
	@Override
	public void didLogin(PeerInfo peer, P2PError error) {
		if (error != null) {
			String errorMessage = error.getErrorMessage();
			// TODO handle error
		} else if (peer != null) {
			// TODO successful login
		}
	}
	
	@Override
	public void didSendMessage(P2PError error) {
		if (error != null) {
			String errorMessage = error.getErrorMessage();
			// TODO handle error
		} else {
			// TODO message has been sent
		}
	}
	
	@Override
	public void didReceiveMessage(String senderUsername, String message, P2PError error) {
		if (senderUsername != null && message != null) {
			// TODO handle message received successfully
		} else if (error != null) {
			String errorMessage = error.getErrorMessage();
			// TODO handle error
		}
	}
	
	@Override
	public void didDiscoverContact(Contact contact, P2PError error) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void didShutdown(P2PError error) {
		if (error == null) {
			//TODO successfull shutdown
		} else {
			String errorMessage = error.getErrorMessage();
			// TODO handle error
		}
	}

}
