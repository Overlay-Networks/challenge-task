package com.uzh.csg.overlaynetworks.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {

	@RequestMapping(
		method = GET,
		produces = APPLICATION_JSON_UTF8_VALUE)
	public String getMessage() {
		return null;
	}

}