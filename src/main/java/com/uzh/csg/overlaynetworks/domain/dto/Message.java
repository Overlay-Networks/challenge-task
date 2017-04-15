package com.uzh.csg.overlaynetworks.domain.dto;

public class Message {

	private Contact receiver;
	private String message;

	public Message(Contact receiver, String message) {
		this.receiver = receiver;
		this.message = message;
	}
	public Message() { }

	public Contact getReceiver() {
		return receiver;
	}

	public void setReceiver(Contact receiver) {
		this.receiver = receiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "MessageDTO{receiver:"+receiver+",message:"+message+",}";
	}

}
