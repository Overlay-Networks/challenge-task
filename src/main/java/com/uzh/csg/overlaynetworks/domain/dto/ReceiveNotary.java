package com.uzh.csg.overlaynetworks.domain.dto;

public class ReceiveNotary {

	private long messageId;

	public ReceiveNotary(long messageId) {
		this.messageId = messageId;
	}
	public ReceiveNotary() { }

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	@Override
	public String toString() {
		return "ReceiveNotary{messageId:"+messageId+"}";
	}

}
