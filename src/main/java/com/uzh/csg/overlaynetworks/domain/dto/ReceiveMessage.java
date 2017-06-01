package com.uzh.csg.overlaynetworks.domain.dto;

public class ReceiveMessage {

	private Contact sender;
	private String message;
	private long messageId;
	private boolean notary;

	public ReceiveMessage(Contact sender, String message, long messageId, boolean notary) {
		this.sender = sender;
		this.message = message;
		this.messageId = messageId;
		this.notary = notary;
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

	public boolean getNotary() {
		return notary;
	}

	public void setNotary(boolean notary) {
		this.notary = notary;
	}

	@Override
	public String toString() {
		return "ReceiveMessageDTO{sender:"+sender+",message:"+message+",messageId:"+messageId+",notary:"+notary+"}";
	}

}
