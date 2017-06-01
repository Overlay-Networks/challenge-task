package com.uzh.csg.overlaynetworks.service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.uzh.csg.overlaynetworks.domain.dto.*;
import com.uzh.csg.overlaynetworks.domain.exception.MessageSendFailureException;
import com.uzh.csg.overlaynetworks.domain.exception.LoginFailedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.uzh.csg.overlaynetworks.domain.DataHolder;
import com.uzh.csg.overlaynetworks.p2p.P2PClient;
import com.uzh.csg.overlaynetworks.p2p.P2PClientDelegate;
import com.uzh.csg.overlaynetworks.p2p.PeerInfo;
import com.uzh.csg.overlaynetworks.p2p.error.P2PError;
import com.uzh.csg.overlaynetworks.web3j.MessageService;



@Service
public class P2PService implements P2PClientDelegate {

	@Autowired
	private DataHolder dataHolder;

	@Autowired
	private SimpMessagingTemplate websocket;

	private P2PClient client;
	private MessageService messageService = new MessageService();
	
	private boolean updateContactIsRunning = false;
	private Set<ContactWithStatus> contactsWithStatuses = new HashSet<>();

	private static final Logger LOGGER = Logger.getLogger(P2PService.class.getName() );

	/*
	 * searches the online status for every contact every 5s
	 * returns a contact list with status.
	 * result is returned asynchronously via websockets
	 */
	@Scheduled(fixedDelay = 10000)
	public void updateOnlineStatusOfFriends() {
		if(!updateContactIsRunning && dataHolder.isAuthenticated() && !dataHolder.getContacts().isEmpty()) {
			updateContactIsRunning = true;

			for(Contact contact : dataHolder.getContacts()) {
				//contactsWithStatuses.add(new ContactWithStatus(contact, false));
				client.updateOnlineStatus(contact);
			}
		}

	}

	/*
	 * sends messages into the p2p network,
	 * returns a unique message ID
	 * result is returned immediately (via REST)
	 */
	public MessageResult sendMessage(Message message) throws InterruptedException, ExecutionException, MessageSendFailureException {
		
		MessageResult result = new MessageResult();
		client.sendMessage(message, result);
		if (message.getNotary()) {
			System.out.println("[ETH]Initiating FUTURE transaction.");
			messageService.writeToBlockchain(message, result.getMessageId(),websocket);
		}
		return result;
	}

	/*
	 * initial call to login the user
	 */
	public void login() throws LoginFailedException {
		client = new P2PClient(dataHolder.getUsername());
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
	public void didReceiveMessage(Message message, Contact from, MessageResult result, P2PError error) {
		if (message != null && result != null) {
			ReceiveMessage receiveMessage = new ReceiveMessage();
			receiveMessage.setMessage(message.getMessage());
			receiveMessage.setSender(from);
			websocket.convertAndSend("/topic/receive-message", receiveMessage);
			if (message.getNotary()) {
				try {
					if(messageService.isInBlockchain(result.getMessageId())) {
						Thread.sleep(30000);
						websocket.convertAndSend("/topic/receive-notary");
					}
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else if (error != null) {
			String errorMessage = error.getErrorMessage();
			LOGGER.log(Level.INFO, "Error receiving message: " + errorMessage);
		}
	}
	


	@Override
	public void didReceiveAck(MessageResult result, P2PError error) {
		if(result != null) {
			/* TODO: message was delivered */
		} else if (error != null) {
			String errorMessage = error.getErrorMessage();
			LOGGER.log(Level.INFO, "Error receiving ACK message: " + errorMessage);
		}
	}

	@Override
	public void didUpdateOnlineStatus(Contact contact, boolean isOnline, P2PError error) {
		if(contact != null) {
			contactsWithStatuses.add(new ContactWithStatus(contact, isOnline));
			if (allContactsStatusUpdated(contactsWithStatuses)) {
				websocket.convertAndSend("/topic/update-contacts", contactsWithStatuses);
				contactsWithStatuses.clear();
				updateContactIsRunning = false;
			}
		} else if (error != null) {
			String errorMessage = error.getErrorMessage();
			LOGGER.log(Level.INFO, "Error receiving ACK message: " + errorMessage);
		}
	}

	@Override
	public void didShutdown(P2PError error) {
		if (error == null) {
			LOGGER.log(Level.INFO, "Successfully shutdown the client!");
			client = null;
		} else {
			String errorMessage = error.getErrorMessage();
			LOGGER.log(Level.INFO, "Error shutting down the client: " + errorMessage);
		}
	}

	/* Helper methods */

	/**
	 * Returns true if all contact have their status set
	 * Otherwise, returns false
	 * @param contactsWithStatuses
	 * @return
	 */
	private boolean allContactsStatusUpdated(Set<ContactWithStatus> contactsWithStatuses) {
		return contactsWithStatuses.size() == dataHolder.getContacts().size();
	}

}
