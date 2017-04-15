package com.uzh.csg.overlaynetworks.web.controller;

import java.util.Set;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.uzh.csg.overlaynetworks.domain.dto.ContactWithStatus;
import com.uzh.csg.overlaynetworks.domain.dto.ReceiveMessage;
import com.uzh.csg.overlaynetworks.domain.dto.ReceiveNotary;
import com.uzh.csg.overlaynetworks.web.dto.example.Greeting;
import com.uzh.csg.overlaynetworks.web.dto.example.HelloMessage;

@Controller
public class MainWebsocketController {

	/* EXAMPLE */
	@MessageMapping("/hello") // input
	@SendTo("/topic/greetings") // output
	public Greeting greeting(HelloMessage message) throws Exception {
		Thread.sleep(1000); // simulated delay
		return new Greeting("Hello, " + message.getName() + "!");
	}
	/* EXAMPLE END */

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