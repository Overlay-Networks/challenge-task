package com.uzh.csg.overlaynetworks.service;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.uzh.csg.overlaynetworks.domain.dto.ReceiveNotary;
import com.uzh.csg.overlaynetworks.web3j.MessageService;

@Service
public class EtherService {

	@Autowired
	private SimpMessagingTemplate websocket;

	private MessageService messageService = new MessageService();

	/*
	 * receives a notary confirmation for a single message (with message id)
	 * result is returned asynchronously via websockets
	 */
	public void receiveNotary(long messageId) {
		// TODO receive notary from ethereum and send message id to websocket

		ReceiveNotary notary = new ReceiveNotary();
		MessageService messageService = new MessageService();

		try {
			if (messageService.isInBlockchain(messageId) == true) {
				notary.setMessageId(messageId);
				websocket.convertAndSend("/topic/receive-notary", notary);
			}
		} catch (MessagingException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Async
	public void checkIfMessageIsInBlockChain(long messageId) throws Exception {
		Thread.sleep(5000);

		if(messageService.isInBlockchain(messageId)) {
			websocket.convertAndSend("/topic/receive-notary");
		} else {
			checkIfMessageIsInBlockChain(messageId);
		}
	}

}