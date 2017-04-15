package com.uzh.csg.overlaynetworks.domain.dto;

public class MessageResult {

	private long messageId;

	public MessageResult(long messageId) {
		this.messageId = messageId;
	}
	public MessageResult() { }

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	@Override
	public String toString() {
		return "MessageResultDTO{messageId:"+messageId+"}";
	}

}
