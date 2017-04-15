package com.uzh.csg.overlaynetworks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.uzh.csg.overlaynetworks.domain.dto.ReceiveNotary;

@Service
public class EtherService {

	@Autowired
	private SimpMessagingTemplate websocket;

	/*
	 * receives a notary confirmation for a single message (with message id)
	 * result is returned asynchronously via websockets
	 */
	public void receiveNotary() {
		// TODO receive notary from ethereum and send message id to websocket

		ReceiveNotary notary = new ReceiveNotary();
		notary.setMessageId(1234L);
		websocket.convertAndSend("/topic/receive-notary", notary);
	}

}