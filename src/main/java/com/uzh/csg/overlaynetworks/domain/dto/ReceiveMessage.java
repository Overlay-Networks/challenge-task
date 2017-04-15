package com.uzh.csg.overlaynetworks.domain.dto;

public class ReceiveMessage {

	private Contact sender;
	private String message;

	public ReceiveMessage(Contact sender, String message) {
		this.sender = sender;
		this.message = message;
	}
	public ReceiveMessage() { }

	public Contact getSender() {
		return sender;
	}

	public void setSender(Contact sender) {
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ReceiveMessageDTO{sender:"+sender+",message:"+message+",}";
	}

}
