package com.uzh.csg.overlaynetworks.web.dto;

public class MessageResultDTO {

	private long messageId;

	public MessageResultDTO(long messageId) {
		this.messageId = messageId;
	}
	public MessageResultDTO() { }

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
