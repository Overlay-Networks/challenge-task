package com.uzh.csg.overlaynetworks.web.controller;

import static java.lang.Math.random;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Set;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uzh.csg.overlaynetworks.web.dto.ContactDTO;
import com.uzh.csg.overlaynetworks.web.dto.LoginDTO;
import com.uzh.csg.overlaynetworks.web.dto.MessageDTO;
import com.uzh.csg.overlaynetworks.web.dto.MessageResultDTO;
import com.uzh.csg.overlaynetworks.web.exception.InvalidDataProvidedException;

@RestController
@RequestMapping("/rest")
public class MainController {

	@RequestMapping(
			value = "/login",
			method = POST,
			consumes = APPLICATION_JSON_UTF8_VALUE)
	public void login(@RequestBody LoginDTO loginDTO) {
		String name = loginDTO.getName();

		if(name == null || "".equals(name)) {
			throw new InvalidDataProvidedException();
		}

		// TODO login
	}

	@RequestMapping(
			value = "/new-contact-list",
			method = POST,
			consumes = APPLICATION_JSON_UTF8_VALUE,
			produces = APPLICATION_JSON_UTF8_VALUE)
	public void newContactList(@RequestBody Set<ContactDTO> friends) {
		// TODO create new contact list
	}

	@RequestMapping(
			value = "/new-contact",
			method = POST,
			consumes = APPLICATION_JSON_UTF8_VALUE,
			produces = APPLICATION_JSON_UTF8_VALUE)
	public void newContact(@RequestBody ContactDTO friend) {
		// TODO add contact to contact list
	}

	@RequestMapping(
			value = "/send-message",
			method = POST,
			consumes = APPLICATION_JSON_UTF8_VALUE)
	public MessageResultDTO sendMessage(@RequestBody MessageDTO message) {

		// TODO process message
		// TODO return message id
		System.out.println("Message: " + message);

		return new MessageResultDTO(Long.valueOf(Math.round(random() * 10000) + ""));
	}

}