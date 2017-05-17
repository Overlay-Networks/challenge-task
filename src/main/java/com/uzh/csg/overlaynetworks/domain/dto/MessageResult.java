package com.uzh.csg.overlaynetworks.domain.dto;


import java.util.UUID;

public class MessageResult {

	private long messageId;

	public MessageResult(long messageId) {
		this.messageId = messageId;
	}

	public MessageResult() {
		this.messageId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
	}

	public long getMessageId() {
		return messageId;
	}

	@Override
	public String toString() {
		return "MessageResultDTO{messageId:"+messageId+"}";
	}

}
