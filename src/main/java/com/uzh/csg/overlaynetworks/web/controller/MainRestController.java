package com.uzh.csg.overlaynetworks.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.HashSet;
import java.util.Set;

import com.uzh.csg.overlaynetworks.domain.exception.LoginFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uzh.csg.overlaynetworks.domain.DataHolder;
import com.uzh.csg.overlaynetworks.domain.dto.Contact;
import com.uzh.csg.overlaynetworks.domain.dto.Login;
import com.uzh.csg.overlaynetworks.domain.dto.Message;
import com.uzh.csg.overlaynetworks.domain.dto.MessageResult;
import com.uzh.csg.overlaynetworks.domain.exception.InvalidDataProvidedException;
import com.uzh.csg.overlaynetworks.service.P2PService;

@RestController
@RequestMapping("/rest")
public class MainRestController {

	@Autowired
	private DataHolder dataHolder;

	@Autowired
	private P2PService p2pService;

	@RequestMapping(
			value = "/login",
			method = POST,
			consumes = APPLICATION_JSON_UTF8_VALUE)
	public void login(@RequestBody Login login) throws LoginFailedException {
		String name = login.getName();

		if(name == null || "".equals(name)) {
			throw new InvalidDataProvidedException();
		}

		dataHolder.setAuthenticated(true);
		dataHolder.setUsername(name);
		dataHolder.setContacts(new HashSet<>());

		p2pService.login();
	}

	@RequestMapping(
			value = "/logout",
			method = POST,
			consumes = APPLICATION_JSON_UTF8_VALUE)
	public void logout() {

		p2pService.logout();

		dataHolder.setAuthenticated(false);
		dataHolder.setUsername(null);
		dataHolder.setContacts(new HashSet<>());
	}

	@RequestMapping(
			value = "/new-contact-list",
			method = POST,
			consumes = APPLICATION_JSON_UTF8_VALUE,
			produces = APPLICATION_JSON_UTF8_VALUE)
	public void newContactList(@RequestBody Set<Contact> contacts) {

		if(dataHolder.isAuthenticated()) {
			dataHolder.setContacts(contacts);
		} else {
			throw new InvalidDataProvidedException();
		}
	}

	@RequestMapping(
			value = "/new-contact",
			method = POST,
			consumes = APPLICATION_JSON_UTF8_VALUE,
			produces = APPLICATION_JSON_UTF8_VALUE)
	public void newContact(@RequestBody Contact contact) {

		if(dataHolder.isAuthenticated()) {
			dataHolder.getContacts().add(contact);
		} else {
			throw new InvalidDataProvidedException();
		}
	}

	@RequestMapping(
			value = "/send-message",
			method = POST,
			consumes = APPLICATION_JSON_UTF8_VALUE)
	public MessageResult sendMessage(@RequestBody Message message) {

		MessageResult result = null;

		if(dataHolder.isAuthenticated()) {
			result = p2pService.sendMessage(message);
		} else {
			throw new InvalidDataProvidedException();
		}

		return result;
	}

}
