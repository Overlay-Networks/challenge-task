package com.uzh.csg.overlaynetworks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.uzh.csg.overlaynetworks.web.controller.MainRestController;

@Component
public class WebsocketListener implements ApplicationListener<SessionDisconnectEvent> {

	@Autowired
	private MainRestController mainRestController;

	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
		System.out.println("Websocket Connection ("+sha.getSessionId()+") disconnected. Logging out server-side.");
		mainRestController.logout();
	}

}
