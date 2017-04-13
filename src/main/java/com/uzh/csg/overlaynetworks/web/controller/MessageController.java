package com.uzh.csg.overlaynetworks.web.controller;

import static java.lang.Math.random;
import static java.lang.Math.round;

import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.uzh.csg.overlaynetworks.web.dto.FriendDTO;
import com.uzh.csg.overlaynetworks.web.dto.FriendWithStatusDTO;
import com.uzh.csg.overlaynetworks.web.dto.LoginDTO;
import com.uzh.csg.overlaynetworks.web.dto.MessageDTO;
import com.uzh.csg.overlaynetworks.web.dto.MessageResultDTO;
import com.uzh.csg.overlaynetworks.web.dto.ResultDTO;
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

	@MessageMapping("/login")
	@SendTo("/topic/login")
	public ResultDTO login(LoginDTO loginDTO) {
		// TODO subscribe the person somewhere
		// TODO return a result dto (true/false)

		return new ResultDTO(true);
	}

	@MessageMapping("/friends-status")
	@SendTo("/topic/friends-status")
	public List<FriendWithStatusDTO> getFriendsWithStatus(List<FriendDTO> friends) {
		// TODO get status of friends
		// TODO return a correct FriendsWithStatusDTO list

		List<FriendWithStatusDTO> output = new ArrayList<>();
		for(FriendDTO friend : friends) {
			output.add(new FriendWithStatusDTO(friend, (random() < 0.5)));
		}
		return output;
	}

	@MessageMapping("/friend-exists")
	@SendTo("/topic/friend-exists")
	public ResultDTO friendExists(FriendDTO friend) {
		// TODO check, if the friend exists

		return new ResultDTO(random() < 0.5);
	}

	@MessageMapping("/send-message")
	@SendTo("/topic/send-message")
	public MessageResultDTO sendMessage(MessageDTO message) {
		// TODO send message into network
		// TODO return a valid message id, which will later be linked to the ether value

		return new MessageResultDTO(true, round(random() * 10000));
	}

}