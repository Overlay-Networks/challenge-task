package com.uzh.csg.overlaynetworks.web.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.uzh.csg.overlaynetworks.web.dto.Greeting;
import com.uzh.csg.overlaynetworks.web.dto.HelloMessage;

@Controller
public class MessageController {

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(HelloMessage message) throws Exception {
		Thread.sleep(1000); // simulated delay
		return new Greeting("Hello, " + message.getName() + "!");
	}

}