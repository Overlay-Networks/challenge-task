package com.uzh.csg.overlaynetworks.domain.dto;

public class ReceiveMessage {

	private Contact sender;
	private String message;
	private long messageId;

	public ReceiveMessage(Contact sender, String message, long messageId) {
		this.sender = sender;
		this.message = message;
		this.messageId = messageId;
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

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	@Override
	public String toString() {
		return "ReceiveMessageDTO{sender:"+sender+",message:"+message+",messageId:"+messageId+"}";
	}

}
