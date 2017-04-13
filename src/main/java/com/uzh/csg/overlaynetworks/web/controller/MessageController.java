package com.uzh.csg.overlaynetworks.web.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.uzh.csg.overlaynetworks.web.dto.ReceiveMessageDTO;
import com.uzh.csg.overlaynetworks.web.dto.ReceiveSignDTO;
import com.uzh.csg.overlaynetworks.web.dto.example.Greeting;
import com.uzh.csg.overlaynetworks.web.dto.example.HelloMessage;

@Controller
public class MessageController {

	/* EXAMPLE */
	@MessageMapping("/hello") // input
	@SendTo("/topic/greetings") // output
	public Greeting greeting(HelloMessage message) throws Exception {
		Thread.sleep(1000); // simulated delay
		return new Greeting("Hello, " + message.getName() + "!");
	}
	/* EXAMPLE END */

	@SendTo("/topic/receive-message")
	public ReceiveMessageDTO receiveMessage(ReceiveMessageDTO message) {
		return message;
	}

	@SendTo("/topic/receive-sign")
	public ReceiveSignDTO receiveSign(ReceiveSignDTO sign) {
		return sign;
	}

}