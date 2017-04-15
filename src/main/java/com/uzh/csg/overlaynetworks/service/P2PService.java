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
import com.uzh.csg.overlaynetworks.web.controller.MainWebsocketController;

@Service
public class P2PService {

	@Autowired
	private DataHolder dataHolder;

	@Autowired
	private MainWebsocketController socketController;

	private boolean updateContactIsRunning = false;

	@Autowired
	private SimpMessagingTemplate websocket;

	/*
	 * searches the online status for every contact every 5s
	 * returns a contact list with status.
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
	 */
	public void receiveMessage() {
		// TODO get those messages from a p2p channel

		ReceiveMessage message = new ReceiveMessage();
		message.setMessage("test123");
		message.setSender(new Contact("sender123"));

		socketController.receiveMessage(message);
	}

	/*
	 * sends messages into the p2p network,
	 * returns a unique message ID
	 */
	public MessageResult sendMessage(Message message) {
		// TODO send message into p2p network
		// TODO return a unique message ID

		System.out.println(message);
		MessageResult result = new MessageResult();
		result.setMessageId(Long.valueOf(Math.round(random() * 10000) + ""));

		return result;
	}
}
