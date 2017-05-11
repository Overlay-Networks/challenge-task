package com.uzh.csg.overlaynetworks.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uzh.csg.overlaynetworks.domain.dto.ContactWithStatus;
import com.uzh.csg.overlaynetworks.domain.dto.ReceiveMessage;
import com.uzh.csg.overlaynetworks.domain.dto.ReceiveNotary;

@RestController
@RequestMapping("/fake")
public class FakeController {

	@Autowired
	private SimpMessagingTemplate websocket;

	@RequestMapping(value = "/receive-message", method = POST, consumes = APPLICATION_JSON_UTF8_VALUE)
	public void receiveMessage(@RequestBody ReceiveMessage message) {
		websocket.convertAndSend("/topic/receive-message", message);
	}

	@RequestMapping(value = "/receive-notary", method = POST, consumes = APPLICATION_JSON_UTF8_VALUE)
	public void receiveNotary(@RequestBody ReceiveNotary notary) {
		websocket.convertAndSend("/topic/receive-notary", notary);
	}

	@RequestMapping(value = "/update-contacts", method = POST, consumes = APPLICATION_JSON_UTF8_VALUE)
	public void updateContacts(@RequestBody Set<ContactWithStatus> contacts) {
		websocket.convertAndSend("/topic/update-contacts", contacts);
	}

}