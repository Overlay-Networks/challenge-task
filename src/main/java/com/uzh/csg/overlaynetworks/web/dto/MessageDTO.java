package com.uzh.csg.overlaynetworks.web.dto;

public class MessageDTO {

	private FriendDTO receiver;
	private String message;

	public MessageDTO(FriendDTO receiver, String message) {
		this.receiver = receiver;
		this.message = message;
	}

	public FriendDTO getReceiver() {
		return receiver;
	}

	public void setReceiver(FriendDTO receiver) {
		this.receiver = receiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
