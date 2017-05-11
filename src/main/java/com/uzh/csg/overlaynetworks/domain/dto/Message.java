package com.uzh.csg.overlaynetworks.domain.dto;

public class Message {

	private Contact receiver;
	private String message;
	private boolean notary;

	public Message(Contact receiver, String message, boolean notary) {
		this.receiver = receiver;
		this.message = message;
		this.notary = notary;
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

	public boolean getNotary() {
		return notary;
	}

	public void setNotary(boolean notary) {
		this.notary = notary;
	}

	@Override
	public String toString() {
		return "MessageDTO{receiver:"+receiver+",message:"+message+",notary:"+notary+"}";
	}

}
