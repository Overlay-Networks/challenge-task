package com.uzh.csg.overlaynetworks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uzh.csg.overlaynetworks.domain.dto.ReceiveNotary;
import com.uzh.csg.overlaynetworks.web.controller.MainWebsocketController;

@Service
public class EtherService {

	@Autowired
	private MainWebsocketController socketController;

	/*
	 * receives a notary confirmation for a single message (with message id)
	 */
	public void receiveNotary() {
		// TODO receive notary from ethereum and send message id to websocket

		ReceiveNotary notary = new ReceiveNotary();
		notary.setMessageId(1234L);
		socketController.receiveNotary(notary);
	}

}