package com.uzh.csg.overlaynetworks.web.controller;

import java.util.Set;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.uzh.csg.overlaynetworks.domain.dto.ContactWithStatus;
import com.uzh.csg.overlaynetworks.domain.dto.ReceiveMessage;
import com.uzh.csg.overlaynetworks.domain.dto.ReceiveNotary;

@Controller
public class MainWebsocketController {

	@SendTo("/topic/receive-message")
	public ReceiveMessage receiveMessage(ReceiveMessage message) {
		return message;
	}

	@SendTo("/topic/receive-notary")
	public ReceiveNotary receiveNotary(ReceiveNotary notary) {
		return notary;
	}

	@SendTo("/topic/update-contacts")
	public Set<ContactWithStatus> updateContacts(Set<ContactWithStatus> contactsWithStatus) {
		return contactsWithStatus;
	}

}