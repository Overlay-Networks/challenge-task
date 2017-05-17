package com.uzh.csg.overlaynetworks.service;

import static java.lang.Math.random;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.uzh.csg.overlaynetworks.domain.dto.*;
import com.uzh.csg.overlaynetworks.domain.exception.MessageSendFailureException;
import com.uzh.csg.overlaynetworks.domain.exception.LoginFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.uzh.csg.overlaynetworks.domain.DataHolder;
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

	private static final Logger LOGGER = Logger.getLogger(P2PService.class.getName() );

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
	 * sends messages into the p2p network,
	 * returns a unique message ID
	 * result is returned immediately (via REST)
	 */
	public MessageResult sendMessage(Message message) {
		client.sendMessage(message.getReceiver().getName(), message.getMessage());
		MessageResult result = new MessageResult();

		return result;
	}

	/*
	 * initial call to login the user
	 */
	public void login() throws LoginFailedException {
		client = new P2PClient("TestYury");
		client.delegate = this;
		client.start();
	}

	/*
	 * final call to logout
	 */
	public void logout() {
		client.shutdown();
	}

	/* P2PClientDelegate */

	@Override
	public void didLogin(PeerInfo peer, P2PError error) throws LoginFailedException {
		if (error != null) {
			String errorMessage = error.getErrorMessage();
			LOGGER.log(Level.INFO, "Failed to login: " + errorMessage);
			throw new LoginFailedException();
		}
	}

	@Override
	public void didSendMessage(P2PError error) throws MessageSendFailureException {
		if (error != null) {
			String errorMessage = error.getErrorMessage();
			LOGGER.log(Level.INFO, "Failed sending message: " + errorMessage);
			throw new MessageSendFailureException();
		}
	}

	/*
 	* receives message which are directed to this user.
 	* redirects the message to the socket controller (front-end).
 	* result is returned asynchronously via websockets
 	*/
	@Override
	public void didReceiveMessage(String senderUsername, String message, P2PError error) {
		if (senderUsername != null && message != null) {
			ReceiveMessage receiveMessage = new ReceiveMessage();
			receiveMessage.setMessage(message);
			receiveMessage.setSender(new Contact(senderUsername));
			websocket.convertAndSend("/topic/receive-message", receiveMessage);
		} else if (error != null) {
			String errorMessage = error.getErrorMessage();
			LOGGER.log(Level.INFO, "Error receiving message: " + errorMessage);
		}
	}

	@Override
	public void didDiscoverContact(Contact contact, P2PError error) {
		// TODO Auto-generated method stub
	}

	@Override
	public void didShutdown(P2PError error) {
		if (error == null) {
			LOGGER.log(Level.INFO, "Successfully shutdown the client!z");
		} else {
			String errorMessage = error.getErrorMessage();
			LOGGER.log(Level.INFO, "Error shutting down the client: " + errorMessage);
		}
	}

}
